package ru.catstack.nfc_terminal.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.catstack.nfc_terminal.model.audit.DateAudit;
import ru.catstack.nfc_terminal.model.enums.UserPrivilege;
import ru.catstack.nfc_terminal.model.enums.UserStatus;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends DateAudit {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus userStatus;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Employee> employees;

    @Column(name = "logins_count")
    private long loginsCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_privilege")
    private UserPrivilege userPrivilege;

    public User() {
    }

    public User(String email, String password, String firstName, String lastName, String patronymic, String phone, UserPrivilege userPrivilege) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.userPrivilege = userPrivilege;
        this.userStatus = UserStatus.ACTIVE;
        this.phone = phone;
        this.employees = new HashSet<>();
        this.loginsCount = 0;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public Set<Employee> getRegistrations() {
        return employees;
    }

    public String getPhone() {
        return phone;
    }

    public long getLoginsCount() {
        return loginsCount;
    }

    public UserPrivilege getUserPrivilege() {
        return userPrivilege;
    }
}
