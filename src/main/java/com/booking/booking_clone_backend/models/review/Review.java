package com.booking.booking_clone_backend.models.review;

import com.booking.booking_clone_backend.models.AbstractEntity;
import com.booking.booking_clone_backend.models.booking.Booking;
import com.booking.booking_clone_backend.models.property.Property;
import com.booking.booking_clone_backend.models.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(
        name = "reviews",
        indexes = {
                @Index(name = "idx_reviews_property_created", columnList = "property_id, created_at"),
                @Index(name = "idx_reviews_guest_created", columnList = "guest_id, created_at")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_reviews_booking", columnNames = "booking_id")
        }
)
public class Review extends AbstractEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Enforce: one review per booking
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, updatable = false)
    private Booking booking;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false, updatable = false)
    private Property property;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false, updatable = false)
    private User guest;

    @Column(nullable = false)
    private int rating; // decide 1-10 or 1-5

    @Column(name = "positive_comment", length = 1500)
    private String positiveComment;

    @Column(name = "negative_comment", length = 1500)
    private String negativeComment;

    // Owner/partner response (manager reply)
    @Column(name = "owner_response", length = 1500)
    private String ownerResponse;

    @Column(name = "owner_responded_at")
    private Instant ownerRespondedAt;

}
