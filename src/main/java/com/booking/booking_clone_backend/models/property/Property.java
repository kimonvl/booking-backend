package com.booking.booking_clone_backend.models.property;

import com.booking.booking_clone_backend.models.amenity.PropertyAmenity;
import com.booking.booking_clone_backend.models.language.PropertyLanguage;
import com.booking.booking_clone_backend.models.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PropertyAmenity> propertyAmenities = new HashSet<>();

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PropertyLanguage> propertyLanguages = new HashSet<>();

    @OneToOne(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private PropertyAddress address;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "type", columnDefinition = "property_type_enum", nullable = false)
    private PropertyType type = PropertyType.APARTMENT;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", columnDefinition = "property_status_enum", nullable = false)
    private PropertyStatus status = PropertyStatus.DRAFT;

    @Column(nullable = false, length = 200)
    private String name;

    // Pricing
    @Column(precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "currency", columnDefinition = "currency_code_enum", nullable = false)
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
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "parking_policy", columnDefinition = "parking_policy_enum", nullable = false)
    private ParkingPolicy parkingPolicy = ParkingPolicy.NONE;

    // Rules
    @Column(nullable = false)
    private boolean smokingAllowed = false;

    @Column(nullable = false)
    private boolean partiesAllowed = false;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "pets_policy", columnDefinition = "pets_policy_enum", nullable = false)
    private PetsPolicy petsPolicy = PetsPolicy.NO;

    // Optional check-in/out windows (can be null until filled)
    private LocalTime checkInFrom;
    private LocalTime checkInUntil;
    private LocalTime checkOutFrom;
    private LocalTime checkOutUntil;

    // Sleeping areas: store JSON as text for MVP (works on Postgres).
    // Later you can migrate to jsonb column definition if you want.
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "sleeping_areas_json", columnDefinition = "jsonb")
    private String sleepingAreasJson;
    @Column(nullable = false)
    private int bedroomCount;
    @Column(nullable = false)
    private int bedCount;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}
