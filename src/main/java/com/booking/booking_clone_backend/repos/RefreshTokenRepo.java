package com.booking.booking_clone_backend.repos;

import com.booking.booking_clone_backend.models.refresh_token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
}
