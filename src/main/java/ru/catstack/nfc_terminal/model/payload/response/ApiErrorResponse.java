package ru.catstack.nfc_terminal.model.payload.response;

public class ApiErrorResponse {
    private final String message;
    private final String cause;
    private final String path;
    private final Integer code;

    public ApiErrorResponse(String message, int code, String cause, String path){
        this.code = code;
        this.message = message;
        this.cause = cause;
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public String getCause() {
        return cause;
    }

    public String getPath() {
        return path;
    }

    public Integer getCode() {
        return code;
    }
}
