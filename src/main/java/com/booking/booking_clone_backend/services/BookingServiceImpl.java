package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.requests.booking.CreateBookingRequest;
import com.booking.booking_clone_backend.exceptions.EntityInvalidArgumentException;
import com.booking.booking_clone_backend.exceptions.EntityNotFoundException;
import com.booking.booking_clone_backend.mappers.BookingCheckoutDetailsMapper;
import com.booking.booking_clone_backend.mappers.BookingCustomMapper;
import com.booking.booking_clone_backend.models.booking.Booking;
import com.booking.booking_clone_backend.models.booking.BookingCheckoutDetails;
import com.booking.booking_clone_backend.models.property.Property;
import com.booking.booking_clone_backend.models.user.User;
import com.booking.booking_clone_backend.repos.BookingRepo;
import com.booking.booking_clone_backend.repos.PropertyRepo;
import com.booking.booking_clone_backend.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepo bookingRepo;
    private final UserRepo userRepo;
    private final PropertyRepo propertyRepo;
    private final PropertyAvailabilityService propertyAvailabilityService;
    private final BookingCustomMapper bookingCustomMapper;
    private final BookingCheckoutDetailsMapper bookingCheckoutDetailsMapper;

    @Transactional
    @Override
    public Long createBooking(CreateBookingRequest request, String userEmail) throws EntityNotFoundException, EntityInvalidArgumentException {
        try {
            User user = userRepo.findByEmailIgnoreCase(userEmail)
                    .orElseThrow(() -> new EntityNotFoundException("CreateBookingUser", "user email not found. userEmail=" + userEmail));

            Property property = propertyRepo.findById(request.propertyId())
                    .orElseThrow(() -> new EntityNotFoundException("CreateBookingProperty", "property not found. propertyId=" + request.propertyId()));

            Integer maxGuests = property.getMaxGuests();
            Integer guestCount = request.guestCount();
            if (maxGuests != null && guestCount != null && guestCount > maxGuests) {
                throw new EntityInvalidArgumentException("CreateBookingGuestCount", "guestCount exceeds maxGuests. propertyId=" + request.propertyId() + ", guestCount=" + guestCount + ", maxGuests=" + maxGuests);
            }

            if (property.getPricePerNight() == null) {
                throw new EntityInvalidArgumentException("CreateBookingPrice", "property price per night is not set. propertyId=" + request);
            }

            long nightsLong = ChronoUnit.DAYS.between(request.checkIn(), request.checkOut());
            if (nightsLong <= 0) {
                throw new EntityInvalidArgumentException("CreateBookingDates", "check-out date must be after check-in date. propertyId=" + request.propertyId() + ", checkIn=" + request.checkIn() + ", checkOut=" + request.checkOut());
            }

            BigDecimal total = BigDecimal.valueOf(nightsLong).multiply(property.getPricePerNight());

            BookingCheckoutDetails details = bookingCheckoutDetailsMapper.toEntity(request.checkOutDetails());
            Booking booking = bookingCustomMapper.createBookingRequestToBooking(
                    request, property, user, details, total
            );
            booking.setHoldExpiresAt(Instant.now().plusSeconds(60));

            Booking savedBooking = bookingRepo.save(booking);

            // check availability and block dates for the booking
            propertyAvailabilityService.blockDatesForBooking(savedBooking);

            log.info("Booking created successfully. propertyId={}, bookingId={}", request.propertyId(), savedBooking.getId());
            return savedBooking.getId();

        } catch (EntityNotFoundException | EntityInvalidArgumentException e) {
            log.warn("Failed to create booking. propertyId={}, userEmail={}. Message: {}",request.propertyId(), userEmail, e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteBooking(Long bookingId) throws EntityNotFoundException {
        try {
            Booking booking = bookingRepo.findById(bookingId)
                    .orElseThrow(() -> new EntityNotFoundException("DeleteBooking", "Failed to delete booking: booking not found. bookingId=" + bookingId));
            bookingRepo.delete(booking);
            log.info("Booking deleted. bookingId={}", bookingId);
        } catch (EntityNotFoundException e) {
            log.warn("Booking deletion failed: not found. bookingId={}", bookingId);
            throw e;
        }
    }
}