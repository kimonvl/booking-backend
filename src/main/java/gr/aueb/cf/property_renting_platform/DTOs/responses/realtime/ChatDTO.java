package gr.aueb.cf.property_renting_platform.DTOs.responses.realtime;

import gr.aueb.cf.property_renting_platform.DTOs.responses.user.UserDTO;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record ChatDTO(
        UUID uuid,
        Long bookingId,
        Set<UserDTO> participants,
        Instant lastMessageAt
) {
}
