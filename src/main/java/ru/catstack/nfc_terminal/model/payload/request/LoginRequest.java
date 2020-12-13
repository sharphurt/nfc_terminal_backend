package ru.catstack.nfc_terminal.model.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ru.catstack.nfc_terminal.model.DeviceInfo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@ApiModel(value = "Login Request", description = "The login request payload")
public class LoginRequest {
    @ApiModelProperty(value = "Registered username", allowableValues = "NonEmpty String")
    private String email;

    @ApiModelProperty(value = "Valid user password", allowableValues = "NonEmpty String")
    private String password;

    @Valid
    @NotNull(message = "Device info cannot be null")
    @ApiModelProperty(value = "Device info", required = true, dataType = "object", allowableValues = "A valid " +
            "deviceInfo object")
    private DeviceInfo deviceInfo;

    public LoginRequest(String email, String password, DeviceInfo deviceInfo) {
        this.email = email;
        this.password = password;
        this.deviceInfo = deviceInfo;
    }

    public LoginRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }
}
