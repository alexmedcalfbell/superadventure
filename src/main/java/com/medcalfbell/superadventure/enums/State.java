package com.medcalfbell.superadventure.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.medcalfbell.superadventure.exceptions.StateNotFoundException;
import java.util.stream.Stream;

public enum State {

    WITCH_STABBED("witch_stabbed");

    private String identifier;

    State(String identifier) {
        this.identifier = identifier;
    }

    public static State fromIdentifier(String identifier) {
        return Stream.of(values())
                .filter(value -> value.identifier.equalsIgnoreCase(identifier))
                .findFirst()
                .orElseThrow(
                        () -> new StateNotFoundException("No state found for [" + identifier + "]"));
    }

    @JsonValue
    public String getIdentifier() {
        return identifier;
    }
}
