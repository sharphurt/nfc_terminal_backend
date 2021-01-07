package ru.catstack.nfc_terminal.model;

import ru.catstack.nfc_terminal.model.enums.PaymentStatus;

import java.time.Instant;

public class HistoryRecord {
    private long paymentId;

    private PaymentStatus status;

    private String title;

    private float cost;

    private String cardLogoCode;

    private Instant datetime;

    public HistoryRecord() {
    }

    public HistoryRecord(long paymentId, PaymentStatus status, String title, float cost, String cardLogoCode, Instant datetime) {
        this.paymentId = paymentId;
        this.status = status;
        this.title = title;
        this.cost = cost;
        this.cardLogoCode = cardLogoCode;
        this.datetime = datetime;
    }

    public String getTitle() {
        return title;
    }

    public float getCost() {
        return cost;
    }

    public String getCardLogoCode() {
        return cardLogoCode;
    }

    public Instant getDatetime() {
        return datetime;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public PaymentStatus getStatus() {
        return status;
    }
}
