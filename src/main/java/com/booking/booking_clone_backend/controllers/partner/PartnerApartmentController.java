package com.booking.booking_clone_backend.controllers.partner;

import com.booking.booking_clone_backend.DTOs.requests.partner.apartment.CreateApartmentRequest;
import com.booking.booking_clone_backend.DTOs.responses.GenericResponse;
import com.booking.booking_clone_backend.models.property.Property;
import com.booking.booking_clone_backend.models.user.UserPrincipal;
import com.booking.booking_clone_backend.services.PartnerApartmentService;
import com.booking.booking_clone_backend.validators.CreateApartmentValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/partner/apartment")
@RequiredArgsConstructor
public class PartnerApartmentController {

    private final PartnerApartmentService apartmentService;
    private final CreateApartmentValidator createApartmentValidator;
    private final MessageSource messageSource;

    @PostMapping(value = "/addApartment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull GenericResponse<Property>> addApartment(
            @Valid @RequestPart(value = "data") CreateApartmentRequest req,
            BindingResult bindingResult,
            @RequestPart(value = "photos") List<MultipartFile> photos,
            @RequestPart(value = "mainIndex") String mainIndex,
            @AuthenticationPrincipal UserPrincipal principal,
            Locale locale
            ) {

        createApartmentValidator.validate(req, bindingResult);
        if (bindingResult.hasErrors()) {
            AtomicReference<String> msg = new AtomicReference<>();
            bindingResult.getFieldErrors().forEach(fieldError ->
            {
                msg.set(messageSource.getMessage(fieldError, locale));
                System.out.println(msg.get());
            }
           );
        }

        apartmentService.addApartment(req, photos, Integer.valueOf(mainIndex), principal.user());
        return ResponseEntity.ok(new GenericResponse<>(null, "OK", true));
    }
}
