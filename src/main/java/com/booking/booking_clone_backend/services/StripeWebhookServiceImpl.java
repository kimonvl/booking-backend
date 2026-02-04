package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.models.booking.BookingStatus;
import com.booking.booking_clone_backend.models.booking.PaymentStatus;
import com.booking.booking_clone_backend.repos.BookingRepo;
import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class StripeWebhookServiceImpl implements StripeWebhookService{
    @Autowired
    private BookingRepo bookingRepo;

    @Override
    @Transactional
    public void handleEvent(Event event) {
        switch (event.getType()) {
            case "payment_intent.succeeded" -> handlePaymentSucceeded(event);
            case "payment_intent.payment_failed" -> handlePaymentFailed(event);
            default -> {
                // ignore
            }
        }
    }


    private void handlePaymentSucceeded(Event event) {
        System.out.println("Payment succeeded");

        StripeObject stripeObject = null;
        try {
            stripeObject = event.getDataObjectDeserializer().deserializeUnsafe();
        } catch (EventDataObjectDeserializationException e) {
            throw new RuntimeException(e);
        }

        if (!(stripeObject instanceof PaymentIntent intent)) {
            System.out.println("⚠️ Not a PaymentIntent. Was: " + stripeObject.getClass().getName());
            return;
        }

        System.out.println("Payment succeeded " + intent.getId());

        bookingRepo.findByPaymentIntentId(intent.getId())
                .ifPresent(booking -> {
                    if (booking.getPaymentStatus() == PaymentStatus.SUCCEEDED) return;

                    booking.setPaymentStatus(PaymentStatus.SUCCEEDED);
                    booking.setStatus(BookingStatus.CONFIRMED);
                    booking.setPaidAt(Instant.now());
                    bookingRepo.save(booking);
                });
    }

    private void handlePaymentFailed(Event event) {
        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject()
                .orElse(null);
        System.out.println("Payment failed");
        if (intent == null) return;
        System.out.println("Payment failed " + intent.getId());

        bookingRepo.findByPaymentIntentId(intent.getId())
                .ifPresent(booking -> {
                    if (booking.getPaymentStatus() == PaymentStatus.SUCCEEDED) return;
                    booking.setPaymentStatus(PaymentStatus.FAILED);
                    bookingRepo.save(booking);
                });
    }
}
