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
     * Handles cases where a user attempts to create an entity that already exists.
     *
     * @param exception the thrown {@link EntityAlreadyExistsException}
     * @return a response with a message indicating that the entity cannot be created because it already exists.
     * */
    @ExceptionHandler(EntityAlreadyExistsException.class)
    ResponseEntity<@NonNull GenericResponse<?>> handleEntityAlreadyExists(EntityAlreadyExistsException exception){
        return new ResponseEntity<>(new GenericResponse<>(null, exception.getMessage(), false), HttpStatus.CONFLICT);
    }

    /**
     * Handles cases where a user attempts to fetch an entity that doesn't exist.
     *
     * @param exception the thrown {@link EntityNotFoundException}
     * @return a response with a message indicating that the entity cannot be fetched because it doesn't exist.
     * */
    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<@NonNull GenericResponse<?>> handleEntityNotFound(EntityNotFoundException exception){
        return new ResponseEntity<>(new GenericResponse<>(null, exception.getMessage(), false), HttpStatus.NOT_FOUND);
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
     * Handles cases where invalid country code is given from frontend.
     *
     * @param exception the thrown {@link InvalidCountryCodeException}
     * @return a response with a message indicating the invalid country code given
     * */
    @ExceptionHandler(InvalidCountryCodeException.class)
    ResponseEntity<@NonNull GenericResponse<?>> handleInvalidCountryCode(InvalidCountryCodeException exception){
        return new ResponseEntity<>(new GenericResponse<>(null, exception.getMessage(), false), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles cases where username doesn't match a registered user.
     *
     * @param exception the thrown {@link UserNotFoundException}
     * @return a response with a message indicating the authentication failure
     * */
    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<@NonNull GenericResponse<?>> handleUserNotFound(UserNotFoundException exception){
        return new ResponseEntity<>(new GenericResponse<>(null, exception.getMessage(), false), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles cases where property id is not found.
     *
     * @param exception the thrown {@link PropertyNotFoundException}
     * @return a response with a message indicating the fetching failure
     * */
    @ExceptionHandler(PropertyNotFoundException.class)
    ResponseEntity<@NonNull GenericResponse<?>> handlePropertyNotFound(PropertyNotFoundException exception){
        return new ResponseEntity<>(new GenericResponse<>(null, exception.getMessage(), false), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles cases where country code is not found.
     *
     * @param exception the thrown {@link PropertyNotFoundException}
     * @return a response with a message indicating the fetching failure
     * */
    @ExceptionHandler(CountryNotFoundException.class)
    ResponseEntity<@NonNull GenericResponse<?>> handleCountryNotFound(CountryNotFoundException exception){
        return new ResponseEntity<>(new GenericResponse<>(null, exception.getMessage(), false), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles cases where booking id is not found.
     *
     * @param exception the thrown {@link BookingNotFoundException}
     * @return a response with a message indicating the fetching failure
     * */
    @ExceptionHandler(BookingNotFoundException.class)
    ResponseEntity<@NonNull GenericResponse<?>> handleBookingNotFound(BookingNotFoundException exception){
        return new ResponseEntity<>(new GenericResponse<>(null, exception.getMessage(), false), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles cases where property is not available for booking for a given time window.
     *
     * @param exception the thrown {@link BookingNotFoundException}
     * @return a response with a message indicating the fetching failure
     * */
    @ExceptionHandler(PropertyAvailabilityException.class)
    ResponseEntity<@NonNull GenericResponse<?>> handlePropertyAvailability(PropertyAvailabilityException exception){
        return new ResponseEntity<>(new GenericResponse<>(null, exception.getMessage(), false), HttpStatus.UNAUTHORIZED);
    }
}