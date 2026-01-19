package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.requests.apartment.CreateApartmentRequest;
import com.booking.booking_clone_backend.models.property.Property;
import com.booking.booking_clone_backend.models.user.User;

public interface ApartmentService {
    Property addApartment(CreateApartmentRequest request, User user);
}
