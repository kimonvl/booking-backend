package com.booking.booking_clone_backend.controllers;

import com.booking.booking_clone_backend.DTOs.requests.booking.CreateBookingRequest;
import com.booking.booking_clone_backend.DTOs.responses.GenericResponse;
import com.booking.booking_clone_backend.DTOs.responses.booking.CreatePaymentIntentResponse;
import com.booking.booking_clone_backend.controllers.controller_utils.ResponseFactory;
import com.booking.booking_clone_backend.services.StripePaymentService;
import com.stripe.exception.StripeException;
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
@RequestMapping("/payments")
public class PaymentController {

    private final StripePaymentService stripePaymentService;
    private final MessageSource messageSource;

    @PostMapping("/create-intent")
    public ResponseEntity<@NonNull GenericResponse<String>> createIntent(
            @RequestBody Long bookingId,
            Principal principal,
            Locale locale
    ) {
        String res = null;
        try {
            res = stripePaymentService.createPaymentIntent(bookingId, principal.getName());
        } catch (StripeException e) {
            return new ResponseEntity<>(
                new GenericResponse<>(
                    null,
                    messageSource.getMessage("payment.create_intent.failed", null, "Payment failed", locale),
                    false
                ),
                HttpStatus.BAD_GATEWAY
            );
        }
        return ResponseFactory.createResponse(res, "payment.create_intent.succeeded", HttpStatus.OK, true);
    }
}
