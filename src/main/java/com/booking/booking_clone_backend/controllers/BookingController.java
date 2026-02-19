package com.booking.booking_clone_backend.controllers;

import com.booking.booking_clone_backend.DTOs.requests.booking.CreateBookingRequest;
import com.booking.booking_clone_backend.DTOs.responses.GenericResponse;
import com.booking.booking_clone_backend.DTOs.responses.booking.BookingStatusResponse;
import com.booking.booking_clone_backend.constants.MessageConstants;
import com.booking.booking_clone_backend.controllers.controller_utils.ResponseFactory;
import com.booking.booking_clone_backend.models.booking.Booking;
import com.booking.booking_clone_backend.repos.BookingRepo;
import com.booking.booking_clone_backend.services.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Locale;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final MessageSource messageSource;
    private final BookingRepo bookingRepo;

    @GetMapping("/{id}/status")
    public ResponseEntity<@NonNull GenericResponse<BookingStatusResponse>> getStatus(@PathVariable long id) {
        Booking b = bookingRepo.findById(id).orElseThrow();
        return ResponseFactory.createResponse(
                new BookingStatusResponse(b.getId(), b.getStatus(), b.getPaymentStatus()),
                "Booking status fetched",
                HttpStatus.OK,
                true
        );
    }

    @PostMapping("/create")
    public ResponseEntity<@NonNull GenericResponse<Long>> createBooking(
            @Valid @RequestBody CreateBookingRequest request,
            Principal principal,
            Locale locale
    ) {
        return new ResponseEntity<>(
                new GenericResponse<>(
                        bookingService.createBooking(request, principal.getName()),
                        messageSource.getMessage("booking.created", null, MessageConstants.BOOKING_CREATED, locale),
                        true
                ),
                HttpStatus.CREATED
        );

    }

    @PostMapping("/cancel/{bookingId}")
    public ResponseEntity<@NonNull GenericResponse<?>> cancelBooking(
            @PathVariable Long bookingId,
            Locale locale
    ) {
        bookingService.deleteBooking(bookingId);
        return new ResponseEntity<>(
                new GenericResponse<>(
                        null,
                        messageSource.getMessage("booking.deleted", null, MessageConstants.BOOKING_DELETED, locale),
                        true
                ),
                HttpStatus.CREATED
        );

    }
}
