package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.exceptions.InternalErrorException;
import com.stripe.model.Event;

public interface StripeWebhookService {
    public void handleEvent(Event event) throws InternalErrorException;
}
