package com.booking.booking_clone_backend.DTOs.responses.dictionaries.amenity;

import com.booking.booking_clone_backend.models.static_data.AmenityGroup;

public record AmenityDTO(long id, String code, String label, AmenityGroup groupName) {
}
