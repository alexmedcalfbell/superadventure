package com.medcalfbell.superadventure.exceptions;

public class DuplicateTargetException extends RuntimeException {

    public DuplicateTargetException() {
    }

    public DuplicateTargetException(String message) {
        super(message);
    }

    public DuplicateTargetException(String message, Throwable cause) {
        super(message, cause);
    }
}
