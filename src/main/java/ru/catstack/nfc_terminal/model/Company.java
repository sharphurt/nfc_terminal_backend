package ru.catstack.nfc_terminal.model;


import ru.catstack.nfc_terminal.model.audit.DateAudit;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "company")
public class Company extends DateAudit {
    @Id
    @Column(name = "inn")
    private long inn;

    @OneToMany(mappedBy = "company")
    private Set<Employee> employees;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "tax_system")
    private String taxSystem;

    @Column(name = "payments_address")
    private String address;

    @Column(name = "kkt")
    private long kkt;

    public Company() {
    }

    public Company(String companyName, long inn, String taxSystem, String address, long kkt) {
        this.companyName = companyName;
        this.inn = inn;
        this.taxSystem = taxSystem;
        this.address = address;
        this.kkt = kkt;
        this.employees = new HashSet<>();
    }

    public String getCompanyName() {
        return companyName;
    }

    public long getInn() {
        return inn;
    }

    public String getTaxSystem() {
        return taxSystem;
    }

    public String getAddress() {
        return address;
    }

    public long getKkt() {
        return kkt;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }
}
