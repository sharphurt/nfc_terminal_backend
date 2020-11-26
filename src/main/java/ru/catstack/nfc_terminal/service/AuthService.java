package ru.catstack.nfc_terminal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.exception.ResourceAlreadyInUseException;
import ru.catstack.nfc_terminal.exception.UserLogOutException;
import ru.catstack.nfc_terminal.exception.UserLoginException;
import ru.catstack.nfc_terminal.model.Session;
import ru.catstack.nfc_terminal.model.User;
import ru.catstack.nfc_terminal.model.payload.request.LogOutRequest;
import ru.catstack.nfc_terminal.model.payload.request.LoginRequest;
import ru.catstack.nfc_terminal.model.payload.request.RegistrationRequest;
import ru.catstack.nfc_terminal.model.payload.response.JwtAuthResponse;
import ru.catstack.nfc_terminal.security.jwt.JwtTokenProvider;
import ru.catstack.nfc_terminal.security.jwt.JwtUser;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final SessionService sessionService;
    private final JwtTokenProvider tokenProvider;
    private final Random random = new Random();

    @Autowired
    public AuthService(UserService userService,
                       AuthenticationManager authenticationManager,
                       SessionService sessionService,
                       JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.sessionService = sessionService;
        this.tokenProvider = tokenProvider;
    }

    public Optional<User> registerUser(RegistrationRequest request) {
        var email = request.getEmail();
        var username = request.getUsername();
        var phone = request.getPhone();
        if (emailAlreadyExists(email))
            throw new ResourceAlreadyInUseException("Email", "address", email);
        if (usernameAlreadyExists(username))
            throw new ResourceAlreadyInUseException("Username", "value", username);
        if (phoneAlreadyExists(phone))
            throw new ResourceAlreadyInUseException("Phone", "value", phone);

        var registeredNewUser = userService.createUser(request);
        return Optional.ofNullable(registeredNewUser);
    }

    public boolean emailAlreadyExists(String email) {
        return userService.existsByEmail(email);
    }

    public boolean usernameAlreadyExists(String username) {
        return userService.existsByUsername(username);
    }

    public boolean phoneAlreadyExists(String phone) {
        return userService.existsByPhone(phone);
    }

    public JwtAuthResponse authenticateUser(LoginRequest loginRequest) {
        var uniqueKey = random.nextLong();

        var username = userService.findByEmail(loginRequest.getEmail()).map(User::getUsername).orElseThrow(() -> new UserLoginException(""));

        var auth = createAuthenticationOrThrow(username, loginRequest.getPassword());
        var principal = (JwtUser) auth.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(auth);

        var session = sessionService.createSession(auth, loginRequest, uniqueKey);
        userService.increaseLoginsCountById(principal.getId());
        var jwtToken = generateToken(principal, session);
        return new JwtAuthResponse(jwtToken, tokenProvider.getTokenPrefix());
    }

    private Authentication createAuthenticationOrThrow(String username, String password) {
        return Optional.ofNullable(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)))
                .orElseThrow(() -> new UserLoginException("Couldn't login user [" + username + "]"));
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

