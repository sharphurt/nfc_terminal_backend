package ru.catstack.nfc_terminal.model.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import ru.catstack.nfc_terminal.model.DeviceInfo;

import javax.validation.Valid;
import javax.validation.constraints.*;

@ApiModel("Payment request")
public class CreatePaymentRequest {
    @ApiModelProperty(value = "Idempotence key")
    @NotNull(message = "Idempotence key can't be null")
    private long idempotenceKey;

    @ApiModelProperty(value = "inn")
    @NotNull(message = "INN number can't be null")
    private long inn;

    @ApiModelProperty(value = "Payer's card number")
    @Min(value = 1_000_000_000_000_000L, message = "CN length must be exactly 16 digits")
    @Max(value = 9_999_999_999_999_999L, message = "CN must be exactly 16 digits")
    private long payerCN;

    @ApiModelProperty(value = "Payment title")
    @NotNull(message = "Title can't be null")
    @Length(min = 1, max = 50, message = "Title length must be between 1 and 50")
    private String title;

    @ApiModelProperty(value = "Product's cost")
    @NotNull(message = "Product's cost can't be null")
    @DecimalMin(value = "0.0099", message = "Amount of money can't be less than 0.01₽")
    private float cost;

    @ApiModelProperty(value = "Amount of products")
    @Min(value = 1, message = "Amount can't be less than 1")
    private long amount;

    @Valid
    @NotNull(message = "Device info cannot be null")
    @ApiModelProperty(value = "Device info", required = true, dataType = "object", allowableValues = "A valid " +
            "deviceInfo object")
    private DeviceInfo deviceInfo;


    @Email(message = "Email is not valid")
    @ApiModelProperty(value = "A valid email")
    private String buyerEmail;

    public CreatePaymentRequest() {
    }

    public CreatePaymentRequest(long idempotenceKey, long inn, long payerCN, String title, long cost, @Min(value = 1, message = "Amount can't be less than 1") long amount, DeviceInfo deviceInfo, String buyerEmail) {
        this.idempotenceKey = idempotenceKey;
        this.inn = inn;
        this.payerCN = payerCN;
        this.title = title;
        this.cost = cost;
        this.amount = amount;
        this.deviceInfo = deviceInfo;
        this.buyerEmail = buyerEmail;
    }

    public long getInn() {
        return inn;
    }

    public long getPayerCN() {
        return payerCN;
    }

    public float getCost() {
        return cost;
    }

    public long getIdempotenceKey() {
        return idempotenceKey;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
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
