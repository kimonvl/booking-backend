package com.booking.booking_clone_backend.DTOs.responses.dictionaries.amenity;

import java.util.List;

public record AmenitiesDictionaryItemDTO(String title, List<AmenityDTO> items) {
}
