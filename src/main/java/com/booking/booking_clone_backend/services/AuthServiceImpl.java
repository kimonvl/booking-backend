package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.requests.auth.LoginRequest;
import com.booking.booking_clone_backend.DTOs.requests.auth.RegisterRequest;
import com.booking.booking_clone_backend.DTOs.responses.user.UserDTO;
import com.booking.booking_clone_backend.constants.MessageConstants;
import com.booking.booking_clone_backend.exceptions.*;
import com.booking.booking_clone_backend.mappers.UserMapper;
import com.booking.booking_clone_backend.models.static_data.Country;
import com.booking.booking_clone_backend.models.refresh_token.RefreshToken;
import com.booking.booking_clone_backend.models.user.User;
import com.booking.booking_clone_backend.models.user.UserPrincipal;
import com.booking.booking_clone_backend.repos.CountryRepo;
import com.booking.booking_clone_backend.repos.RefreshTokenRepo;
import com.booking.booking_clone_backend.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl {
    private final UserRepo userRepo;
    private final CountryRepo countryRepo;
    private final UserMapper userMapper;
    private final RefreshTokenRepo refreshRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @Value("${app.jwt.refresh-days}")
    private long refreshDays;
    private final SecureRandom random = new SecureRandom();

    public UserDTO register(RegisterRequest req) {
        try {
            String normalized = req.email().trim().toLowerCase();
            if (userRepo.existsByEmailIgnoreCase(normalized)) {
                throw new EntityAlreadyExistsException("User with email=" + normalized + " already exists");
            }

            Optional<Country> countryOpt = countryRepo.findByCode(req.country());
            if (countryOpt.isEmpty())
                throw new EntityInvalidArgumentException("Country code=" + req.country() + " invalid");

            User u = userMapper.registerRequestToUser(req, normalized, passwordEncoder.encode(req.password()), countryOpt.get());

            log.info("User with email={} registered successfully", normalized);
            return userMapper.toDto(userRepo.save(u));
        } catch (EntityAlreadyExistsException e) {
            log.error("Registration failed for email={}. Email already exists", req.email(), e);
            throw e;
        } catch (EntityInvalidArgumentException e) {
            log.error("Registration failed for email={}. Country code={} invalid", req.email(), req.country(), e);
            throw e;
        }
    }

    public AuthResult login(LoginRequest request) {
        var auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        var principal = (UserPrincipal) auth.getPrincipal();
        assert principal != null;
        User user = principal.user();
        if (!user.getRole().equals(request.role()))
            throw new WrongCredentialsException(MessageConstants.WRONG_EMAIL_OR_PASSWORD);
        String access = jwtService.generateAccessToken(
                principal.getId(),
                principal.getUsername(),
                principal.getRole()
        );

        RefreshToken refresh = issueRefreshToken(principal.getUsername());
        return new AuthResult(access, refresh.getToken(), userMapper.toDto(user));
    }

    public AuthResult refresh(String refreshTokenValue) {
        RefreshToken existing = refreshRepo.findByToken(refreshTokenValue)
                .orElseThrow(() -> new InvalidRefreshTokenException(MessageConstants.INVALID_REFRESH_TOKEN));

        if (existing.isRevoked() || existing.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidRefreshTokenException(MessageConstants.INVALID_REFRESH_TOKEN);
        }

        // rotate refresh token
        existing.setRevoked(true);
        refreshRepo.save(existing);

        User user = existing.getUser();
        String access = jwtService.generateAccessToken(user.getId(), user.getEmail(), user.getRole());
        RefreshToken newRefresh = issueRefreshToken(user.getEmail());

        return new AuthResult(access, newRefresh.getToken(), userMapper.toDto(user));
    }

    public void logout(String refreshTokenValue) {
        refreshRepo.findByToken(refreshTokenValue).ifPresent(rt -> {
            rt.setRevoked(true);
            refreshRepo.save(rt);
        });
    }

    private RefreshToken issueRefreshToken(String email) {
        User user = userRepo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException("User not found for refresh"));

        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = HexFormat.of().formatHex(bytes);

        RefreshToken rt = new RefreshToken();
        rt.setUser(user);
        rt.setToken(token);
        rt.setRevoked(false);
        rt.setExpiresAt(Instant.now().plus(refreshDays, ChronoUnit.DAYS));

        return refreshRepo.save(rt);
    }

    public boolean isUserExists(String email) {
        return userRepo.existsByEmailIgnoreCase(email);
    }

    public record AuthResult(String accessToken, String refreshToken, UserDTO userDTO) {}
}
