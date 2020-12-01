package ru.catstack.nfc_terminal.model;

import ru.catstack.nfc_terminal.model.audit.DateAudit;
import ru.catstack.nfc_terminal.model.enums.ApplicationStatus;

import javax.persistence.*;

@Entity
@Table(name = "application")
public class Application extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "inn")
    private long inn;

    @Column(name = "status")
    private ApplicationStatus status;

    public Application() {
    }

    public Application(String name, String phone, String email, long inn) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.inn = inn;
        this.status = ApplicationStatus.NOT_CONSIDERED;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public long getInn() {
        return inn;
    }
}

