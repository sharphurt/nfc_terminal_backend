package ru.catstack.nfc_terminal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.exception.ResourceAlreadyInUseException;
import ru.catstack.nfc_terminal.exception.UserLogOutException;
import ru.catstack.nfc_terminal.model.Session;
import ru.catstack.nfc_terminal.model.User;
import ru.catstack.nfc_terminal.model.payload.request.LogOutRequest;
import ru.catstack.nfc_terminal.model.payload.request.LoginRequest;
import ru.catstack.nfc_terminal.model.payload.request.RegistrationRequest;
import ru.catstack.nfc_terminal.security.jwt.JwtTokenProvider;
import ru.catstack.nfc_terminal.security.jwt.JwtUser;

import java.util.Optional;

@Service
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final SessionService sessionService;
    private final JwtTokenProvider tokenProvider;

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
        if (emailAlreadyExists(email))
            throw new ResourceAlreadyInUseException("Email", "address", email);
        if (usernameAlreadyExists(username))
            throw new ResourceAlreadyInUseException("Username", "value", username);

        var registeredNewUser = userService.save(userService.createUser(request));
        return Optional.ofNullable(registeredNewUser);
    }

    public boolean emailAlreadyExists(String email) {
        return userService.existsByEmail(email);
    }

    public boolean usernameAlreadyExists(String username) {
        return userService.existsByUsername(username);
    }

    public Optional<Authentication> authenticateUser(LoginRequest loginRequest) {
        return Optional.ofNullable(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())));
    }

    public String generateToken(JwtUser user) {
        return tokenProvider.createToken(user);
    }

    public void createSession(Authentication auth, LoginRequest loginRequest) {
        var user = (JwtUser) auth.getPrincipal();
        if (sessionService.isDeviceAlreadyExists(loginRequest.getDeviceInfo())) {
            var session = sessionService.findByUserIdAndDeviceId(user.getId(), loginRequest.getDeviceInfo().getDeviceId());
            session.ifPresent(sess -> sessionService.deleteBySessionId(sess.getId()));
        }
        var newSession = new Session(user.getId(), loginRequest.getDeviceInfo());
        sessionService.save(newSession);
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

