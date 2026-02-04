package com.booking.booking_clone_backend.DTOs.requests.booking;

import java.time.LocalDate;

public record CreatePaymentIntentRequest(
        long propertyId,
        LocalDate checkIn,
        LocalDate checkOut,
        int guestCount,
        CheckOutDetailsDTO checkOutDetails
) {}
