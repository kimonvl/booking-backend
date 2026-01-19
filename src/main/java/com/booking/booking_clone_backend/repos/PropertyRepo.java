package com.booking.booking_clone_backend.repos;

import com.booking.booking_clone_backend.models.property.Property;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepo extends JpaRepository<@NonNull Property, @NonNull Long> {
}
