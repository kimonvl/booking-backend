package com.booking.booking_clone_backend.DTOs.responses.auth;

import com.booking.booking_clone_backend.DTOs.responses.user.UserDTO;

public record AuthResponse(String accessToken, UserDTO user) {}
