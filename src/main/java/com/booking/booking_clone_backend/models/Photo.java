package com.booking.booking_clone_backend.models;

import com.booking.booking_clone_backend.models.property.Property;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(
        name = "photos"
)
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    @Column(unique = true, nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false, length = 600)
    private String url;

    @Column(nullable = false, length = 600)
    private String publicId;

    @Column(nullable = false)
    private int sortOrder = 0;
}
