package ru.catstack.nfc_terminal.model.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.*;

@ApiModel(value = "Add company request", description = "The adding company request payload")
public class CreateCompanyRequest {
    @ApiModelProperty(value = "Company name", allowableValues = "NonEmpty String")
    @NotBlank(message = "Company name can't be blank")
    @NotNull(message = "Company name can't be null")
    private String name;

    @ApiModelProperty(value = "A valid INN", required = true)
    @Min(value = 1_000_000_000L, message = "INN length must be exactly 10 digits")
    @Max(value = 9_999_999_999L, message = "INN length must be exactly 10 digits")
    private long inn;

    @ApiModelProperty(value = "Tax system", allowableValues = "NonEmpty String")
    @Pattern(regexp = "ЕНВД|ЕСХН|СРП|УСН|ЕНВД-ЕСХН|УСН-ЕНВД", message = "Invalid tax system")
    @NotBlank(message = "Tax system can't be blank")
    @NotNull(message = "Tax system can't be null")
    private String taxSystem;

    @ApiModelProperty(value = "Company address", allowableValues = "NonEmpty String")
    @NotBlank(message = "Company address can't be blank")
    @NotNull(message = "Company address can't be null")
    private String address;

    @ApiModelProperty(value = "A valid KKT", required = true)
    @Min(value = 1_000_000_000_000_000L, message = "KKT length must be exactly 16 digits")
    @Max(value = 9_999_999_999_999_999L, message = "KKT length must be exactly 16 digits")
    private long kkt;

    public CreateCompanyRequest() {
    }

    public CreateCompanyRequest(String name, long inn, String taxSystem, String address, long kkt) {
        this.name = name;
        this.inn = inn;
        this.taxSystem = taxSystem;
        this.address = address;
        this.kkt = kkt;
    }

    public String getName() {
        return name;
    }

    public long getInn() {
        return inn;
    }

    public String getTaxSystem() {
        return taxSystem;
    }

    public String getAddress() {
        return address;
    }

    public long getKkt() {
        return kkt;
    }
}
