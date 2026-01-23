package com.booking.booking_clone_backend.mappers;

import com.booking.booking_clone_backend.DTOs.responses.dictionaries.amenity.AmenityDTO;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.country.CountryDTO;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.language.LanguageDTO;
import com.booking.booking_clone_backend.models.amenity.Amenity;
import com.booking.booking_clone_backend.models.language.Language;
import com.booking.booking_clone_backend.models.property.Country;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface DictionaryMapper {
    List<AmenityDTO> amenitiesToDtoList(List<Amenity> amenities);
    AmenityDTO amenityToDto(Amenity amenity);
    List<LanguageDTO> languagesToDtoList(List<Language> languages);
    List<CountryDTO> countriesToDtoList(List<Country> countries);
}
