package ru.catstack.nfc_terminal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.catstack.nfc_terminal.model.payload.response.ApiResponse;
import ru.catstack.nfc_terminal.service.AuthService;
import ru.catstack.nfc_terminal.service.CompanyService;

@RestController
@RequestMapping("/api/check/")
public class CheckController {

    private final AuthService authService;
    private final CompanyService companyService;

    @Autowired
    public CheckController(AuthService authService, CompanyService companyService) {
        this.authService = authService;
        this.companyService = companyService;
    }


    @GetMapping("/email")
    public ApiResponse checkEmailInUse(@RequestParam("email") String email) {
        var emailExists = authService.emailAlreadyExists(email);
        return new ApiResponse(emailExists);
    }

    @GetMapping("/phone")
    public ApiResponse checkPhoneInUse(@RequestParam("phone") String phone) {
        var phoneExists = authService.phoneAlreadyExists(phone);
        return new ApiResponse(phoneExists);
    }

    @GetMapping("/inn")
    public ApiResponse checkInnInUse(@RequestParam("inn") long inn) {
        var phoneExists = companyService.existsByInn(inn);
        return new ApiResponse(phoneExists);
    }

}
