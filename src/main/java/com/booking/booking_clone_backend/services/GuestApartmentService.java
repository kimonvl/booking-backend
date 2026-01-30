package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.requests.guest.property.PropertySearchRequest;
import com.booking.booking_clone_backend.DTOs.responses.property.PropertyShortDTO;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GuestApartmentService {
    List<PropertyShortDTO> getPropertiesByCity(String city);

    Page<@NonNull PropertyShortDTO> search(PropertySearchRequest request);
}
