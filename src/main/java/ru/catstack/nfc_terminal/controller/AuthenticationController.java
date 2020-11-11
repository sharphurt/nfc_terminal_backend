package ru.catstack.nfc_terminal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.nfc_terminal.exception.ObjectSavingException;
import ru.catstack.nfc_terminal.model.payload.request.LogOutRequest;
import ru.catstack.nfc_terminal.model.payload.request.LoginRequest;
import ru.catstack.nfc_terminal.model.payload.request.RegistrationRequest;
import ru.catstack.nfc_terminal.model.payload.response.ApiResponse;
import ru.catstack.nfc_terminal.service.AuthService;
import ru.catstack.nfc_terminal.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/auth/")
public class AuthenticationController {
    private final AuthService authService;
    private final UserService userService;
    // TODO: Вынести в отдельный контроллер методы для работы с юзером

    @Autowired
    public AuthenticationController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping("/checkEmail")
    public ApiResponse checkEmailInUse(@RequestParam("email") String email) {
        var emailExists = authService.emailAlreadyExists(email);
        return new ApiResponse(emailExists);
    }

    @GetMapping("/checkUsername")
    public ApiResponse checkUsernameInUse(@RequestParam("username") String username) {
        var usernameExists = authService.usernameAlreadyExists(username);
        return new ApiResponse(usernameExists);
    }

    @PostMapping("/login")
    public ApiResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        var response = authService.authenticateUser(loginRequest);
        return new ApiResponse(response);
    }

    @PostMapping("/register")
    public ApiResponse registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return authService.registerUser(registrationRequest)
                .map(user -> new ApiResponse("User registered successfully"))
                .orElseThrow(() -> new ObjectSavingException(registrationRequest.getUsername(), "Missing user object in database"));

    }

    @PostMapping("/logout")
    public ApiResponse logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
        authService.logoutUser(logOutRequest);
        return new ApiResponse("Log out successfully");
    }

    @GetMapping("/about")
    public ApiResponse getAboutMe() {
        var me = userService.getLoggedInUser();
        return new ApiResponse(me);
    }
}

