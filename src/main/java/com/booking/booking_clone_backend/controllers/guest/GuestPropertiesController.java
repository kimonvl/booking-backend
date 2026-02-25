package com.booking.booking_clone_backend.controllers.guest;

import com.booking.booking_clone_backend.DTOs.requests.guest.property.PropertySearchRequest;
import com.booking.booking_clone_backend.DTOs.responses.GenericResponse;
import com.booking.booking_clone_backend.DTOs.responses.property.PropertyDetailsDTO;
import com.booking.booking_clone_backend.DTOs.responses.property.PropertyShortDTO;
import com.booking.booking_clone_backend.constants.MessageConstants;
import com.booking.booking_clone_backend.controllers.controller_utils.ResponseFactory;
import com.booking.booking_clone_backend.services.GuestApartmentService;
import com.booking.booking_clone_backend.validators.SearchFiltersValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/guest/properties")
public class GuestPropertiesController {

    private final GuestApartmentService guestApartmentService;
    private final SearchFiltersValidator searchFiltersValidator;
    private final MessageSource messageSource;

    @PostMapping("/search")
    public ResponseEntity<@NonNull GenericResponse<?>> search(
            @Valid @RequestBody PropertySearchRequest request,
            BindingResult bindingResult,
            Locale locale
    ) {
        searchFiltersValidator.validate(request, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = bindingResult.getFieldErrors()
                    .stream()
                    .collect(java.util.stream.Collectors.toMap(
                            org.springframework.validation.FieldError::getField,
                            fe -> messageSource.getMessage(fe, locale),
                            (msg1, msg2) -> msg1 // keep first if multiple
                    ));
            return new ResponseEntity<>(
                    new GenericResponse<>(
                            fieldErrors,
                            messageSource.getMessage("guest_property.search.failed", null, "Failed to search properties", locale),
                            false
                    ),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                new GenericResponse<>(
                        guestApartmentService.search(request),
                        messageSource.getMessage("guest_property.search.succeeded", null, "Property search was successful", locale),
                        true
                        ),
                HttpStatus.OK);
    }

    @GetMapping("/getPropertyDetails/{propertyId}")
    public ResponseEntity<@NonNull GenericResponse<@NonNull PropertyDetailsDTO>> getPropertyDetails(@PathVariable Long propertyId) {

        return ResponseFactory.createResponse(
                guestApartmentService.getPropertyDetails(propertyId),
                MessageConstants.PROPERTIES_FETCHED,
                HttpStatus.OK,
                true
        ) ;
    }
}
