package com.booking.booking_clone_backend.controllers;

import com.booking.booking_clone_backend.services.StripeWebhookService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/stripe")
public class StripeWebhookController {
    @Autowired
    private  StripeWebhookService webhookService;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(HttpServletRequest request,
                                          @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            String payload = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            webhookService.handleEvent(event);
            return ResponseEntity.ok("ok");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("invalid signature");
        }
    }

}
