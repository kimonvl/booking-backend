package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.requests.booking.CreatePaymentIntentRequest;
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
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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
    @Transactional
    public CreatePaymentIntentResponse createPaymentIntent(CreatePaymentIntentRequest req, String email) {
        Optional<User> userOpt = userRepo.findByEmailIgnoreCase(email);
        if (userOpt.isEmpty())
            throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);
        Optional<@NonNull Property> propertyOpt = propertyRepo.findById(req.propertyId());
        if (propertyOpt.isEmpty())
            throw new PropertyNotFoundException(MessageConstants.PROPERTY_NOT_FOUND);
        Property property = propertyOpt.get();

        // Check if property is available
        if (!propertyAvailabilityRepo.isAvailable(property.getId(), req.checkIn(), req.checkOut()))
            throw new PropertyAvailabilityException(MessageConstants.PROPERTY_AVAILABILITY);

        // Calculate total amount
        BigDecimal nights = BigDecimal.valueOf(ChronoUnit.DAYS.between(req.checkIn(), req.checkOut()));
        BigDecimal total = nights.multiply(property.getPricePerNight());
        long amountInCents = total.multiply(BigDecimal.valueOf(100)).longValueExact();

        // Create unconfirmed booking
        Booking booking = new Booking();
        BookingCheckoutDetails details = bookingCheckoutDetailsMapper.toEntity(req.checkOutDetails());
        booking.setProperty(property);
        booking.setGuest(userOpt.get());
        booking.setCheckInDate(req.checkIn());
        booking.setCheckOutDate(req.checkOut());
        booking.setGuestCount(req.guestCount());
        booking.setStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.REQUIRES_PAYMENT);
        booking.setAmountTotal(BigDecimal.valueOf(amountInCents));
        booking.setCheckoutDetails(details);
        Booking savedBooking = bookingRepo.save(booking);

        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency(booking.getProperty().getCurrency().name().toLowerCase()) // e.g. "eur"
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .putMetadata("bookingId", String.valueOf(savedBooking.getId()))
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            savedBooking.setPaymentIntentId(intent.getId());
            savedBooking.setPaymentStatus(PaymentStatus.PROCESSING);
            bookingRepo.save(booking);

            return new CreatePaymentIntentResponse(booking.getId(), intent.getClientSecret());
        } catch (Exception e) {
            booking.setPaymentStatus(PaymentStatus.FAILED);
            bookingRepo.save(booking);
            throw new RuntimeException("Stripe error: " + e.getMessage(), e);
        }
    }
}
