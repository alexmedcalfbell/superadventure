package com.medcalfbell.superadventure.exceptions;

public class DuplicateActionException extends RuntimeException {

    public DuplicateActionException() {
    }

    public DuplicateActionException(String message) {
        super(message);
    }

    public DuplicateActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
