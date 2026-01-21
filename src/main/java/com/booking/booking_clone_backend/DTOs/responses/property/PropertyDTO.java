package com.booking.booking_clone_backend.DTOs.responses.property;

import com.booking.booking_clone_backend.DTOs.responses.dictionaries.amenity.AmenityDTO;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.language.LanguageDTO;
import com.booking.booking_clone_backend.DTOs.responses.user.UserDTO;
import com.booking.booking_clone_backend.models.property.CurrencyCode;
import com.booking.booking_clone_backend.models.property.PropertyStatus;
import com.booking.booking_clone_backend.models.property.PropertyType;

import java.math.BigDecimal;
import java.util.Set;

public record PropertyDTO(
        long id,
        UserDTO owner,
        Set<AmenityDTO> propertyAmenities,
        Set<LanguageDTO> propertyLanguages,
        AddressDTO address,
        PropertyType type,
        PropertyStatus status,
        String name,
        BigDecimal pricePerNight,
        CurrencyCode currency,
        Integer maxGuests,
        Integer bathrooms,
        BigDecimal sizeSqm,
        boolean childrenAllowed,
        boolean cotsOffered,
        String mainPhotoId
) {
}
