package ru.catstack.nfc_terminal.model.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private final Object response;
    private final Instant timestamp = Instant.now();

    public ApiResponse(Object response) {
        this.response = response;
    }

    public Object getResponse() {
        return response;
    }

    public String getTimestamp() {
        return timestamp.toString();
    }
}
