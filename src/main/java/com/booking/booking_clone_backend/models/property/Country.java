package com.booking.booking_clone_backend.models.property;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "countries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Country {

    @Id
    @EqualsAndHashCode.Include
    @Column(length = 2)
    private String code; // e.g. "GR"

    @Column(nullable = false, length = 120)
    private String name; // e.g. "Greece"
}
