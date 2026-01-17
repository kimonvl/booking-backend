package com.booking.booking_clone_backend.repos;

import com.booking.booking_clone_backend.models.property.Country;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepo extends JpaRepository<@NonNull Country, @NonNull Long> {
}
