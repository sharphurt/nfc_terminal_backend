package ru.catstack.nfc_terminal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.exception.*;
import ru.catstack.nfc_terminal.model.Employee;
import ru.catstack.nfc_terminal.model.Session;
import ru.catstack.nfc_terminal.model.User;
import ru.catstack.nfc_terminal.model.enums.ApplicationStatus;
import ru.catstack.nfc_terminal.model.enums.DeviceType;
import ru.catstack.nfc_terminal.model.enums.UserPrivilege;
import ru.catstack.nfc_terminal.model.enums.UserStatus;
import ru.catstack.nfc_terminal.model.payload.request.AdminRegistrationRequest;
import ru.catstack.nfc_terminal.model.payload.request.ClientCompanyRegistrationRequest;
import ru.catstack.nfc_terminal.model.payload.request.LogOutRequest;
import ru.catstack.nfc_terminal.model.payload.request.LoginRequest;
import ru.catstack.nfc_terminal.model.payload.response.JwtAuthResponse;
import ru.catstack.nfc_terminal.security.jwt.JwtTokenProvider;
import ru.catstack.nfc_terminal.security.jwt.JwtUser;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {
    private final UserService userService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final SessionService sessionService;
    private final JwtTokenProvider tokenProvider;
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final ApplicationService applicationService;
    private final Random random = new Random();

    @Autowired
    public AuthService(UserService userService,
                       EmailService emailService, AuthenticationManager authenticationManager,
                       SessionService sessionService,
                       JwtTokenProvider tokenProvider, CompanyService companyService, EmployeeService employeeService, ApplicationService applicationService) {
        this.userService = userService;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
        this.sessionService = sessionService;
        this.tokenProvider = tokenProvider;
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.applicationService = applicationService;
    }

    public Optional<User> registerAdmin(AdminRegistrationRequest request) {
        var email = request.getEmail();
        if (emailAlreadyExists(email))
            throw new ResourceAlreadyInUseException("Email", "address", email);

        if (phoneAlreadyExists(request.getPhone()))
            throw new ResourceAlreadyInUseException("Phone", "value", request.getPhone());

        var registeredNewUser = userService.createAdmin(request);
        return Optional.ofNullable(registeredNewUser);
    }

    public Optional<Employee> registerClientCompany(ClientCompanyRegistrationRequest request) {
        if (userService.getLoggedInUser().getUserPrivilege() != UserPrivilege.ADMIN)
            throw new AccessDeniedException("You don't have permission to make this request");

        if (isRequestCorrect(request)) {
            var registeredClient = userService.createClient(request.getClient());
            var registeredCompany = companyService.createCompany(request.getCompany());
            applicationService.setStatusById(request.getApplicationToRemoveId(), ApplicationStatus.ACCEPTED);
            try {
                emailService.sendRegistrationMail(request);
            } catch (Exception e)
            {
                e.printStackTrace();
                throw new BadRequestException("Email service exception");
            }

            return employeeService.createEmployee(registeredClient, registeredCompany);
        }
        return Optional.empty();
    }

    private boolean isRequestCorrect(ClientCompanyRegistrationRequest request) {
        var client = request.getClient();
        var company = request.getCompany();

        if (emailAlreadyExists(client.getEmail()))
            throw new ResourceAlreadyInUseException("Email", "address", client.getEmail());
        if (phoneAlreadyExists(client.getPhone()))
            throw new ResourceAlreadyInUseException("Phone", "value", client.getPhone());
        if (companyService.existsByInn(company.getInn()))
            throw new ResourceAlreadyInUseException("INN", "value", company.getInn());
        if (companyService.existsByName(company.getName()))
            throw new ResourceAlreadyInUseException("Company name", "value", company.getName());
        return true;
    }

    public boolean emailAlreadyExists(String email) {
        return userService.existsByEmail(email);
    }

    public boolean phoneAlreadyExists(String phone) {
        return userService.existsByPhone(phone);
    }


    public JwtAuthResponse authenticateUser(LoginRequest loginRequest) {
        var uniqueKey = random.nextLong();

        var user = userService.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserLoginException("Неверный логин или пароль. Проверьте правильность написания введенных данных и повторите попытку"));
        if (user.getUserPrivilege() == UserPrivilege.ADMIN && loginRequest.getDeviceInfo().getDeviceType() != DeviceType.DEVICE_TYPE_WINDOWS)
            throw new AccessDeniedException("Вы не можете авторизоваться с этого устройства");

        if (user.getUserPrivilege() != UserPrivilege.ADMIN && loginRequest.getDeviceInfo().getDeviceType() == DeviceType.DEVICE_TYPE_WINDOWS)
        {
            userService.updateStatusById(user.getId(), UserStatus.LOCKED);
            throw new AccessDeniedException("Ваш аккаунт временно заблокирован. Для разблокировки обратитесь в техническую поддержку сервиса");
        }

        if (user.getUserStatus() == UserStatus.LOCKED)
            throw new AccessDeniedException("Ваш аккаунт временно заблокирован. Для разблокировки обратитесь в техническую поддержку сервиса");

        var auth = createAuthenticationOrThrow(user.getEmail(), loginRequest.getPassword());
        var principal = (JwtUser) auth.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(auth);

        var session = sessionService.createSession(auth, loginRequest, uniqueKey);
        userService.increaseLoginsCountById(principal.getId());
        var jwtToken = generateToken(principal, session);


        return new JwtAuthResponse(jwtToken, tokenProvider.getTokenPrefix());
    }

    private Authentication createAuthenticationOrThrow(String username, String password) {
        try {
            return Optional.ofNullable(authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)))
                    .orElseThrow(() -> new UserLoginException("Couldn't login user [" + username + "]"));
        } catch (BadCredentialsException e) {
            throw new UserLoginException("Неверный логин или пароль. Проверьте правильность написания введенных данных и повторите попытку");
        }
    }

    public String generateToken(JwtUser user, Session session) {
        return tokenProvider.createToken(user, session);
    }


    public void logoutUser(LogOutRequest logOutRequest) {
        var me = userService.getLoggedInUser();
        var deviceId = logOutRequest.getDeviceInfo().getDeviceId();
        var session = sessionService.findByUserIdAndDeviceId(me.getId(), deviceId);
        if (session.isEmpty())
            throw new UserLogOutException(deviceId, "Invalid device Id supplied. No matching session found for the given user");
        sessionService.deleteByDeviceIdAndUserId(session.get().getDeviceId(), session.get().getUserId());
    }
}

