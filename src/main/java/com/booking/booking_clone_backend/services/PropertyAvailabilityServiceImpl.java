package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.exceptions.EntityInvalidArgumentException;
import com.booking.booking_clone_backend.exceptions.EntityNotFoundException;
import com.booking.booking_clone_backend.models.property.Property;
import com.booking.booking_clone_backend.repos.PropertyAvailabilityRepo;
import com.booking.booking_clone_backend.repos.PropertyRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropertyAvailabilityServiceImpl implements PropertyAvailabilityService{

    private final PropertyAvailabilityRepo propertyAvailabilityRepo;
    private final PropertyRepo propertyRepo;

    @Override
    public boolean assertPropertyAvailability(Long propertyId, LocalDate checkIn, LocalDate checkOut)
            throws EntityNotFoundException, EntityInvalidArgumentException
    {
        try {
            Property property = propertyRepo.findById(propertyId)
                    .orElseThrow(() -> new EntityNotFoundException("availability.property.not_found"));

            if (checkIn == null || checkOut == null)
                throw new EntityInvalidArgumentException("availability.dates.invalid");

            // checkIn must be strictly before checkOut
            if (!checkIn.isBefore(checkOut))
                throw new EntityInvalidArgumentException("availability.dates.invalid");

            LocalDate today = LocalDate.now();

            // both dates must be today or in the future
            if (checkIn.isBefore(today))
                throw new EntityInvalidArgumentException("availability.dates.invalid");

            log.info("Property availability assert succeeded for property with id={}", propertyId);
            return propertyAvailabilityRepo.isAvailable(propertyId, checkIn, checkOut);
        } catch (EntityNotFoundException e) {
            log.warn("Property availability assert failed: Property with id={} not found", propertyId, e);
            throw e;
        } catch (EntityInvalidArgumentException e) {
            log.warn("Property availability assert failed: Invalid date range (checkIn={}, checkOut={}) for property with id={}", checkIn, checkOut, propertyId, e);
            throw e;
        }
    }

    @Override
    public int deleteBlocksByBookingIds(List<Long> bookingIds) {
        return propertyAvailabilityRepo.deleteByBookingIds(bookingIds);
    }

}
