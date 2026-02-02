package com.booking.booking_clone_backend.DTOs.responses.review;

public record ReviewSummaryDTO(
        double averageRating,
        long reviewCount
) {}
