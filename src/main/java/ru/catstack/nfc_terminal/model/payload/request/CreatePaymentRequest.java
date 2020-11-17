package ru.catstack.nfc_terminal.model.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel("Payment request")
public class CreatePaymentRequest {
    @ApiModelProperty(value = "Transaction key")
    @NotNull(message = "Transaction key can't be null")
    private long transactionalKey;

    @ApiModelProperty(value = "inn")
    @NotNull(message = "INN number can't be null")
    private long inn;

    @ApiModelProperty(value = "Payer's card number")
    @Min(value = 1_000_000_000_000_000L, message = "CN length must be exactly 16 digits")
    @Max(value = 9_999_999_999_999_999L, message = "CN must be exactly 16 digits")
    private long payerCN;

    @ApiModelProperty(value = "Amount of money to pay")
    @NotNull(message = "Amount can't be null")
    @DecimalMin(value = "0.0099", message = "Amount of money can't be less than 0.01₽")
    private float amount;

    public CreatePaymentRequest() {
    }

    public CreatePaymentRequest(long transactionalKey, long inn, long payerCN, long amount) {
        this.transactionalKey = transactionalKey;
        this.inn = inn;
        this.payerCN = payerCN;
        this.amount = amount;
    }

    public long getInn() {
        return inn;
    }

    public long getPayerCN() {
        return payerCN;
    }

    public float getAmount() {
        return amount;
    }

    public long getTransactionalKey() {
        return transactionalKey;
    }
}
