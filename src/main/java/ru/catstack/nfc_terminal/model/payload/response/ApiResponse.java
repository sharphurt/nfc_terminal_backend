package ru.catstack.nfc_terminal.model.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private ApiErrorResponse error;
    private Object response;
    private final Instant timestamp = Instant.now();

    public ApiResponse(ApiErrorResponse errorResponse) {
        this.error = errorResponse;
    }

    public ApiResponse(Object successResponse) {
        this.response = successResponse;
    }

    public ApiResponse(boolean b, String toString) {
    }

    public ApiErrorResponse getError() {
        return error;
    }

    public Object getResponse() {
        return response;
    }

    public String getTimestamp() {
        return timestamp.toString();
    }
}
