package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.responses.dictionaries.amenity.AmenitiesDictionaryItemDTO;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.amenity.AmenityDTO;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.country.CountryDTO;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.language.LanguageDTO;

import java.util.List;

public interface DictionaryService {
    List<AmenitiesDictionaryItemDTO> getAmenitiesDictionaryGroupByGroupName();
    List<LanguageDTO> getLanguageDictionary();
    List<CountryDTO> getCountryDictionary();

    boolean isCountryExists(String code);
    List<String> findIncorrectAmenityCodes(List<String> codes);
    List<String> findIncorrectLanguageCodes(List<String> codes);
}
