package ru.catstack.nfc_terminal.model.payload.request;

import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotNull;

@ApiModel(value = "Registration Request", description = "The registration request payload")
public class ClientCompanyRegistrationRequest {
    @NotNull(message = "Client object can't be null")
    private ClientRequestBody client;

    @NotNull(message = "Company object can't be null")
    private CompanyRequestBody company;

    @NotNull(message = "Application id can't be null")
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
