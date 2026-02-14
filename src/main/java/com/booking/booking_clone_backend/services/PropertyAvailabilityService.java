package com.booking.booking_clone_backend.services;

import java.time.LocalDate;
import java.util.List;

public interface PropertyAvailabilityService {
    boolean assertPropertyAvailability(Long propertyId, LocalDate checkIn, LocalDate checkOut);
    int deleteBlocksByBookingIds(List<Long> bookingIds);
}
