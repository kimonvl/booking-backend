package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.responses.dictionaries.amenity.AmenitiesDictionaryItemDTO;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.language.LanguageDTO;

import java.util.List;

public interface DictionaryService {
    List<AmenitiesDictionaryItemDTO> getAmenitiesDictionary();
    List<LanguageDTO> getLanguageDictionary();
}
