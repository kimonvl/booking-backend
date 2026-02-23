package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.requests.booking.CreateBookingRequest;
import com.booking.booking_clone_backend.DTOs.responses.booking.CreatePaymentIntentResponse;
import com.booking.booking_clone_backend.constants.MessageConstants;
import com.booking.booking_clone_backend.exceptions.*;
import com.booking.booking_clone_backend.mappers.BookingCheckoutDetailsMapper;
import com.booking.booking_clone_backend.models.booking.Booking;
import com.booking.booking_clone_backend.models.booking.BookingCheckoutDetails;
import com.booking.booking_clone_backend.models.booking.BookingStatus;
import com.booking.booking_clone_backend.models.booking.PaymentStatus;
import com.booking.booking_clone_backend.models.property.Property;
import com.booking.booking_clone_backend.models.user.User;
import com.booking.booking_clone_backend.repos.BookingRepo;
import com.booking.booking_clone_backend.repos.PropertyAvailabilityRepo;
import com.booking.booking_clone_backend.repos.PropertyRepo;
import com.booking.booking_clone_backend.repos.UserRepo;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class StripePaymentServiceImpl implements StripePaymentService{
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PropertyRepo propertyRepo;
    @Autowired
    private PropertyAvailabilityRepo propertyAvailabilityRepo;
    @Autowired
    private BookingRepo bookingRepo;
    @Autowired
    private BookingCheckoutDetailsMapper bookingCheckoutDetailsMapper;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Override
    @Transactional(noRollbackFor = {StripeException.class},
            rollbackFor = {EntityNotFoundException.class, EntityInvalidArgumentException.class})
    public String createPaymentIntent(Long bookingId, String email) throws StripeException {
        Booking booking = null;
        try {
            booking = bookingRepo.findById(bookingId)
                    .orElseThrow(() -> new EntityNotFoundException("payment.create.property.not_found"));
            User user = userRepo.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new EntityNotFoundException("payment.create.user.not_found"));

            // Check if user that issues the payment request is the same as the user that created the booking
            if (!Objects.equals(user, booking.getGuest())) {
                throw new EntityInvalidArgumentException("payment.create.user.mismatch");
            }

            if (booking.getPaymentStatus() != PaymentStatus.REQUIRES_PAYMENT) {
                throw new EntityInvalidArgumentException("payment.create.booking.not_pending");
            }

            if (booking.getHoldExpiresAt() != null && booking.getHoldExpiresAt().isBefore(Instant.now())) {
                throw new EntityInvalidArgumentException("payment.create.hold.expired");
            }

            long amountInCents = booking.getAmountTotal()
                    .movePointRight(2)      // 12345
                    .setScale(0, RoundingMode.HALF_UP)
                    .longValueExact();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency(booking.getProperty().getCurrency().name().toLowerCase()) // e.g. "eur"
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .putMetadata("bookingId", String.valueOf(booking.getId()))
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            booking.setPaymentIntentId(intent.getId());
            booking.setPaymentStatus(PaymentStatus.PROCESSING);
            bookingRepo.save(booking);

            log.info("Payment intent created for booking with id={} and guest with email={}", booking, email);
            return intent.getClientSecret();
        } catch (StripeException e) {
            if (booking != null) {
                booking.setPaymentStatus(PaymentStatus.FAILED);
                bookingRepo.save(booking);
            }
            throw e;
        } catch (EntityNotFoundException | EntityInvalidArgumentException e) {
            log.warn("Create payment intent failed for booking with id={} and guest with email={}", bookingId, email, e);
            throw e;
        }
    }
}
