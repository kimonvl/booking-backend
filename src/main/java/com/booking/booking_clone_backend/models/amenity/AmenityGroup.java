package com.booking.booking_clone_backend.models.amenity;

import lombok.Getter;

@Getter
public enum AmenityGroup {
    GENERAL("General"),
    COOKING_AND_CLEANING("Cooking and cleaning"),
    ENTERTAINMENT("Entertainment"),
    OUTSIDE_AND_VIEW("Outside and view");

    private final String label;

    AmenityGroup(String label) {
        this.label = label;
    }

}