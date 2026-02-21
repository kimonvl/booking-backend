package com.booking.booking_clone_backend.repos;

import com.booking.booking_clone_backend.models.static_data.Amenity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmenitiesRepo extends JpaRepository<@NonNull Amenity, @NonNull Long> {
    List<Amenity> findByCodeIn(List<String> codes);
}
