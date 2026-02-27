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
    public Long createBooking(CreateBookingRequest request, String userEmail) {
        try {
            User user = userRepo.findByEmailIgnoreCase(userEmail)
                    .orElseThrow(() -> new EntityNotFoundException("booking.create.user.not_found"));

            Property property = propertyRepo.findById(request.propertyId())
                    .orElseThrow(() -> new EntityNotFoundException("booking.create.property.not_found"));

            Integer maxGuests = property.getMaxGuests();
            Integer guestCount = request.guestCount();
            if (maxGuests != null && guestCount != null && guestCount > maxGuests) {
                throw new EntityInvalidArgumentException("booking.create.guest_count.greater");
            }

            if (property.getPricePerNight() == null) {
                throw new EntityInvalidArgumentException("booking.create.property.not_priced");
            }

            long nightsLong = ChronoUnit.DAYS.between(request.checkIn(), request.checkOut());
            if (nightsLong <= 0) {
                throw new EntityInvalidArgumentException("availability.dates.invalid");
            }

            BigDecimal total = BigDecimal.valueOf(nightsLong).multiply(property.getPricePerNight());

            BookingCheckoutDetails details = bookingCheckoutDetailsMapper.toEntity(request.checkOutDetails());
            Booking booking = bookingCustomMapper.createBookingRequestToBooking(
                    request, property, user, details, total
            );
            booking.setHoldExpiresAt(Instant.now().plusSeconds(60));

            Booking savedBooking = bookingRepo.save(booking);

            propertyAvailabilityService.blockDatesForBooking(savedBooking);

            log.info("Booking created successfully. propertyId={}, bookingId={}", request.propertyId(), savedBooking.getId());
            return savedBooking.getId();

        } catch (EntityNotFoundException e) {
            switch (e.getMessage()) {
                case "booking.create.user.not_found" ->
                        log.warn("Create booking failed: guest email not found. userEmail={}", userEmail);
                case "booking.create.property.not_found" ->
                        log.warn("Create booking failed: property not found. propertyId={}", request.propertyId());
                default ->
                        log.warn("Create booking failed: not found. code={}, propertyId={}, userEmail={}",
                                e.getMessage(), request.propertyId(), userEmail);
            }
            throw e;

        } catch (EntityInvalidArgumentException e) {
            switch (e.getMessage()) {
                case "booking.create.guest_count.greater" ->
                        log.warn("Create booking failed: guestCount={} exceeds maxGuests for propertyId={}",
                                request.guestCount(), request.propertyId());
                case "availability.dates.invalid" ->
                        log.warn("Create booking failed: Invalid range. checkIn={} and checkOut={}",
                                request.checkIn(), request.checkOut());
                case "booking.create.property.not_available" ->
                        log.warn("Create booking failed: Property not available. propertyId={}, checkIn={}, checkOut={}",
                                request.propertyId(), request.checkIn(), request.checkOut());
                default ->
                        log.warn("Create booking failed: invalid argument. code={}, propertyId={}",
                                e.getMessage(), request.propertyId());
            }
            throw e;
        }
    }

    @Override
    public void deleteBooking(Long bookingId) {
        try {
            Booking booking = bookingRepo.findById(bookingId)
                    .orElseThrow(() -> new EntityNotFoundException("booking.delete.not_found"));
            bookingRepo.delete(booking);
            log.info("Booking deleted. bookingId={}", bookingId);
        } catch (EntityNotFoundException e) {
            log.warn("Booking deletion failed: not found. bookingId={}", bookingId);
            throw e;
        }
    }
}