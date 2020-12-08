package ru.catstack.nfc_terminal.model.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel(value = "Registration Request", description = "The registration request payload")
public class ClientCompanyRegistrationRequest {
    @Valid
    @NotNull(message = "Client object can't be null")
    @ApiModelProperty(value = "Application to be removed", allowableValues = "Long integer number")
    private ClientRequestBody client;

    @Valid
    @NotNull(message = "Company object can't be null")
    @ApiModelProperty(value = "Application to be removed", allowableValues = "Long integer number")
    private CompanyRequestBody company;

    @Valid
    @Min(value = 1, message = "Application id can't be less than 1")
    @ApiModelProperty(value = "Application to be removed", allowableValues = "Long integer number")
    private long applicationToRemoveId;

    public ClientCompanyRegistrationRequest(ClientRequestBody client, CompanyRequestBody company, @NotNull(message = "Application id can't be null") long applicationToRemoveId) {
        this.client = client;
        this.company = company;
        this.applicationToRemoveId = applicationToRemoveId;
    }

    public ClientCompanyRegistrationRequest() {
    }

    public ClientRequestBody getClient() {
        return client;
    }

    public CompanyRequestBody getCompany() {
        return company;
    }

    public long getApplicationToRemoveId() {
        return applicationToRemoveId;
    }
}
