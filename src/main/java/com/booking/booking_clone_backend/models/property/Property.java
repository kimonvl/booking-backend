package com.booking.booking_clone_backend.models.property;

import com.booking.booking_clone_backend.models.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(
        name = "properties",
        indexes = {
                @Index(name = "idx_properties_owner", columnList = "owner_id"),
                @Index(name = "idx_properties_status", columnList = "status"),
                @Index(name = "idx_properties_type", columnList = "type")
        }
)
public class Property {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Partner that owns the property
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private PropertyType type = PropertyType.APARTMENT;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private PropertyStatus status = PropertyStatus.DRAFT;

    @Column(nullable = false, length = 200)
    private String name;

    // Pricing
    @Column(precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private CurrencyCode currency = CurrencyCode.EUR;

    // Capacity / details
    @Column
    private Integer maxGuests;

    @Column
    private Integer bathrooms;

    @Column(precision = 10, scale = 2)
    private BigDecimal sizeSqm;

    @Column(nullable = false)
    private boolean childrenAllowed = true;

    @Column(nullable = false)
    private boolean cotsOffered = false;

    // Services
    @Column(nullable = false)
    private boolean breakfastServed = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private ParkingPolicy parkingPolicy = ParkingPolicy.NONE;

    // Rules
    @Column(nullable = false)
    private boolean smokingAllowed = false;

    @Column(nullable = false)
    private boolean partiesAllowed = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private PetsPolicy petsPolicy = PetsPolicy.NO;

    // Optional check-in/out windows (can be null until filled)
    private LocalTime checkInFrom;
    private LocalTime checkInUntil;
    private LocalTime checkOutFrom;
    private LocalTime checkOutUntil;

    // Sleeping areas: store JSON as text for MVP (works on Postgres).
    // Later you can migrate to jsonb column definition if you want.
    @Lob
    @Column(columnDefinition="jsonb")
    private String sleepingAreasJson;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}
