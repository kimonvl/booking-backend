package com.booking.booking_clone_backend.DTOs.responses.property;

public record AddressDTO(
        Long propertyId,
        String country,
        String city,
        String postcode,
        String street,
        String streetNumber
) {
}
