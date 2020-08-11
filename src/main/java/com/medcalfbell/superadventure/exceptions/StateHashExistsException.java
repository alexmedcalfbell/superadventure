package com.medcalfbell.superadventure.exceptions;

public class StateHashExistsException extends RuntimeException {

    public StateHashExistsException() {
    }

    public StateHashExistsException(String message) {
        super(message);
    }

    public StateHashExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
