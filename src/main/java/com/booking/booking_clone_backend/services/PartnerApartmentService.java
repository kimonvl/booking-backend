package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.requests.apartment.CreateApartmentRequest;
import com.booking.booking_clone_backend.models.property.Property;
import com.booking.booking_clone_backend.models.user.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PartnerApartmentService {
    Property addApartment(CreateApartmentRequest request, List<MultipartFile> photos, Integer mainIndex, User user);
}
