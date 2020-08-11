package com.medcalfbell.superadventure.exceptions;

public class DuplicateDirectionLocationException extends RuntimeException {

    public DuplicateDirectionLocationException() {
    }

    public DuplicateDirectionLocationException(String message) {
        super(message);
    }

    public DuplicateDirectionLocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
