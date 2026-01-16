package com.booking.booking_clone_backend.models.refresh_token;

import com.booking.booking_clone_backend.models.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "refresh_tokens",
        indexes = {
                @Index(name = "idx_refresh_token_token", columnList = "token", unique = true),
                @Index(name = "idx_refresh_token_user_id", columnList = "user_id")
        })
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // NOTE: for production you can store a hash instead of raw token string.
    @Column(nullable = false, unique = true, length = 200)
    private String token;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean revoked = false;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();
}
