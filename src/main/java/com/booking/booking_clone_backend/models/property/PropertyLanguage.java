package com.booking.booking_clone_backend.models.property;

import com.booking.booking_clone_backend.models.AbstractEntity;
import com.booking.booking_clone_backend.models.static_data.Language;
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
        name = "property_languages",
        indexes = {
                @Index(name = "idx_property_languages_property", columnList = "property_id"),
                @Index(name = "idx_property_languages_language", columnList = "language_id")
        }
)
public class PropertyLanguage extends AbstractEntity {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private PropertyLanguageId id;

    @MapsId("propertyId")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @MapsId("languageId")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class PropertyLanguageId implements Serializable {
        private long propertyId;
        private long languageId;
    }
}
