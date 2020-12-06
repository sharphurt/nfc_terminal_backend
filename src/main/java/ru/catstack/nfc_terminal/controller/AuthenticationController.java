package ru.catstack.nfc_terminal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.nfc_terminal.exception.ObjectSavingException;
import ru.catstack.nfc_terminal.model.payload.request.AdminRegistrationRequest;
import ru.catstack.nfc_terminal.model.payload.request.ClientCompanyRegistrationRequest;
import ru.catstack.nfc_terminal.model.payload.request.LogOutRequest;
import ru.catstack.nfc_terminal.model.payload.request.LoginRequest;
import ru.catstack.nfc_terminal.model.payload.response.ApiResponse;
import ru.catstack.nfc_terminal.service.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth/")
public class AuthenticationController {
    private final AuthService authService;

    @Autowired
    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        var response = authService.authenticateUser(loginRequest);
        return new ApiResponse(response);
    }

    @PostMapping("/admin/register")
    public ApiResponse registerAdmin(@Valid @RequestBody AdminRegistrationRequest registrationRequest) {
        return authService.registerAdmin(registrationRequest)
                .map(user -> new ApiResponse("Admin registered successfully"))
                .orElseThrow(() -> new ObjectSavingException(registrationRequest.getEmail(), "Registration not completed"));
    }

    @PostMapping("/register")
    public ApiResponse registerClientAndCompany(@Valid @RequestBody ClientCompanyRegistrationRequest registrationRequest) {
        return authService.registerClientCompany(registrationRequest)
                .map(user -> new ApiResponse("Client and company registered successfully"))
                .orElseThrow(() -> new ObjectSavingException(registrationRequest.getClient().getEmail(), "Registration not completed"));
    }

    @PostMapping("/logout")
    public ApiResponse logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
        authService.logoutUser(logOutRequest);
        return new ApiResponse("Log out successfully");
    }
}

