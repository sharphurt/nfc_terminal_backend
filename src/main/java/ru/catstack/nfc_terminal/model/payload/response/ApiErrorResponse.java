package ru.catstack.nfc_terminal.model.payload.response;

public class ApiErrorResponse {
    private final ErrorBody error;

    public ApiErrorResponse(String message, int httpCode, String errorCode) {
        this.error = new ErrorBody(message, httpCode, errorCode);
    }

    public ErrorBody getError() {
        return error;
    }
}
