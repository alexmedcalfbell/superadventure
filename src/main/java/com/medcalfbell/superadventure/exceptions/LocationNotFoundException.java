package com.medcalfbell.superadventure.exceptions;

public class LocationNotFoundException extends RuntimeException {

    public LocationNotFoundException() {
    }

    public LocationNotFoundException(String message) {
        super(message);
    }

    public LocationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
