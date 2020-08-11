package com.medcalfbell.superadventure.exceptions;

public class ActionNotFoundException extends RuntimeException {

    public ActionNotFoundException() {
    }

    public ActionNotFoundException(String message) {
        super(message);
    }

    public ActionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
