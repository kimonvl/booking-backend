package com.booking.booking_clone_backend.controllers;

import com.booking.booking_clone_backend.DTOs.requests.auth.LoginRequest;
import com.booking.booking_clone_backend.DTOs.requests.auth.RegisterRequest;
import com.booking.booking_clone_backend.DTOs.responses.GenericResponse;
import com.booking.booking_clone_backend.DTOs.responses.user.UserDTO;
import com.booking.booking_clone_backend.DTOs.responses.auth.AuthResponse;
import com.booking.booking_clone_backend.constants.MessageConstants;
import com.booking.booking_clone_backend.controllers.controller_utils.ResponseFactory;
import com.booking.booking_clone_backend.exceptions.InvalidRefreshTokenException;
import com.booking.booking_clone_backend.exceptions.MissingRefreshTokenException;
import com.booking.booking_clone_backend.services.AuthServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final String REFRESH_COOKIE = "refresh_token";

    @Autowired
    private AuthServiceImpl authService;

    @Value("${app.cookie.secure}")
    private boolean cookieSecure;

    @Value("${app.cookie.same-site}")
    private String sameSite;

    @Value("${app.jwt.refresh-days}")
    private long refreshDays;

    @PostMapping("/register")
    public ResponseEntity<@NonNull GenericResponse<UserDTO>> register(@Valid @RequestBody RegisterRequest req, HttpServletResponse response) {
        return ResponseFactory.createSuccessResponse(
                authService.register(req.email(),req.password(), req.role()),
                MessageConstants.REGISTERED,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<@NonNull GenericResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest req, HttpServletResponse response) {
        var result = authService.login(req);
        setRefreshCookie(response, result.refreshToken());
        return ResponseFactory.createSuccessResponse(new AuthResponse(result.accessToken(), result.userDTO()), MessageConstants.LOGGED_IN, HttpStatus.ACCEPTED) ;
    }

    /**
     * Frontend calls this with axios { withCredentials: true }
     * We read refresh token from HttpOnly cookie.
     */
    @PostMapping("/refresh")
    public ResponseEntity<@NonNull GenericResponse<AuthResponse>> refresh(
            @CookieValue(name = REFRESH_COOKIE, required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new MissingRefreshTokenException(MessageConstants.MISSING_REFRESH_TOKEN);
        }

        try {
            var result = authService.refresh(refreshToken);
            setRefreshCookie(response, result.refreshToken());
            return ResponseFactory.createSuccessResponse(new AuthResponse(result.accessToken(), result.userDTO()), MessageConstants.TOKEN_REFRESHED, HttpStatus.ACCEPTED);
        } catch (InvalidRefreshTokenException e) {
            clearRefreshCookie(response);
            throw e;
        }
    }

    @PostMapping("/logout")
    public void logout(
            @CookieValue(name = REFRESH_COOKIE, required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            authService.logout(refreshToken);
        }
        clearRefreshCookie(response);
    }

    private void setRefreshCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE, refreshToken)
                .httpOnly(true)
                .secure(cookieSecure) // false on localhost, true in prod
                .path("/auth")        // only sent to /auth/*
                .sameSite(sameSite)   // Lax recommended for same-site SPA dev
                .maxAge(Duration.ofDays(refreshDays))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void clearRefreshCookie(HttpServletResponse response) {
        ResponseCookie cleared = ResponseCookie.from(REFRESH_COOKIE, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/auth")
                .sameSite(sameSite)
                .maxAge(Duration.ZERO)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cleared.toString());
    }
}
