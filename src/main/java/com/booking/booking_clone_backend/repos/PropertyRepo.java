package com.booking.booking_clone_backend.repos;

import com.booking.booking_clone_backend.models.property.Property;
import com.booking.booking_clone_backend.models.user.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepo extends JpaRepository<@NonNull Property, @NonNull Long> {
    List<Property> findByOwner(User owner);

    // Exact match
    List<Property> findByAddress_City(String city);

    // Case-insensitive exact match
    List<Property> findByAddress_CityIgnoreCase(String city);

    // Contains (search)
    List<Property> findByAddress_CityContainingIgnoreCase(String city);
}
