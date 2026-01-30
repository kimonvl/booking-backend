package com.booking.booking_clone_backend.DTOs.requests.guest.property;

import com.booking.booking_clone_backend.models.property.PropertyType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PropertySearchRequest(
        String city,
        PropertyType type,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Integer maxGuest,
        Integer bathroomCount,
        Integer bedroomCount,
        LocalDate checkIn,
        LocalDate checkOut,
        List<String> amenities,
        Boolean pets,
        Integer page,
        Integer size
) {
}
