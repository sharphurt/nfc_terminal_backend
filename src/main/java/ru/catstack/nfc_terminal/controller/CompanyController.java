package ru.catstack.nfc_terminal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.nfc_terminal.exception.ObjectSavingException;
import ru.catstack.nfc_terminal.exception.ResourceNotFoundException;
import ru.catstack.nfc_terminal.model.payload.request.CreateCompanyRequest;
import ru.catstack.nfc_terminal.model.payload.response.ApiResponse;
import ru.catstack.nfc_terminal.service.CompanyService;
import ru.catstack.nfc_terminal.service.EmployeeService;
import ru.catstack.nfc_terminal.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/companies/")
public class CompanyController {
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final UserService userService;

    @Autowired
    public CompanyController(CompanyService companyService, EmployeeService employeeService, UserService userService) {
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.userService = userService;
    }

    @GetMapping("{inn}")
    public ApiResponse getByInn(@PathVariable("inn") long inn) {
        return companyService.findByInn(inn)
                .map(ApiResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "INN", inn));
    }

    @PostMapping("/create")
    public ApiResponse createCompany(@Valid @RequestBody CreateCompanyRequest createCompanyRequest) {
        var company = companyService.createCompany(createCompanyRequest)
                .orElseThrow(() -> new ObjectSavingException("Company", "Unexpected database error"));
        var me = userService.getLoggedInUser();
        employeeService.createEmployee(me, company)
                .orElseThrow(() -> new ObjectSavingException("Employee", "Unexpected database error"));

        return new ApiResponse("Company was created successfully with logged-in employee");
    }
}
