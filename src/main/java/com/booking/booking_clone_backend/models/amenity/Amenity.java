package com.booking.booking_clone_backend.models.amenity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(
        name = "amenities",
        indexes = {
                @Index(name = "idx_amenities_code", columnList = "code", unique = true)
        }
)
public class Amenity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // e.g. "WIFI", "AIR_CONDITIONING"
    @Column(nullable = false, unique = true, length = 80)
    private String code;

    // e.g. "Free WiFi"
    @Column(nullable = false, length = 160)
    private String label;

    // e.g. "General", "Cooking and cleaning"
    @Column(nullable = false, length = 120)
    private String groupName;
}
