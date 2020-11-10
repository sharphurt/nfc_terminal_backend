package ru.catstack.nfc_terminal.model;

import ru.catstack.nfc_terminal.model.audit.DateAudit;

import javax.persistence.*;

@Entity
public class Employee extends DateAudit {
    @Id
    long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "inn")
    Company company;

    @Column(name = "appointment")
    String appointment;

    public Employee() {
    }

    public Employee(long id, User user, Company company, String appointment) {
        this.id = id;
        this.user = user;
        this.company = company;
        this.appointment = appointment;
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

    public String getAppointment() {
        return appointment;
    }
}
