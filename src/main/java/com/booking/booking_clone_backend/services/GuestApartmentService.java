package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.responses.property.PropertyShortDTO;

import java.util.List;

public interface GuestApartmentService {
    List<PropertyShortDTO> getPropertiesByCity(String city);
}
