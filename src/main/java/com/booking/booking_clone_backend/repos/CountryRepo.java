package com.booking.booking_clone_backend.repos;

import com.booking.booking_clone_backend.models.static_data.Country;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepo extends JpaRepository<@NonNull Country, @NonNull Long> {
    Optional<Country> findByCode(String code);
}
