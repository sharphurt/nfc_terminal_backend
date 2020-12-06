package ru.catstack.nfc_terminal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.model.Bill;
import ru.catstack.nfc_terminal.model.Company;
import ru.catstack.nfc_terminal.model.payload.request.CompanyRequestBody;
import ru.catstack.nfc_terminal.repository.CompanyRepository;

import java.util.Optional;
import java.util.Random;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final BillService billService;
    private final Random random = new Random();

    @Autowired
    public CompanyService(CompanyRepository companyRepository, BillService billService) {
        this.companyRepository = companyRepository;
        this.billService = billService;
    }

    public Company createCompany(CompanyRequestBody request) {
        var bill = billService.save(new Bill());
        var company = new Company(request.getName(), request.getInn(), request.getTaxSystem(), request.getAddress(), request.getKkt(), Math.abs(random.nextLong()), Math.abs(random.nextLong()), bill);
        save(company);

        return findByInn(company.getInn()).get();
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
