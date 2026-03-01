package com.booking.booking_clone_backend.controllers;

import com.booking.booking_clone_backend.DTOs.responses.GenericResponse;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.amenity.AmenitiesDictionaryItemDTO;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.country.CountryDTO;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.language.LanguageDTO;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.role.RoleDTO;
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
@RequestMapping("/dictionaries")
public class DictionaryController {
    private final DictionaryService dictionaryService;

    @GetMapping("/amenities")
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

    @GetMapping("/languages")
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

    @GetMapping("/countries")
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
    // feels bad to expose all roles in permitAll() endpoint, but we need it for the frontend to know which role id corresponds to which role name when assigning roles to users
    @GetMapping("/roles")
    public ResponseEntity<@NonNull GenericResponse<List<RoleDTO>>> getRoleDictionary() {
        return new ResponseEntity<>(
                new GenericResponse<>(
                        dictionaryService.getRoleDictionary(),
                        "CountryDictionaryFetched",
                        MessageConstants.COUNTRY_DICTIONARY_FETCHED,
                        true
                ),
                HttpStatus.OK
        );
    }
}

