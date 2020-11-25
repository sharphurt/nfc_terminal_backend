package ru.catstack.nfc_terminal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.exception.ResourceAlreadyInUseException;
import ru.catstack.nfc_terminal.model.Bill;
import ru.catstack.nfc_terminal.model.Company;
import ru.catstack.nfc_terminal.model.payload.request.CreateCompanyRequest;
import ru.catstack.nfc_terminal.repository.CompanyRepository;

import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final BillService billService;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, BillService billService) {
        this.companyRepository = companyRepository;
        this.billService = billService;
    }

    public Optional<Company> createCompany(CreateCompanyRequest request) {
        if (existsByInn(request.getInn()))
            throw new ResourceAlreadyInUseException("INN", "value", request.getInn());
        if (existsByKkt(request.getKkt()))
            throw new ResourceAlreadyInUseException("KKT", "value", request.getKkt());
        if (existsByName(request.getName()))
            throw new ResourceAlreadyInUseException("Company name", "value", request.getName());

        var bill = billService.save(new Bill());
        var company = new Company(request.getName(), request.getInn(), request.getTaxSystem(), request.getAddress(), request.getKkt(), bill);
        save(company);

        return findByInn(company.getInn());
    }

    public void addToBalance(Company company, float amount) {
        companyRepository.updateBalanceById(company.getBill().getId(), company.getBill().getBalance() + amount);
    }

    public Company save(Company company) {
        return companyRepository.save(company);
    }

    public boolean existsByInn(long inn) {
        return companyRepository.existsByInn(inn);
    }

    public boolean existsByKkt(long kkt) {
        return companyRepository.existsByKkt(kkt);
    }

    public boolean existsByName(String name) {
        return companyRepository.existsByCompanyName(name);
    }

    public Optional<Company> findByInn(long inn) {
        return companyRepository.findByInn(inn);
    }
}
