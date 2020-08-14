package com.medcalfbell.superadventure.exceptions;

public class DirectionNotFoundException extends RuntimeException {

    public DirectionNotFoundException() {
        super("You can't go that way.");
    }

    public DirectionNotFoundException(String message) {
        super(message);
    }

    public DirectionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
