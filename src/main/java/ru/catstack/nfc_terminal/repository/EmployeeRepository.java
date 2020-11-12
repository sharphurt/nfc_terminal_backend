package ru.catstack.nfc_terminal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.catstack.nfc_terminal.model.Company;
import ru.catstack.nfc_terminal.model.Employee;
import ru.catstack.nfc_terminal.model.User;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByCompany(Company company);
    List<Employee> findAllByUser(User user);

    Optional<Employee> findByUserAndCompany(User user, Company company);
}
