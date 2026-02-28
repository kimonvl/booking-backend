package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.requests.auth.LoginRequest;
import com.booking.booking_clone_backend.DTOs.requests.auth.RegisterRequest;
import com.booking.booking_clone_backend.DTOs.responses.user.UserDTO;
import com.booking.booking_clone_backend.exceptions.EntityAlreadyExistsException;
import com.booking.booking_clone_backend.exceptions.EntityInvalidArgumentException;
import com.booking.booking_clone_backend.exceptions.EntityNotFoundException;
import com.booking.booking_clone_backend.exceptions.InternalErrorException;

public interface AuthService {
    UserDTO register(RegisterRequest req) throws EntityAlreadyExistsException, EntityInvalidArgumentException;
    AuthServiceImpl.AuthResult login(LoginRequest request) throws EntityInvalidArgumentException, InternalErrorException, EntityNotFoundException;
    AuthServiceImpl.AuthResult refresh(String refreshTokenValue)
            throws EntityNotFoundException, EntityInvalidArgumentException;
    void logout(String refreshTokenValue) throws EntityNotFoundException;
}
