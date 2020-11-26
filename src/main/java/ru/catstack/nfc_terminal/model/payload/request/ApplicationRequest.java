package ru.catstack.nfc_terminal.model.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.*;

@ApiModel(value = "Application request")
public class ApplicationRequest {
    @ApiModelProperty(value = "Company name", allowableValues = "NonEmpty String")
    @NotBlank(message = "Company name can't be blank")
    @NotNull(message = "Company name can't be null")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email is not valid")
    @ApiModelProperty(value = "A valid email", required = true, allowableValues = "NonEmpty String")
    private String email;

    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = "\\+7[\\d]{10}", message = "Invalid phone value")
    @ApiModelProperty(value = "A valid phone string", required = true, allowableValues = "NonEmpty String")
    private String phone;

    @ApiModelProperty(value = "A valid INN", required = true)
    @Min(value = 1_000_000_000L, message = "INN length must be exactly 10 digits")
    @Max(value = 9_999_999_999L, message = "INN length must be exactly 10 digits")
    private long inn;

    public ApplicationRequest() {
    }

    public ApplicationRequest(String name, String email, String phone, long inn) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.inn = inn;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public long getInn() {
        return inn;
    }
}
