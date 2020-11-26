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
    private long idempotenceKey;

    @Column(name = "title")
    private String title;

    @Column(name = "payer_CN")
    private long payerCardNumber;

    @Column(name = "vendor_id")
    private long vendorId;

    @Column(name = "cost")
    private float cost;

    @Column(name = "amount")
    private long amount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "buyer_email")
    private String buyerEmail;

    public Payment() {
    }

    public Payment(long idempotenceKey, String title, long payerCardNumber, long vendorId, float cost, long amount, String deviceId, String buyerEmail) {
        this.idempotenceKey = idempotenceKey;
        this.title = title;
        this.payerCardNumber = payerCardNumber;
        this.vendorId = vendorId;
        this.cost = cost;
        this.amount = amount;
        this.deviceId = deviceId;
        this.buyerEmail = buyerEmail;
        this.status = PaymentStatus.WAITING;
    }

    public long getId() {
        return id;
    }

    public long getIdempotenceKey() {
        return idempotenceKey;
    }

    public long getPayerCardNumber() {
        return payerCardNumber;
    }

    public long getVendorId() {
        return vendorId;
    }

    public float getCost() {
        return cost;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public String getTitle() {
        return title;
    }

    public long getAmount() {
        return amount;
    }
}
