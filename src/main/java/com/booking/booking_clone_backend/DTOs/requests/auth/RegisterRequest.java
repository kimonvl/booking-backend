package com.booking.booking_clone_backend.DTOs.requests.auth;

import com.booking.booking_clone_backend.models.user.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Email @NotBlank String email,
        @NotBlank @Size(min = 6, max = 100) String password,
        @NotNull Long roleId, // "GUEST" or "PARTNER" (MVP)
        @NotBlank @Size(min = 2, max = 50) String firstName,
        @NotBlank @Size(min = 2, max = 50) String lastName,
        @NotBlank @Size(min = 2, max = 2) String country // Code
) {}
