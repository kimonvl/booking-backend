package com.booking.booking_clone_backend.DTOs.requests.auth;

import com.booking.booking_clone_backend.models.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotNull Role role
) {}

