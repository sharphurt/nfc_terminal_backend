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
    @Column(name = "payment_id")
    private long paymentId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inn", referencedColumnName = "inn")
    private Company company;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User vendor;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_cost")
    private float productCost;

    @Column(name = "product_amount")
    private long productAmount;

    @Column(name = "total")
    private float total;

    @Column(name = "buyer_email")
    private String buyerEmail;

    public Receipt() {
    }

    public Receipt(long paymentId, Session session, Company company, User vendor, String productName, float productCost, long productAmount, float total, String buyerEmail) {
        this.paymentId = paymentId;
        this.session = session;
        this.company = company;
        this.vendor = vendor;
        this.productName = productName;
        this.productCost = productCost;
        this.productAmount = productAmount;
        this.total = total;
        this.buyerEmail = buyerEmail;
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

    public long getProductAmount() {
        return productAmount;
    }

    public float getTotal() {
        return total;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public User getVendor() {
        return vendor;
    }

    public long getPaymentId() {
        return paymentId;
    }
}
