package com.booking.booking_clone_backend.models.property;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PetsPolicy {
    NO("no"),
    YES("yes"),
    UPON_REQUEST("upon request");

    private final String wire;

    PetsPolicy(String wire) {
        this.wire = wire;
    }

    @JsonValue
    public String getWire() {
        return wire;
    }

    @JsonCreator
    public static PetsPolicy fromWire(String value) {
        if (value == null) return null;
        String v = value.trim().toLowerCase();

        for (PetsPolicy p : values()) {
            if (p.wire.equals(v)) return p;
        }
        throw new IllegalArgumentException("Unknown PetsPolicy: " + value);
    }
}
