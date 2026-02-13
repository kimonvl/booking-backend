package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.requests.auth.LoginRequest;
import com.booking.booking_clone_backend.DTOs.requests.auth.RegisterRequest;
import com.booking.booking_clone_backend.DTOs.responses.user.UserDTO;
import com.booking.booking_clone_backend.exceptions.EntityInvalidArgumentException;
import com.booking.booking_clone_backend.exceptions.EntityNotFoundException;

public interface AuthService {
    UserDTO register(RegisterRequest req);
    AuthServiceImpl.AuthResult login(LoginRequest request);
    AuthServiceImpl.AuthResult refresh(String refreshTokenValue)
            throws EntityNotFoundException, EntityInvalidArgumentException;
    void logout(String refreshTokenValue) throws EntityNotFoundException;
}
