package com.booking.booking_clone_backend.exceptions;

import com.booking.booking_clone_backend.DTOs.responses.GenericResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler responsible for gathering and translating application-specific
 * exceptions into standardized http responses.
 *
 * <p>This class ensures consistent error responses by wrapping the
 * exception messages into {@link GenericResponse} which is contained inside {@link ResponseEntity}
 * with appropriate http status codes.</p>
 * */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles cases where a user tries to register with an email
     * already associated with an existing account.
     *
     * @param exception the thrown {@link EmailAlreadyInUseException}
     * @return a response with a message indicating registration failure
     * */
    @ExceptionHandler(EmailAlreadyInUseException.class)
    ResponseEntity<@NonNull GenericResponse<?>> handleEmailInUse(EmailAlreadyInUseException exception){
        return new ResponseEntity<>(new GenericResponse<>(null, exception.getMessage(), false), HttpStatus.IM_USED);
    }

    /**
     * Handles cases where a user tries to log in with wrong
     * email, password or role.
     *
     * @param exception the thrown {@link WrongCredentialsException}
     * @return a response with a message indicating authentication failure
     * */
    @ExceptionHandler(WrongCredentialsException.class)
    ResponseEntity<@NonNull GenericResponse<?>> handleWrongCredentials(WrongCredentialsException exception){
        return new ResponseEntity<>(new GenericResponse<>(null, exception.getMessage(), false), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles cases where the access token is missing from the cookies
     * in a request to a protected endpoint
     *
     * @param exception the thrown {@link MissingRefreshTokenException}
     * @return a response with a message indicating the authorization failure
     * */
    @ExceptionHandler(MissingRefreshTokenException.class)
    ResponseEntity<@NonNull GenericResponse<?>> handleMissingAccessToken(MissingRefreshTokenException exception){
        return new ResponseEntity<>(new GenericResponse<>(null, exception.getMessage(), false), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles cases where the access token is expired or invalid.
     *
     * @param exception the thrown {@link InvalidRefreshTokenException}
     * @return a response with a message indicating the authorization failure
     * */
    @ExceptionHandler(InvalidRefreshTokenException.class)
    ResponseEntity<@NonNull GenericResponse<?>> handleInvalidAccessToken(InvalidRefreshTokenException exception){
        return new ResponseEntity<>(new GenericResponse<>(null, exception.getMessage(), false), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles cases where invalid country code is given from frontend.
     *
     * @param exception the thrown {@link InvalidCountryCodeException}
     * @return a response with a message indicating the authorization failure
     * */
    @ExceptionHandler(InvalidCountryCodeException.class)
    ResponseEntity<@NonNull GenericResponse<?>> handleInvalidCountryCode(InvalidCountryCodeException exception){
        return new ResponseEntity<>(new GenericResponse<>(null, exception.getMessage(), false), HttpStatus.UNAUTHORIZED);
    }
}