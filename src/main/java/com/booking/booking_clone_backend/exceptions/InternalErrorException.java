package com.booking.booking_clone_backend.exceptions;

public class InternalErrorException extends AppGenericException {
    private static final String DEFAULT_CODE = "InternalError";

    public InternalErrorException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
