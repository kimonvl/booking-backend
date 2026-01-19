package com.booking.booking_clone_backend.DTOs.requests.apartment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BedType {
    SINGLE("single"),
    DOUBLE("double"),
    KING_SIZE("king_size"),
    SINGLE_SOFA("single_sofa"),
    DOUBLE_SOFA("double_sofa");

    private final String wire;

    BedType(String wire) {
        this.wire = wire;
    }

    @JsonValue
    public String getWire() {
        return wire;
    }

    @JsonCreator
    public static BedType fromWire(String value) {
        if (value == null) return null;
        String v = value.trim().toLowerCase();

        for (BedType t : values()) {
            if (t.wire.equals(v)) return t;
        }
        throw new IllegalArgumentException("Unknown BedType: " + value);
    }
}