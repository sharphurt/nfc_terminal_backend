package ru.catstack.nfc_terminal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.model.Company;
import ru.catstack.nfc_terminal.model.Employee;
import ru.catstack.nfc_terminal.model.User;
import ru.catstack.nfc_terminal.repository.EmployeeRepository;

import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Optional<Employee> createEmployee(User user, Company company) {
        var employee = new Employee(user, company);
        save(employee);
        return findByUserAndCompany(user, company);
    }

    public void save(Employee employee) {
        employeeRepository.save(employee);
    }

    Optional<Employee> findByUserAndCompany(User user, Company company){
        return employeeRepository.findByUserAndCompany(user, company);
    }


}
