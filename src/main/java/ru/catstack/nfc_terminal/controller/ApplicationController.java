package ru.catstack.nfc_terminal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.nfc_terminal.model.payload.request.ApplicationRequest;
import ru.catstack.nfc_terminal.model.payload.response.ApiResponse;
import ru.catstack.nfc_terminal.service.ApplicationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/applications/")
public class ApplicationController {
    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/send")
    public ApiResponse sendApplication(@Valid @RequestBody ApplicationRequest applicationRequest) {
        applicationService.createApplication(applicationRequest);
        return new ApiResponse("Application saved successfully");
    }
}

