package ru.catstack.nfc_terminal.model.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "Amount of money to pay")
    @NotNull(message = "Amount can't be null")
    @DecimalMin(value = "0.0099", message = "Amount of money can't be less than 0.01₽")
    private float amount;

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

    public CreatePaymentRequest(long idempotenceKey, long inn, long payerCN, long amount, @Valid @NotNull(message = "Device info cannot be null") DeviceInfo deviceInfo, @NotBlank(message = "Email cannot be blank") @Email(message = "Email is not valid") String buyerEmail) {
        this.idempotenceKey = idempotenceKey;
        this.inn = inn;
        this.payerCN = payerCN;
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

    public float getAmount() {
        return amount;
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
}
