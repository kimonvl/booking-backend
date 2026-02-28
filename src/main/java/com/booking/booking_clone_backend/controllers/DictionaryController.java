package com.booking.booking_clone_backend.controllers;

import com.booking.booking_clone_backend.DTOs.responses.GenericResponse;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.amenity.AmenitiesDictionaryItemDTO;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.country.CountryDTO;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.language.LanguageDTO;
import com.booking.booking_clone_backend.constants.MessageConstants;
import com.booking.booking_clone_backend.controllers.controller_utils.ResponseFactory;
import com.booking.booking_clone_backend.services.DictionaryService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {
    private final DictionaryService dictionaryService;

    @GetMapping("/getAmenities")
    public ResponseEntity<@NonNull GenericResponse<List<AmenitiesDictionaryItemDTO>>> getAmenitiesDictionary() {
        return new ResponseEntity<>(
                new GenericResponse<>(
                        dictionaryService.getAmenitiesDictionaryGroupByGroupName(),
                        "AmenitiesDictionaryFetched",
                        MessageConstants.AMENITIES_DICTIONARY_FETCHED,
                        true
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/getLanguages")
    public ResponseEntity<@NonNull GenericResponse<List<LanguageDTO>>> getLanguageDictionary() {
        return new ResponseEntity<>(
                new GenericResponse<>(
                        dictionaryService.getLanguageDictionary(),
                        "LanguageDictionaryFetched",
                        MessageConstants.LANGUAGE_DICTIONARY_FETCHED,
                        true
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/getCountries")
    public ResponseEntity<@NonNull GenericResponse<List<CountryDTO>>> getCountryDictionary() {
        return new ResponseEntity<>(
                new GenericResponse<>(
                        dictionaryService.getCountryDictionary(),
                        "CountryDictionaryFetched",
                        MessageConstants.COUNTRY_DICTIONARY_FETCHED,
                        true
                ),
                HttpStatus.OK
        );
    }
}

