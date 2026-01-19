package com.booking.booking_clone_backend.DTOs.requests.apartment;

import java.util.Map;

public record LivingRoomDTO(Map<BedType, Integer> beds) {
}
