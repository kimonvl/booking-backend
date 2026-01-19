package com.booking.booking_clone_backend.exceptions;

public class InvalidCountryCodeException extends RuntimeException {
    public InvalidCountryCodeException(String message) {
        super(message);
    }
}
