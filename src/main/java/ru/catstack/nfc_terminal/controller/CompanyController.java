package ru.catstack.nfc_terminal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.nfc_terminal.exception.ObjectSavingException;
import ru.catstack.nfc_terminal.exception.ResourceNotFoundException;
import ru.catstack.nfc_terminal.model.payload.request.CreateCompanyRequest;
import ru.catstack.nfc_terminal.model.payload.response.ApiResponse;
import ru.catstack.nfc_terminal.service.CompanyService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/companies/")
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

    @PostMapping("/create")
    public ApiResponse createCompany(@Valid @RequestBody CreateCompanyRequest createCompanyRequest) {
        return companyService.createCompany(createCompanyRequest)
                .map(c -> new ApiResponse("Company registered successfully"))
                .orElseThrow(() -> new ObjectSavingException(createCompanyRequest.getName(), "The company was not successfully created"));
    }
}
