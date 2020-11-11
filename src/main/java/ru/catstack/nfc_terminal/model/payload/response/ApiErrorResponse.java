package ru.catstack.nfc_terminal.model.payload.response;

public class ApiErrorResponse {
    private final ErrorBody error;

    public ApiErrorResponse(String message, int code, String cause, String path) {
        this.error = new ErrorBody(message, code, cause, path);
    }

    public ErrorBody getError() {
        return error;
    }
}
