package com.medcalfbell.superadventure.exceptions;

public class InventoryActionNotFoundException extends RuntimeException {

    public InventoryActionNotFoundException() {
    }

    public InventoryActionNotFoundException(String message) {
        super(message);
    }

    public InventoryActionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
