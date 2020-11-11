package ru.catstack.nfc_terminal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class ObjectSavingException extends RuntimeException {

    public ObjectSavingException(String objectName, String message) {
        super(String.format("Failed to register User %s: '%s'", objectName, message));
    }

}