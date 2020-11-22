package ru.catstack.nfc_terminal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.nfc_terminal.model.payload.response.ApiResponse;
import ru.catstack.nfc_terminal.service.EmailService;

@RestController
@RequestMapping("/api/email/")
public class EmailController {
    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/send")
    public ApiResponse checkEmailInUse(@RequestParam("val") long val) {
        emailService.sendMail("pashaluk31@yandex.ru", "message from Павлик", val);
        return new ApiResponse("success");
    }

}
