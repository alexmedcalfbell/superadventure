package com.medcalfbell.superadventure.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.medcalfbell.superadventure.exceptions.ActionNotFoundException;
import java.util.stream.Stream;

public enum MovementAction {

    GO("go"),
    RUN("run"),
    CLIMB("climb"),
    CROSS("cross");


    private String identifier;

    MovementAction(String identifier) {
        this.identifier = identifier;
    }

    public static MovementAction fromIdentifier(String identifier) {
        return Stream.of(values())
                .filter(value -> value.identifier.equalsIgnoreCase(identifier))
                .findFirst()
                .orElseThrow(
                        () -> new ActionNotFoundException("No action found for [" + identifier + "]"));
    }

    @JsonValue
    public String getIdentifier() {
        return identifier;
    }
}
