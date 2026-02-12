package com.booking.booking_clone_backend.models.booking;

import com.booking.booking_clone_backend.models.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "booking_checkout_details")
public class BookingCheckoutDetails extends AbstractEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "booking_id")
    private long bookingId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "traveling_for_work", nullable = false)
    private boolean travelingForWork = false;

    @Column(name = "title", length = 20)
    private String title; // "Mr", "Ms", "Mrs" (optional)

    @Column(name = "first_name", nullable = false, length = 120)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 120)
    private String lastName;

    @Column(name = "contact_email", nullable = false, length = 320)
    private String contactEmail;

    @Column(name = "phone_country_code", length = 10)
    private String phoneCountryCode;

    @Column(name = "phone_number", length = 40)
    private String phoneNumber;

    @Column(name = "special_request", length = 1000)
    private String specialRequest;
}
