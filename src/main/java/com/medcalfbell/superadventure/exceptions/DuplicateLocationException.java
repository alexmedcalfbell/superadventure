package com.medcalfbell.superadventure.exceptions;

public class DuplicateLocationException extends RuntimeException {

    public DuplicateLocationException() {
    }

    public DuplicateLocationException(String message) {
        super(message);
    }

    public DuplicateLocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
