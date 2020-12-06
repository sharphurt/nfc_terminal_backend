package ru.catstack.nfc_terminal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.catstack.nfc_terminal.exception.ResourceNotFoundException;
import ru.catstack.nfc_terminal.model.payload.response.ApiResponse;
import ru.catstack.nfc_terminal.service.CompanyService;

@RestController
@RequestMapping("/api/companies/")
public class CompanyController {
    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("{inn}")
    public ApiResponse getByInn(@PathVariable("inn") long inn) {
        return companyService.findByInn(inn)
                .map(ApiResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "INN", inn));
    }
}
