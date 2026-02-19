package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.requests.booking.CreateBookingRequest;

public interface BookingService {
    Long createBooking(CreateBookingRequest request, String userEmail);
    void deleteBooking(Long bookingId);
}
