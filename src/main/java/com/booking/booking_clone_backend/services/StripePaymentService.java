package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.requests.booking.CreatePaymentIntentRequest;
import com.booking.booking_clone_backend.DTOs.responses.booking.CreatePaymentIntentResponse;

public interface StripePaymentService {
    public CreatePaymentIntentResponse createPaymentIntent(CreatePaymentIntentRequest req, String email);
}
