package com.booking.booking_clone_backend.controllers.partner;

import com.booking.booking_clone_backend.DTOs.requests.partner.apartment.CreatePropertyRequest;
import com.booking.booking_clone_backend.DTOs.responses.GenericResponse;
import com.booking.booking_clone_backend.constants.MessageConstants;
import com.booking.booking_clone_backend.exceptions.EntityInvalidArgumentException;
import com.booking.booking_clone_backend.exceptions.FileUploadException;
import com.booking.booking_clone_backend.exceptions.InternalErrorException;
import com.booking.booking_clone_backend.exceptions.ValidationException;
import com.booking.booking_clone_backend.models.user.UserPrincipal;
import com.booking.booking_clone_backend.services.PartnerPropertyService;
import com.booking.booking_clone_backend.validators.CreateApartmentValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;
import java.util.Map;

// TODO change endpoint to /partner/properties and add update endpoint
@RestController
@RequestMapping("/partner/apartment")
@RequiredArgsConstructor
public class PartnerPropertyController {

    private final PartnerPropertyService apartmentService;
    private final CreateApartmentValidator createApartmentValidator;
    private final MessageSource messageSource;

    @PostMapping(value = "/addApartment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull GenericResponse<?>> addApartment(
            @Valid @RequestPart(value = "data") CreatePropertyRequest req,
            BindingResult bindingResult,
            @RequestPart(value = "photos") List<MultipartFile> photos,
            @RequestPart(value = "mainIndex") String mainIndex,
            @AuthenticationPrincipal UserPrincipal principal
            ) throws ValidationException, EntityInvalidArgumentException, InternalErrorException, FileUploadException {

        createApartmentValidator.validate(req, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationException("CreatePropertyRequest", "Invalid property data", bindingResult);
        }

        apartmentService.createProperty(req, photos, Integer.valueOf(mainIndex), principal.user());
        return ResponseEntity.ok(new GenericResponse<>(null, "CreatePropertySucceeded", MessageConstants.PROPERTY_CREATED, true));
    }
}
