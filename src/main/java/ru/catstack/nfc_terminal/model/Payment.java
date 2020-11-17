package ru.catstack.nfc_terminal.model;

import ru.catstack.nfc_terminal.model.audit.DateAudit;
import ru.catstack.nfc_terminal.model.enums.PaymentStatus;

import javax.persistence.*;

@Entity
@Table(name = "payment")
public class Payment extends DateAudit {
    @Id
    @Column(name = "payment_id")
    private long id;

    @Column(name = "transactional_key")
    private long transactionalKey;

    @Column(name = "payer_CN")
    private long payerCardNumber;

    @Column(name = "vendor_id")
    private long vendorId;

    @Column(name = "amount")
    private float amount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public Payment() {
    }

    public Payment(long transactionalKey, long payerCardNumber, long vendorId, float amount) {
        this.transactionalKey = transactionalKey;
        this.payerCardNumber = payerCardNumber;
        this.vendorId = vendorId;
        this.amount = amount;
        this.status = PaymentStatus.WAITING;
    }

    public long getId() {
        return id;
    }

    public long getTransactionalKey() {
        return transactionalKey;
    }

    public long getPayerCardNumber() {
        return payerCardNumber;
    }

    public long getVendorId() {
        return vendorId;
    }

    public float getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
