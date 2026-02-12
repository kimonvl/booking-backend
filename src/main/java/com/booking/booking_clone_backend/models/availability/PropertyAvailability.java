package com.booking.booking_clone_backend.models.availability;

import com.booking.booking_clone_backend.models.AbstractEntity;
import com.booking.booking_clone_backend.models.property.Property;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(
        name = "property_availability",
        indexes = {
                @Index(name = "idx_availability_property", columnList = "property_id"),
                @Index(name = "idx_availability_dates", columnList = "start_date, end_date")
        }
)
public class PropertyAvailability extends AbstractEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    // Use [startDate, endDate) convention (end exclusive) in your service logic
    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;
}
