package com.medcalfbell.superadventure.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.medcalfbell.superadventure.exceptions.ActionNotFoundException;
import java.util.stream.Stream;

public enum InventoryAction {

    GET("get"),
    TAKE("take"),
    PICK_UP("pick up"),
    STEAL("steal");

    private String identifier;

    InventoryAction(String identifier) {
        this.identifier = identifier;
    }

    public static InventoryAction fromIdentifier(String identifier) {
        return Stream.of(values())
                .filter(value -> value.identifier.equalsIgnoreCase(identifier))
                .findFirst()
                .orElseThrow(
                        () -> new ActionNotFoundException("No inventory action found for [" + identifier + "]"));
    }

    @JsonValue
    public String getIdentifier() {
        return identifier;
    }
}
