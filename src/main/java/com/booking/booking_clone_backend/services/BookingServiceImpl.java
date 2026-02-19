package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.requests.booking.CreateBookingRequest;
import com.booking.booking_clone_backend.constants.MessageConstants;
import com.booking.booking_clone_backend.exceptions.*;
import com.booking.booking_clone_backend.mappers.BookingCheckoutDetailsMapper;
import com.booking.booking_clone_backend.mappers.BookingCustomMapper;
import com.booking.booking_clone_backend.models.availability.PropertyAvailability;
import com.booking.booking_clone_backend.models.booking.Booking;
import com.booking.booking_clone_backend.models.booking.BookingCheckoutDetails;
import com.booking.booking_clone_backend.models.property.Property;
import com.booking.booking_clone_backend.models.user.User;
import com.booking.booking_clone_backend.repos.BookingRepo;
import com.booking.booking_clone_backend.repos.PropertyAvailabilityRepo;
import com.booking.booking_clone_backend.repos.PropertyRepo;
import com.booking.booking_clone_backend.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final BookingRepo bookingRepo;
    private final UserRepo userRepo;
    private final PropertyRepo propertyRepo;
    private final PropertyAvailabilityRepo propertyAvailabilityRepo;
    private final BookingCustomMapper bookingCustomMapper;
    private final BookingCheckoutDetailsMapper bookingCheckoutDetailsMapper;

    @Override
    public Long createBooking(CreateBookingRequest request, String userEmail) {
        try {
            System.out.println(request.checkIn());
            System.out.println(request.checkOut());
            Optional<User> userOpt = userRepo.findByEmailIgnoreCase(userEmail);
            if (userOpt.isEmpty())
                throw new EntityNotFoundException("booking.create.user.not_found");
            Optional<@NonNull Property> propertyOpt = propertyRepo.findById(request.propertyId());
            if (propertyOpt.isEmpty())
                throw new EntityNotFoundException("booking.create.property.not_found");
            Property property = propertyOpt.get();

            // Check if property is available
            if (!propertyAvailabilityRepo.isAvailable(property.getId(), request.checkIn(), request.checkOut()))
                throw new EntityInvalidArgumentException("booking.create.property.not_available");

            // Calculate total amount
            BigDecimal nights = BigDecimal.valueOf(ChronoUnit.DAYS.between(request.checkIn(), request.checkOut()));
            BigDecimal total = nights.multiply(property.getPricePerNight());

            // Create unconfirmed booking
            BookingCheckoutDetails details = bookingCheckoutDetailsMapper.toEntity(request.checkOutDetails());
            Booking booking = bookingCustomMapper.createBookingRequestToBooking(
                    request,
                    property,
                    userOpt.get(),
                    details,
                    total
            );
            booking.setHoldExpiresAt(Instant.now().plusSeconds(1 * 60));
            Booking savedBooking = bookingRepo.save(booking);

            // Block the dates
            PropertyAvailability pa = new PropertyAvailability();
            pa.setBooking(savedBooking);
            pa.setProperty(savedBooking.getProperty());
            pa.setStartDate(savedBooking.getCheckInDate());
            pa.setEndDate(savedBooking.getCheckOutDate());
            propertyAvailabilityRepo.save(pa);
            propertyAvailabilityRepo.flush();

            log.info("Booking created successfully for property with id={}", request.propertyId());
            return savedBooking.getId();
        } catch (DataIntegrityViolationException e) {
            if (isExclusionViolation(e)) {
                // translate to your domain exception + message key
                throw new EntityInvalidArgumentException("booking.create.property.not_available");
            }
            log.error("Failed to create booking for property with id={}: Internal server error", request.propertyId(), e);
            throw e; // unknown integrity issue
        } catch (EntityNotFoundException e) {
            log.warn("Failed to create booking for property with id={} and guest with email={}", request.propertyId(), userEmail, e);
            throw e;
        } catch (EntityInvalidArgumentException e) {
            log.warn("Failed to create booking for property with id={}: Property is unavailable for that date range (checkIn={} - checkOut={})", request.propertyId(), request.checkIn(), request.checkOut(), e);
            throw e;
        }
    }

    @Override
    public void deleteBooking(Long bookingId) {
        try {
            Booking booking = bookingRepo.findById(bookingId)
                    .orElseThrow(() -> new EntityNotFoundException("booking.delete.not_found"));
            bookingRepo.delete(booking);
            log.info("Booking deleted with id={}", bookingId);
        } catch (EntityNotFoundException e) {
            log.warn("Booking deletion failed: Booking with id ={} not found", bookingId, e);
            throw e;
        }

    }

    private boolean isExclusionViolation(DataIntegrityViolationException e) {
        Throwable t = e;
        while (t != null) {
            if (t instanceof ConstraintViolationException cve) {
                // Hibernate knows the constraint name
                return "ex_availability_no_overlap".equals(cve.getConstraintName());
            }
            t = t.getCause();
        }
        return false;
    }
}
