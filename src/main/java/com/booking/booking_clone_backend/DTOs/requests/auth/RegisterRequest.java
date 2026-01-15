package com.booking.booking_clone_backend.DTOs.requests.auth;

import com.booking.booking_clone_backend.models.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Email @NotBlank String email,
        @NotBlank @Size(min = 6, max = 100) String password,
        @NotNull Role role // "GUEST" or "PARTNER" (MVP)
) {}
