package ru.catstack.nfc_terminal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class UserLogOutException extends RuntimeException {

    public UserLogOutException(String user, String message) {
        super(String.format("Failed to close session for device id %s: '%s'", user, message));
    }
}