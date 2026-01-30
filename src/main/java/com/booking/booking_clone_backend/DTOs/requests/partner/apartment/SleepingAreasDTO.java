package com.booking.booking_clone_backend.DTOs.requests.partner.apartment;

import java.util.List;

public record SleepingAreasDTO(
        List<BedroomDTO> bedrooms,
        LivingRoomDTO livingRoom
) {
}
