package com.booking.booking_clone_backend.exceptions;

public class EntityInvalidArgumentException extends RuntimeException {
    public EntityInvalidArgumentException(String message) {
        super(message);
    }
}
