package com.booking.booking_clone_backend.DTOs.requests.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateReviewRequest(
        @NotNull Long bookingId,
        @Min(1) @Max(10) int rating,
        String positiveComment,
        String negativeComment
) {}
