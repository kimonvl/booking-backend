package com.booking.booking_clone_backend.DTOs.requests.booking;

public record CheckOutDetailsDTO(
        Boolean travelingForWork,
        String title,
        String firstName,
        String lastName,
        String email,
        String phoneCountryCode,
        String phoneNumber,
        String specialRequest
) {
}
