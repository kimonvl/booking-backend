package com.booking.booking_clone_backend.models.property;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(
        name = "property_photos",
        indexes = {
                @Index(name = "idx_property_photos_property", columnList = "property_id"),
                @Index(name = "idx_property_photos_sort", columnList = "property_id, sortOrder")
        }
)
public class PropertyPhoto {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(nullable = false, length = 600)
    private String url;

    @Column(nullable = false, length = 600)
    private String publicId;

    @Column(nullable = false)
    private int sortOrder = 0;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();
}
