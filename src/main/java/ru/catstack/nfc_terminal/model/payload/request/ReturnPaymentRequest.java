package ru.catstack.nfc_terminal.model.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.catstack.nfc_terminal.model.DeviceInfo;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel("Payment request")
public class ReturnPaymentRequest {
    @ApiModelProperty(value = "paymentId")
    @NotNull(message = "payment id can't be null")
    private long paymentId;

    @ApiModelProperty(value = "inn")
    @NotNull(message = "INN number can't be null")
    private long inn;

    @ApiModelProperty(value = "Payer's card number")
    @Min(value = 1_000_000_000_000_000L, message = "CN length must be exactly 16 digits")
    @Max(value = 9_999_999_999_999_999L, message = "CN must be exactly 16 digits")
    private long payerCN;

    @Valid
    @NotNull(message = "Device info cannot be null")
    @ApiModelProperty(value = "Device info", required = true, dataType = "object", allowableValues = "A valid " +
            "deviceInfo object")
    private DeviceInfo deviceInfo;

    public ReturnPaymentRequest() {
    }

    public ReturnPaymentRequest(long paymentId, long inn, long payerCN, DeviceInfo deviceInfo) {
        this.paymentId = paymentId;
        this.inn = inn;
        this.payerCN = payerCN;
        this.deviceInfo = deviceInfo;
    }

    public long getInn() {
        return inn;
    }

    public long getPayerCN() {
        return payerCN;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public long getPaymentId() {
        return paymentId;
    }
}
