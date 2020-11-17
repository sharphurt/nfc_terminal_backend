package ru.catstack.nfc_terminal.model;

import ru.catstack.nfc_terminal.model.audit.DateAudit;

import javax.persistence.*;

@Entity
@Table(name = "bill")
public class Bill extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private long id;

    @Column(name = "balance")
    private float balance;

    public Bill() {
    }

    public Bill(long startBalance) {
        this.balance = startBalance;
    }

    public long getId() {
        return id;
    }

    public float getBalance() {
        return balance;
    }

    public void addToBalance(long amount) {
        this.balance += amount;
    }
}

