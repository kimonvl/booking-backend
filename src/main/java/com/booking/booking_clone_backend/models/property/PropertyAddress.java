package com.booking.booking_clone_backend.models.property;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(
        name = "property_addresses",
        indexes = {
                @Index(name = "idx_property_addresses_city", columnList = "city"),
                @Index(name = "idx_property_addresses_country", columnList = "country_code"), // ✅ matches SQL
                @Index(name = "idx_property_addresses_postcode", columnList = "postcode")
        }
)
public class PropertyAddress {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "property_id")
    private long propertyId;

    @MapsId
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    // FK property_addresses.country_code -> countries.code
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_code", referencedColumnName = "code", nullable = false)
    private Country country;

    @Column(nullable = false, length = 120)
    private String city;

    @Column(length = 32)
    private String postcode;

    @Column(nullable = false, length = 200)
    private String street;

    @Column(name = "street_number", length = 32)
    private String streetNumber;

    private Double lat;
    private Double lng;
}
