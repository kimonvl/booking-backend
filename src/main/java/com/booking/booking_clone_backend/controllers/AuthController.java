package com.booking.booking_clone_backend.controllers;

import com.booking.booking_clone_backend.DTOs.requests.auth.LoginRequest;
import com.booking.booking_clone_backend.DTOs.requests.auth.RegisterRequest;
import com.booking.booking_clone_backend.DTOs.responses.GenericResponse;
import com.booking.booking_clone_backend.DTOs.responses.auth.AuthResponse;
import com.booking.booking_clone_backend.constants.MessageConstants;
import com.booking.booking_clone_backend.controllers.controller_utils.ResponseFactory;
import com.booking.booking_clone_backend.exceptions.EntityInvalidArgumentException;
import com.booking.booking_clone_backend.services.AuthServiceImpl;
import com.booking.booking_clone_backend.validators.RegisterValidator;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final String REFRESH_COOKIE = "refresh_token";

    private final AuthServiceImpl authService;
    private final RegisterValidator registerValidator;
    private final MessageSource messageSource;

    @Value("${app.cookie.secure}")
    private boolean cookieSecure;

    @Value("${app.cookie.same-site}")
    private String sameSite;

    @Value("${app.jwt.refresh-days}")
    private long refreshDays;

    @PostMapping("/register")
    public ResponseEntity<@NonNull GenericResponse<?>> register(
            @Valid @RequestBody RegisterRequest req,
            BindingResult bindingResult,
            Locale locale
    ) {
        registerValidator.validate(req, bindingResult);

        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = bindingResult.getFieldErrors()
                    .stream()
                    .collect(java.util.stream.Collectors.toMap(
                            org.springframework.validation.FieldError::getField,
                            fe -> messageSource.getMessage(fe, locale),
                            (msg1, msg2) -> msg1 // keep first if multiple
                    ));
            return new ResponseEntity<>(new GenericResponse<>(fieldErrors, "Registration failed", false), HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity<>(new GenericResponse<>(authService.register(req),MessageConstants.REGISTERED, true), HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<@NonNull GenericResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest req, HttpServletResponse response) {
        var result = authService.login(req);
        setRefreshCookie(response, result.refreshToken());
        return ResponseFactory.createResponse(new AuthResponse(result.accessToken(), result.userDTO()), MessageConstants.LOGGED_IN, HttpStatus.ACCEPTED, true) ;
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
            throw new EntityInvalidArgumentException("Refresh failed: Refresh token is missing");
        }

        try {
            var result = authService.refresh(refreshToken);
            setRefreshCookie(response, result.refreshToken());
            return ResponseFactory.createResponse(new AuthResponse(result.accessToken(), result.userDTO()), MessageConstants.TOKEN_REFRESHED, HttpStatus.ACCEPTED, true);
        } catch (Exception e) {
            clearRefreshCookie(response);
            throw e;
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<@NonNull GenericResponse<Object>> logout(
            @CookieValue(name = REFRESH_COOKIE, required = false) String refreshToken,
            HttpServletResponse response
    ) {
        try {
            if (refreshToken != null && !refreshToken.isBlank()) {
                authService.logout(refreshToken);
            }
            clearRefreshCookie(response);
            return ResponseFactory.createResponse(null, MessageConstants.LOGGED_OUT, HttpStatus.NO_CONTENT, true);
        } catch (Exception e) {
            clearRefreshCookie(response);
            throw e;
        }
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
