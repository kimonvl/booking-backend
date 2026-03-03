package com.booking.booking_clone_backend.models.property;

import com.booking.booking_clone_backend.models.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(
        name = "property_photos",
        indexes = {
                @Index(name = "idx_property_photos_property", columnList = "property_id"),
                @Index(name = "idx_property_photos_sort", columnList = "property_id, sortOrder")
        }
)
public class PropertyPhoto extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    @Column(unique = true, nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID uuid = UUID.randomUUID();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(nullable = false, length = 600)
    private String url;

    @Column(nullable = false, length = 600)
    private String publicId;

    @Column(nullable = false)
    private int sortOrder = 0;
}
