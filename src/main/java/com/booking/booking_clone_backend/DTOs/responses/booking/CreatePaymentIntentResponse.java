package com.booking.booking_clone_backend.DTOs.responses.booking;

public record CreatePaymentIntentResponse(
        long bookingId,
        String clientSecret
) {}
