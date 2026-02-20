package com.booking.booking_clone_backend.validators;

import com.booking.booking_clone_backend.DTOs.requests.partner.apartment.CreateApartmentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateApartmentValidator implements Validator {

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return CreateApartmentRequest.class == clazz;
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        CreateApartmentRequest createApartmentRequest = (CreateApartmentRequest) target;

        // check sleeping areas to have at least 1 bed and total capacity based on bed types be equal or greater than max guest count
        if (!errors.hasFieldErrors("sleepingAreas")) {
            AtomicInteger allowedGuestCount = new AtomicInteger();
            int plusCot = 0;
            createApartmentRequest.sleepingAreas().bedrooms().forEach(bedroomDTO -> {
                bedroomDTO.beds()
                        .forEach((bedType, amount) -> allowedGuestCount.set(allowedGuestCount.intValue() + bedType.getCapacity() * amount) );
            });
            createApartmentRequest.sleepingAreas().livingRoom().beds()
                    .forEach((bedType, amount) -> allowedGuestCount.set(allowedGuestCount.intValue() + bedType.getCapacity() * amount));

            if (createApartmentRequest.allowChildren() && createApartmentRequest.offerCots()) {
                allowedGuestCount.set(allowedGuestCount.intValue() + 1);
                plusCot++;
            }
            if (createApartmentRequest.guestCount() > allowedGuestCount.intValue()) {
                errors.rejectValue("guestCount", "sleeping_areas.mismatch");
                log.warn("Apartment creation failed. Max guest count={} exceeds sleeping areas beds' total capacity={} ", createApartmentRequest.guestCount(), allowedGuestCount.intValue());
            } else if (allowedGuestCount.intValue() - plusCot <= 0) {
                errors.rejectValue("sleepingAreas", "sleeping_areas.zero_beds");
                log.warn("Apartment creation failed. 0 beds provided");
            }
        }
        // check if amenities strings are legit amenity codes in db
        // check if there is at least 1 language and check language codes with db
        //check in from is after check out until
    }
}
