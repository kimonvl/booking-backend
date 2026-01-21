package com.booking.booking_clone_backend.controllers.partner;

import com.booking.booking_clone_backend.DTOs.requests.apartment.CreateApartmentRequest;
import com.booking.booking_clone_backend.DTOs.responses.GenericResponse;
import com.booking.booking_clone_backend.models.property.Property;
import com.booking.booking_clone_backend.models.user.UserPrincipal;
import com.booking.booking_clone_backend.services.PartnerApartmentService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/partner/apartment")
public class PartnerApartmentController {
    @Autowired
    private PartnerApartmentService apartmentService;

    @PostMapping(value = "/addApartment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull GenericResponse<Property>> addApartment(
            @RequestPart(value = "data") CreateApartmentRequest req,
            @RequestPart(value = "photos") List<MultipartFile> photos,
            @RequestPart(value = "mainIndex") String mainIndex,
            @AuthenticationPrincipal UserPrincipal principal
            ) throws Exception {

        apartmentService.addApartment(req, photos, Integer.valueOf(mainIndex), principal.user());
        return ResponseEntity.ok(new GenericResponse<>(null, "OK", true));
    }
}
