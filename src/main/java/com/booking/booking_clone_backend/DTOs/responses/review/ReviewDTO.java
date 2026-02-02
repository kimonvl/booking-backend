package com.booking.booking_clone_backend.DTOs.responses.review;

import com.booking.booking_clone_backend.DTOs.responses.user.UserDTO;

import java.time.Instant;

public record ReviewDTO(
        long id,
        int rating,
        String positiveComment,
        String negativeComment,
        String ownerResponse,
        Instant createdAt,
        Instant ownerRespondedAt,
        UserDTO guest
) {}
