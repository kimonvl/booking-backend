package com.booking.booking_clone_backend.DTOs.requests.partner.apartment;

import java.util.Map;

public record BedroomDTO(Map<BedType, Integer> beds) {
}
