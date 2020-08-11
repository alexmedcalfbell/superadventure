package com.medcalfbell.superadventure.exceptions;

public class DirectionNotFoundException extends RuntimeException {

    public DirectionNotFoundException() {
    }

    public DirectionNotFoundException(String message) {
        super(message);
    }

    public DirectionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
