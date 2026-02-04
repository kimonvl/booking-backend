package com.booking.booking_clone_backend.controllers;

import com.booking.booking_clone_backend.DTOs.requests.booking.CreatePaymentIntentRequest;
import com.booking.booking_clone_backend.DTOs.responses.GenericResponse;
import com.booking.booking_clone_backend.DTOs.responses.booking.CreatePaymentIntentResponse;
import com.booking.booking_clone_backend.controllers.controller_utils.ResponseFactory;
import com.booking.booking_clone_backend.services.StripePaymentService;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final StripePaymentService stripePaymentService;

    public PaymentController(StripePaymentService stripePaymentService) {
        this.stripePaymentService = stripePaymentService;
    }

    @PostMapping("/create-intent")
    public ResponseEntity<@NonNull GenericResponse<CreatePaymentIntentResponse>> createIntent(
            @Valid @RequestBody CreatePaymentIntentRequest req,
            Principal principal
    ) {
        var res = stripePaymentService.createPaymentIntent(req, principal.getName());
        return ResponseFactory.createSuccessResponse(res, "Payment intent created", HttpStatus.OK);
    }
}
