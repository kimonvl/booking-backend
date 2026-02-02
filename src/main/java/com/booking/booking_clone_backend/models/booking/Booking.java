package com.booking.booking_clone_backend.models.booking;

import com.booking.booking_clone_backend.models.property.Property;
import com.booking.booking_clone_backend.models.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(
        name = "bookings",
        indexes = {
                @Index(name = "idx_bookings_property_checkin", columnList = "property_id, check_in_date"),
                @Index(name = "idx_bookings_property_checkout", columnList = "property_id, check_out_date"),
                @Index(name = "idx_bookings_guest", columnList = "guest_id"),
                @Index(name = "idx_bookings_status", columnList = "status")
        }
)
public class Booking {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    private User guest;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "guest_count", nullable = false)
    private int guestCount = 1;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", columnDefinition = "booking_status_enum", nullable = false)
    private BookingStatus status = BookingStatus.CONFIRMED;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private BookingCheckoutDetails checkoutDetails;


    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
    }
}
