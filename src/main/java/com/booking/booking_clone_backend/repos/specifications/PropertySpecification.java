package com.booking.booking_clone_backend.repos.specifications;

import com.booking.booking_clone_backend.models.booking.Booking;
import com.booking.booking_clone_backend.models.booking.BookingStatus;
import com.booking.booking_clone_backend.models.property.PetsPolicy;
import com.booking.booking_clone_backend.models.property.Property;
import com.booking.booking_clone_backend.models.property.PropertyStatus;
import jakarta.persistence.criteria.Subquery;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PropertySpecification {
    public static Specification<@NonNull Property> isPublished() {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), PropertyStatus.PUBLISHED));
    }

    public static Specification<@NonNull Property> allowPets(Boolean pets) {
        return ((root, query, criteriaBuilder) -> {
            if (pets == null || !pets)
                return criteriaBuilder.conjunction();
            return root.get("petsPolicy").in(PetsPolicy.YES, PetsPolicy.UPON_REQUEST);
        });
    }

    public static Specification<@NonNull Property> cityEqualsIgnoreCase(String city) {
        return ((root, query, criteriaBuilder) -> {
            if (city == null || city.isBlank())
                return criteriaBuilder.conjunction();
            var address = root.join("address");
            return criteriaBuilder.equal(criteriaBuilder.lower(address.get("city")), city.trim().toLowerCase());
        });
    }

    public static Specification<@NonNull Property> guestsAtLeast(Integer guestsMin) {
        return ((root, query, criteriaBuilder) -> {
            if (guestsMin == null)
                return criteriaBuilder.conjunction();
            return criteriaBuilder.greaterThanOrEqualTo(root.get("maxGuests"), guestsMin);
        });
    }

    public static Specification<@NonNull Property> bedroomsAtLeast(Integer bedroomsMin) {
        return ((root, query, criteriaBuilder) -> {
            if (bedroomsMin == null)
                return criteriaBuilder.conjunction();
            return criteriaBuilder.greaterThanOrEqualTo(root.get("bedroomCount"), bedroomsMin);
        });
    }

    public static Specification<@NonNull Property> bathroomsAtLeast(Integer bathroomsMin) {
        return ((root, query, criteriaBuilder) -> {
            if (bathroomsMin == null)
                return criteriaBuilder.conjunction();
            return criteriaBuilder.greaterThanOrEqualTo(root.get("bathrooms"), bathroomsMin);
        });
    }

    public static Specification<@NonNull Property> priceBetween(BigDecimal min, BigDecimal max) {
        return ((root, query, criteriaBuilder) -> {
            if (min == null && max == null)
                return criteriaBuilder.conjunction();
            if (min != null && max != null)
                return criteriaBuilder.between(root.get("pricePerNight"), min, max);
            if (min != null)
                return criteriaBuilder.greaterThanOrEqualTo(root.get("pricePerNight"), min);
            return criteriaBuilder.lessThanOrEqualTo(root.get("pricePerNight"), max);
        });
    }

    /**
     * Availability filter using Booking table:
     *
     * We want properties that have NO overlapping confirmed booking in [checkIn, checkOut).
     * Overlap condition:
     * booking.checkInDate < checkOut AND booking.checkOutDate > checkIn
     *
     * Implemented as NOT EXISTS subquery.
     */
    public static Specification<@NonNull Property> availableBetween(LocalDate checkIn, LocalDate checkOut) {
        return (root, query, cb) -> {
            if (checkIn == null || checkOut == null) return cb.conjunction();

            // NOT EXISTS (select 1 from Booking b where b.property = root and overlap and status confirmed)
            Subquery<Long> sq = query.subquery(Long.class);
            var b = sq.from(Booking.class);

            sq.select(cb.literal(1L));

            var sameProperty = cb.equal(b.get("property"), root);

            var confirmed = cb.equal(b.get("status"), BookingStatus.CONFIRMED);

            var overlap =
                    cb.and(
                            cb.lessThan(b.get("checkInDate"), checkOut),
                            cb.greaterThan(b.get("checkOutDate"), checkIn)
                    );

            sq.where(cb.and(sameProperty, confirmed, overlap));

            return cb.not(cb.exists(sq));
        };
    }

    /**
     * For each property row in the outer query, it checks:
     *
     * Count distinct amenity codes for that property that are in the selected list
     *
     * Must equal the size of the selected list
     *
     * So if user selects [WIFI, KITCHEN, BALCONY], the property must have all 3.
     *
     * */

    public static Specification<@NonNull Property> hasAllAmenities(List<String> codes) {
        return (root, query, cb) -> {
            if (codes == null || codes.isEmpty()) return cb.conjunction();

            // IMPORTANT: if codes has duplicates, the count comparison breaks
            List<String> distinctCodes = codes.stream().distinct().toList();

            var sq = query.subquery(Long.class);
            var p2 = sq.from(Property.class);
            var pa = p2.join("propertyAmenities");
            var a = pa.join("amenity");

            sq.select(cb.countDistinct(a.get("code")));

            sq.where(
                    cb.equal(p2.get("id"), root.get("id")),
                    a.get("code").in(distinctCodes)
            );

            // property matches all selected codes if countDistinct == number of selected codes
            return cb.equal(sq, (long) distinctCodes.size());
        };
    }



}
