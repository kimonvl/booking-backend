package com.booking.booking_clone_backend.models.attachment;

import com.booking.booking_clone_backend.models.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public abstract class AbstractAttachment extends AbstractEntity {

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
}
