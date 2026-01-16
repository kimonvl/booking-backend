package com.booking.booking_clone_backend.repos;

import com.booking.booking_clone_backend.models.refresh_token.RefreshToken;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<@NonNull RefreshToken, @NonNull Long> {
    Optional<RefreshToken> findByToken(String token);
}
