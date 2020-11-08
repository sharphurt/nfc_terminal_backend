package ru.catstack.nfc_terminal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.catstack.nfc_terminal.exception.UserLoginException;
import ru.catstack.nfc_terminal.exception.UserRegistrationException;
import ru.catstack.nfc_terminal.model.payload.request.LogOutRequest;
import ru.catstack.nfc_terminal.model.payload.request.LoginRequest;
import ru.catstack.nfc_terminal.model.payload.request.RegistrationRequest;
import ru.catstack.nfc_terminal.model.payload.response.ApiResponse;
import ru.catstack.nfc_terminal.model.payload.response.JwtAuthResponse;
import ru.catstack.nfc_terminal.security.jwt.JwtTokenProvider;
import ru.catstack.nfc_terminal.security.jwt.JwtUser;
import ru.catstack.nfc_terminal.service.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/auth/")
public class AuthenticationController {
    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public AuthenticationController(AuthService authService, JwtTokenProvider tokenProvider) {
        this.authService = authService;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping("/checkEmailInUse")
    public ApiResponse checkEmailInUse(@RequestParam("email") String email) {
        var emailExists = authService.emailAlreadyExists(email);
        return new ApiResponse(emailExists);
    }

    @GetMapping("/checkUsernameInUse")
    public ApiResponse checkUsernameInUse(@RequestParam("username") String username) {
        var usernameExists = authService.usernameAlreadyExists(username);
        return new ApiResponse(usernameExists);
    }

    @PostMapping("/login")
    public ApiResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        var auth = authService
                .authenticateUser(loginRequest)
                .orElseThrow(() -> new UserLoginException("Couldn't login user [" + loginRequest + "]"));

        var principal = (JwtUser) auth.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(auth);

        authService.createSession(auth, loginRequest);
        var jwtToken = authService.generateToken(principal);
        var response = new JwtAuthResponse(jwtToken, tokenProvider.getTokenPrefix());
        return new ApiResponse(response);
    }

    @PostMapping("/register")
    public ApiResponse registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return authService.registerUser(registrationRequest)
                .map(user -> new ApiResponse("User registered successfully"))
                .orElseThrow(() -> new UserRegistrationException(registrationRequest.getEmail(), "Missing user object in database"));

    }

    @PostMapping("/logout")
    public ApiResponse logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
        authService.logoutUser(logOutRequest);
        return new ApiResponse("Log out successfully");
    }
}

