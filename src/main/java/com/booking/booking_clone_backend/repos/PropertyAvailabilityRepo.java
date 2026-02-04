package com.booking.booking_clone_backend.repos;

import com.booking.booking_clone_backend.models.availability.PropertyAvailability;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface PropertyAvailabilityRepo extends JpaRepository<@NonNull PropertyAvailability, @NonNull Long> {
    @Query("""
      select (count(pa) = 0)
      from PropertyAvailability pa
      where pa.property.id = :propertyId
        and pa.startDate < :checkOut
        and pa.endDate > :checkIn
    """)
    boolean isAvailable(
            @Param("propertyId") long propertyId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );
}
