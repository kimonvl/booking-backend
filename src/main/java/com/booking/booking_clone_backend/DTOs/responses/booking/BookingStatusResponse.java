package com.booking.booking_clone_backend.DTOs.responses.booking;

import com.booking.booking_clone_backend.models.booking.BookingStatus;
import com.booking.booking_clone_backend.models.booking.PaymentStatus;

public record BookingStatusResponse(
        long id,
        BookingStatus status,
        PaymentStatus paymentStatus
) {}
