package com.booking.booking_clone_backend.models.static_data;

import com.booking.booking_clone_backend.models.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(
        name = "languages",
        indexes = {
                @Index(name = "idx_languages_code", columnList = "code", unique = true)
        }
)
public class Language extends AbstractEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // e.g. "en", "el", "fr"
    @Column(nullable = false, unique = true, length = 16)
    private String code;

    // e.g. "English", "Greek"
    @Column(nullable = false, length = 120)
    private String label;
}
