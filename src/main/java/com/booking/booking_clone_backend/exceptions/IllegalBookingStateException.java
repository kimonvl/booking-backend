package com.booking.booking_clone_backend.exceptions;

public class IllegalBookingStateException extends RuntimeException {
    public IllegalBookingStateException(String message) {
        super(message);
    }
}
