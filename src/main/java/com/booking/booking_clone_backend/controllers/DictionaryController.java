package com.booking.booking_clone_backend.controllers;

import com.booking.booking_clone_backend.DTOs.responses.GenericResponse;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.amenity.AmenitiesDictionaryItemDTO;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.country.CountryDTO;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.language.LanguageDTO;
import com.booking.booking_clone_backend.constants.MessageConstants;
import com.booking.booking_clone_backend.controllers.controller_utils.ResponseFactory;
import com.booking.booking_clone_backend.services.DictionaryService;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dictionary")
public class DictionaryController {
    @Autowired
    DictionaryService dictionaryService;

    @GetMapping("/getAmenities")
    public ResponseEntity<@NonNull GenericResponse<List<AmenitiesDictionaryItemDTO>>> getAmenitiesDictionary(HttpServletResponse response) {
        return ResponseFactory.createSuccessResponse(
                dictionaryService.getAmenitiesDictionary(),
                MessageConstants.AMENITIES_DICTIONARY_FETCHED,
                HttpStatus.ACCEPTED
        );
    }

    @GetMapping("/getLanguages")
    public ResponseEntity<@NonNull GenericResponse<List<LanguageDTO>>> getLanguageDictionary(HttpServletResponse response) {
        return ResponseFactory.createSuccessResponse(
                dictionaryService.getLanguageDictionary(),
                MessageConstants.LANGUAGE_DICTIONARY_FETCHED,
                HttpStatus.ACCEPTED
        );
    }

    @GetMapping("/getCountries")
    public ResponseEntity<@NonNull GenericResponse<List<CountryDTO>>> getCountryDictionary(HttpServletResponse response) {
        return ResponseFactory.createSuccessResponse(
                dictionaryService.getCountryDictionary(),
                MessageConstants.COUNTRY_DICTIONARY_FETCHED,
                HttpStatus.ACCEPTED
        );
    }

}

