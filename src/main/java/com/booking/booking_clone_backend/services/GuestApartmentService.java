package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.requests.guest.property.PropertySearchRequest;
import com.booking.booking_clone_backend.DTOs.responses.property.PropertyDetailsDTO;
import com.booking.booking_clone_backend.DTOs.responses.property.PropertyShortDTO;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GuestApartmentService {

    Page<@NonNull PropertyShortDTO> search(PropertySearchRequest request);

    PropertyDetailsDTO getPropertyDetails(Long propertyId);
}
