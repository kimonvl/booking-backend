package com.booking.booking_clone_backend.DTOs.responses.user;

import com.booking.booking_clone_backend.models.user.Role;
import com.booking.booking_clone_backend.models.user.User;

/**
 * Data transfer object representing a {@link User}.
 *
 * <p>This DTO is returned by user-related endpoints and contains the user exposed details.</p>
 * */
public record UserDTO (
    // Unique identifier of the user
    long id,
    // Email that the user registered with
    String email,
    //Guest or Partner
    Role role,
    String firstName,
    String lastName,
    String country
){}
