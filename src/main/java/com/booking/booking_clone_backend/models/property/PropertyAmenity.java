package com.booking.booking_clone_backend.models.property;

import com.booking.booking_clone_backend.models.AbstractEntity;
import com.booking.booking_clone_backend.models.static_data.Amenity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(
        name = "property_amenities",
        indexes = @Index(name = "idx_property_amenities_property", columnList = "property_id")
)
public class PropertyAmenity extends AbstractEntity {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private PropertyAmenityId id;

    @MapsId("propertyId")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @MapsId("amenityId")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "amenity_id", nullable = false)
    private Amenity amenity;

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class PropertyAmenityId implements Serializable {
        private long propertyId;
        private long amenityId;
    }
}
