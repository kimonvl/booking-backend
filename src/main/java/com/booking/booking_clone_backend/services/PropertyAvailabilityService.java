package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.models.booking.Booking;

import java.util.List;

public interface PropertyAvailabilityService {
    void blockDatesForBooking(Booking booking);
    int deleteBlocksByBookingIds(List<Long> bookingIds);
}
