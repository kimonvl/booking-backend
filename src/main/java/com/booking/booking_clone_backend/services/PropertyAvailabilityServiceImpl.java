package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.exceptions.EntityInvalidArgumentException;
import com.booking.booking_clone_backend.models.availability.PropertyAvailability;
import com.booking.booking_clone_backend.models.booking.Booking;
import com.booking.booking_clone_backend.repos.PropertyAvailabilityRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropertyAvailabilityServiceImpl implements PropertyAvailabilityService {

    private final PropertyAvailabilityRepo propertyAvailabilityRepo;

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public void blockDatesForBooking(Booking booking) {
        LocalDate checkIn = booking.getCheckInDate();
        LocalDate checkOut = booking.getCheckOutDate();

        validateDates(checkIn, checkOut);

        try {
            PropertyAvailability pa = new PropertyAvailability();
            pa.setBooking(booking);
            pa.setProperty(booking.getProperty());
            pa.setStartDate(checkIn);
            pa.setEndDate(checkOut);

            propertyAvailabilityRepo.save(pa);
            propertyAvailabilityRepo.flush();

        } catch (DataIntegrityViolationException e) {
            if (isExclusionViolation(e)) {
                throw new EntityInvalidArgumentException("booking.create.property.not_available");
            }
            log.error("Block dates failed: integrity violation. propertyId={}, checkIn={}, checkOut={}",
                    booking.getProperty().getId(), checkIn, checkOut, e);
            throw e;
        }
    }

    @Override
    public int deleteBlocksByBookingIds(List<Long> bookingIds) {
        return propertyAvailabilityRepo.deleteByBookingIds(bookingIds);
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new EntityInvalidArgumentException("availability.dates.invalid");
        }
        if (!checkIn.isBefore(checkOut)) {
            throw new EntityInvalidArgumentException("availability.dates.invalid");
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new EntityInvalidArgumentException("availability.dates.invalid");
        }
    }

    private boolean isExclusionViolation(DataIntegrityViolationException e) {
        Throwable t = e;
        while (t != null) {
            if (t instanceof ConstraintViolationException cve) {
                return "ex_availability_no_overlap".equals(cve.getConstraintName());
            }
            t = t.getCause();
        }
        return false;
    }
}