package com.booking.booking_clone_backend.DTOs.requests.partner.apartment;

import com.booking.booking_clone_backend.DTOs.responses.property.AddressDTO;
import com.booking.booking_clone_backend.models.property.ParkingPolicy;
import com.booking.booking_clone_backend.models.property.PetsPolicy;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.util.List;

public record CreateApartmentRequest(
        String propertyName,
        AddressDTO address,
        SleepingAreasDTO sleepingAreas,
        Integer guestCount,
        Integer bathroomCount,
        Boolean allowChildren,
        Boolean offerCots,
        BigDecimal aptSize,
        List<String> amenities,
        Boolean serveBreakfast,
        ParkingPolicy isParkingAvailable,
        List<String> languages,
        Boolean smokingAllowed,
        Boolean partiesAllowed,
        PetsPolicy petsAllowed,
        String checkInFrom,
        String checkInUntil,
        String checkOutFrom,
        String checkOutUntil,
        Integer photosCount,
        BigDecimal pricePerNight
) {
    @Override
    @NonNull
    public String toString() {
        return "CreateApartmentRequest{" +
                "propertyName='" + propertyName + '\'' +
                ", address=" + address +
                ", sleepingAreas=" + sleepingAreas +
                ", guestCount=" + guestCount +
                ", bathroomCount=" + bathroomCount +
                ", allowChildren='" + allowChildren + '\'' +
                ", offerCots='" + offerCots + '\'' +
                ", aptSize='" + aptSize + '\'' +
                ", amenities=" + (amenities == null ? 0 : amenities.size()) +
                ", serveBreakfast='" + serveBreakfast + '\'' +
                ", isParkingAvailable='" + isParkingAvailable + '\'' +
                ", languages=" + (languages == null ? 0 : languages.size()) +
                ", smokingAllowed=" + smokingAllowed +
                ", partiesAllowed=" + partiesAllowed +
                ", petsAllowed='" + petsAllowed + '\'' +
                ", checkInFrom='" + checkInFrom + '\'' +
                ", checkInUntil='" + checkInUntil + '\'' +
                ", checkOutFrom='" + checkOutFrom + '\'' +
                ", checkOutUntil='" + checkOutUntil + '\'' +
                ", photosCount=" + photosCount +
                ", pricePerNight=" + pricePerNight +
                '}';
    }
}
