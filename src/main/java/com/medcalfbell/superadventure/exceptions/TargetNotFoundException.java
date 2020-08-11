package com.medcalfbell.superadventure.exceptions;

public class TargetNotFoundException extends RuntimeException {

    public TargetNotFoundException() {
    }

    public TargetNotFoundException(String message) {
        super(message);
    }

    public TargetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
