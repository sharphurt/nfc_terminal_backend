package ru.catstack.nfc_terminal.model;

import java.time.Instant;

public class HistoryRecord {
    private String title;

    private float cost;

    private String cardLogoCode;

    private Instant datetime;

    public HistoryRecord() {
    }

    public HistoryRecord(String title, float cost, String cardLogoCode, Instant datetime) {
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


}
