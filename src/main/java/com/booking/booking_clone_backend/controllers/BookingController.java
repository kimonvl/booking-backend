package com.booking.booking_clone_backend.controllers;

import com.booking.booking_clone_backend.DTOs.responses.GenericResponse;
import com.booking.booking_clone_backend.DTOs.responses.booking.BookingStatusResponse;
import com.booking.booking_clone_backend.controllers.controller_utils.ResponseFactory;
import com.booking.booking_clone_backend.models.booking.Booking;
import com.booking.booking_clone_backend.repos.BookingRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingRepo bookingRepo;

    @GetMapping("/{id}/status")
    public ResponseEntity<@NonNull GenericResponse<BookingStatusResponse>> getStatus(@PathVariable long id) {
        Booking b = bookingRepo.findById(id).orElseThrow();
        return ResponseFactory.createSuccessResponse(
                new BookingStatusResponse(b.getId(), b.getStatus(), b.getPaymentStatus()),
                "Booking status fetched",
                HttpStatus.OK
        );
    }
}
