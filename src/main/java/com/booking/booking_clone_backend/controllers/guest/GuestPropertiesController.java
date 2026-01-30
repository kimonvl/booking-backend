package com.booking.booking_clone_backend.controllers.guest;

import com.booking.booking_clone_backend.DTOs.requests.guest.property.PropertySearchRequest;
import com.booking.booking_clone_backend.DTOs.responses.GenericResponse;
import com.booking.booking_clone_backend.DTOs.responses.property.PropertyShortDTO;
import com.booking.booking_clone_backend.constants.MessageConstants;
import com.booking.booking_clone_backend.controllers.controller_utils.ResponseFactory;
import com.booking.booking_clone_backend.services.GuestApartmentService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guest/properties")
public class GuestPropertiesController {
    @Autowired
    GuestApartmentService guestApartmentService;

    @GetMapping("/getPropertiesByCity")
    public ResponseEntity<@NonNull GenericResponse<List<PropertyShortDTO>>> getPropertiesByCity(@RequestParam("city") String city) {

        return ResponseFactory.createSuccessResponse(guestApartmentService.getPropertiesByCity(city), MessageConstants.PROPERTIES_FETCHED, HttpStatus.OK) ;
    }

    @PostMapping("/search")
    public ResponseEntity<@NonNull GenericResponse<Page<@NonNull PropertyShortDTO>>> search(@RequestBody PropertySearchRequest request) {

        return ResponseFactory.createSuccessResponse(guestApartmentService.search(request), MessageConstants.PROPERTIES_FETCHED, HttpStatus.OK) ;
    }
}
