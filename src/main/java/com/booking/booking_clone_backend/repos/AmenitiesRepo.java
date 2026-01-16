package com.booking.booking_clone_backend.repos;

import com.booking.booking_clone_backend.models.amenity.Amenity;
import com.booking.booking_clone_backend.models.amenity.AmenityGroup;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmenitiesRepo extends JpaRepository<@NonNull Amenity, @NonNull Long> {
    List<Amenity> findByGroupName(AmenityGroup groupName);
}
