package ru.catstack.nfc_terminal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.catstack.nfc_terminal.model.audit.DateAudit;

import javax.persistence.*;

@Entity
@Table(name = "receipt")
public class Receipt extends DateAudit {
    @Id
    @Column(name = "receipt_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inn", referencedColumnName = "inn")
    private Company company;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_cost")
    private float productCost;

    @Column(name = "product_amount")
    private int productAmount;

    @Column(name = "total")
    private float total;

    @Column(name = "buyer_email")
    private String buyerEmail;

    @Column(name = "ficsal_accum_number")
    private long fiscalAccumulator;

    @Column(name = "ficsal_sign")
    private long fiscalSign;

    public Receipt() {
    }

    public Receipt(Session session, Company company, String productName, float productCost, int productAmount, float total, String buyerEmail, long fiscalAccumulator, long fiscalSign) {
        this.session = session;
        this.company = company;
        this.productName = productName;
        this.productCost = productCost;
        this.productAmount = productAmount;
        this.total = total;
        this.buyerEmail = buyerEmail;
        this.fiscalAccumulator = fiscalAccumulator;
        this.fiscalSign = fiscalSign;
    }

    public long getId() {
        return id;
    }

    public Session getSession() {
        return session;
    }

    public Company getCompany() {
        return company;
    }

    public String getProductName() {
        return productName;
    }

    public float getProductCost() {
        return productCost;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public float getTotal() {
        return total;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public long getFiscalAccumulator() {
        return fiscalAccumulator;
    }

    public long getFiscalSign() {
        return fiscalSign;
    }
}
