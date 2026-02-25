package com.booking.booking_clone_backend.validators;

import com.booking.booking_clone_backend.DTOs.requests.partner.apartment.CreateApartmentRequest;
import com.booking.booking_clone_backend.services.DictionaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateApartmentValidator implements Validator {

    private final DictionaryService dictionaryService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return CreateApartmentRequest.class == clazz;
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        CreateApartmentRequest createApartmentRequest = (CreateApartmentRequest) target;

        // check sleeping areas to have at least 1 bed and total capacity based on bed types be equal or greater than max guest count
        validateSleepingAreasAndGuestCount(errors, createApartmentRequest);
        // check if there is at least 1 amenity check if amenities strings are legit amenity codes in db
        validateAmenities(errors, createApartmentRequest.amenities());
        // check if there is at least 1 language and check language codes with db
        validateLanguages(errors, createApartmentRequest.languages());
        //check in from is after check out until
        validateCheckInOutTimes(
                errors,
                createApartmentRequest.checkInFrom(),
                createApartmentRequest.checkInUntil(),
                createApartmentRequest.checkOutFrom(),
                createApartmentRequest.checkOutUntil()
                );

    }

    private void validateCheckInOutTimes(
            Errors errors,
            LocalTime checkInFrom,
            LocalTime checkInUntil,
            LocalTime checkOutFrom,
            LocalTime checkOutUntil
    ) {
        if (errors.hasFieldErrors("checkInFrom") ||
                errors.hasFieldErrors("checkInUntil") ||
                errors.hasFieldErrors("checkOutFrom") ||
                errors.hasFieldErrors("checkOutUntil") ||
                checkInFrom == null || checkInUntil == null || checkOutFrom == null || checkOutUntil == null) {
            return;
        }

        if (checkInFrom.isAfter(checkInUntil)) {
            errors.rejectValue("checkInFrom", "checkInFrom.invalid_range");
            log.warn("Apartment creation failed. CheckInFrom={} is after CheckInUntil={}", checkInFrom, checkInUntil);
        }

        if (checkOutFrom.isAfter(checkOutUntil)) {
            errors.rejectValue("checkOutFrom", "checkOutFrom.invalid_range");
            log.warn("Apartment creation failed. CheckOutFrom={} is after CheckOutUntil={}", checkOutFrom, checkOutUntil);
        }

        if (checkOutUntil.isAfter(checkInFrom)) {
            errors.rejectValue("checkOutUntil", "checkOutUntil.invalid_range");
            log.warn("Apartment creation failed. CheckOutUntil={} is after CheckInFrom={}", checkOutUntil, checkInFrom);
        }
    }

    private void validateLanguages(Errors errors, List<String> codes) {
        if (!errors.hasFieldErrors("languages")) {
            List<String> normalized = ValidatorUtils.getNormalisedStrings(codes);
            if (normalized.isEmpty()) {
                errors.rejectValue("languages", "languages.empty");
                log.warn("Apartment creation failed. 0 languages provided");
                return;
            }
            List<String> incorrectLanguageCodes = dictionaryService.findIncorrectLanguageCodes(normalized);
            if (!incorrectLanguageCodes.isEmpty()) {
                errors.rejectValue(
                        "languages",
                        "languages.incorrect_codes",
                        new Object[]{incorrectLanguageCodes},
                        null
                );
                String incorrectCodes = String.join(", ", incorrectLanguageCodes);
                log.warn("Apartment creation failed. Incorrect language codes given: {}", incorrectCodes);
            }
        }
    }

    private void validateAmenities(Errors errors, List<String> amenities) {
        if (!errors.hasFieldErrors("amenities")) {
            List<String> normalized = ValidatorUtils.getNormalisedStrings(amenities);
            if (normalized.isEmpty()) {
                errors.rejectValue("amenities", "amenities.empty");
                log.warn("Apartment creation failed. 0 amenities provided");
                return;
            }
            List<String> incorrectAmenityCodes = dictionaryService.findIncorrectAmenityCodes(normalized);
            if (!incorrectAmenityCodes.isEmpty()) {
                errors.rejectValue(
                        "amenities",
                        "amenities.incorrect_codes",
                        new Object[]{incorrectAmenityCodes},
                        null
                );
                String incorrectCodes = String.join(", ", incorrectAmenityCodes);
               log.warn("Apartment creation failed. Incorrect amenity codes given: {}", incorrectCodes);
            }
        }
    }

    private static void validateSleepingAreasAndGuestCount(Errors errors, CreateApartmentRequest createApartmentRequest) {
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
    }


}
