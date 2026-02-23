package com.booking.booking_clone_backend.controllers.guest;

import com.booking.booking_clone_backend.DTOs.requests.guest.property.PropertySearchRequest;
import com.booking.booking_clone_backend.DTOs.responses.GenericResponse;
import com.booking.booking_clone_backend.DTOs.responses.property.PropertyDetailsDTO;
import com.booking.booking_clone_backend.DTOs.responses.property.PropertyShortDTO;
import com.booking.booking_clone_backend.constants.MessageConstants;
import com.booking.booking_clone_backend.controllers.controller_utils.ResponseFactory;
import com.booking.booking_clone_backend.services.GuestApartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/guest/properties")
public class GuestPropertiesController {

    private final GuestApartmentService guestApartmentService;

    @PostMapping("/search")
    public ResponseEntity<@NonNull GenericResponse<Page<@NonNull PropertyShortDTO>>> search(@Valid @RequestBody PropertySearchRequest request) {

        return ResponseFactory.createResponse(
                guestApartmentService.search(request),
                MessageConstants.PROPERTIES_FETCHED,
                HttpStatus.OK,
                true
        );
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
