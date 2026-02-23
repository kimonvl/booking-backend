package com.booking.booking_clone_backend.exceptions;

public class MediaUploadFailedException extends RuntimeException {
    public MediaUploadFailedException(String message) {
        super(message);
    }
}
