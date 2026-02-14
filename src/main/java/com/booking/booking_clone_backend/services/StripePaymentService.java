package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.requests.booking.CreateBookingRequest;
import com.booking.booking_clone_backend.DTOs.responses.booking.CreatePaymentIntentResponse;
import com.stripe.exception.StripeException;

public interface StripePaymentService {
    public String createPaymentIntent(Long bookingId, String email) throws StripeException;
}
