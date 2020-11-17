package ru.catstack.nfc_terminal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.catstack.nfc_terminal.model.audit.DateAudit;

import javax.persistence.*;

@Entity
public class Employee extends DateAudit {
    @JsonIgnore
    @Id
    @Column(name = "id")
    long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "inn")
    Company company;

    public Employee() {
    }

    public Employee(User user, Company company) {
        this.user = user;
        this.company = company;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Company getCompany() {
        return company;
    }
}
