package ru.catstack.nfc_terminal.model.payload.response;

public class ErrorBody {
    private final String message;
    private final String errorCode;
    private final Integer httpCode;

    public ErrorBody(String message, int httpCode, String errorCode) {
        this.httpCode = httpCode;
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Integer getHttpCode() {
        return httpCode;
    }
}
