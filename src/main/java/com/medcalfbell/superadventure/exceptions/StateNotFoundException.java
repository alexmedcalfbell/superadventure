package com.medcalfbell.superadventure.exceptions;

public class StateNotFoundException extends RuntimeException {

    public StateNotFoundException() {
    }

    public StateNotFoundException(String message) {
        super(message);
    }

    public StateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
