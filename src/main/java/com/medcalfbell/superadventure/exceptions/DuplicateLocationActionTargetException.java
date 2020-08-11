package com.medcalfbell.superadventure.exceptions;

public class DuplicateLocationActionTargetException extends RuntimeException {

    public DuplicateLocationActionTargetException() {
    }

    public DuplicateLocationActionTargetException(String message) {
        super(message);
    }

    public DuplicateLocationActionTargetException(String message, Throwable cause) {
        super(message, cause);
    }
}
