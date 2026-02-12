package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.responses.dictionaries.amenity.AmenitiesDictionaryItemDTO;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.amenity.AmenityDTO;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.country.CountryDTO;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.language.LanguageDTO;
import com.booking.booking_clone_backend.mappers.DictionaryMapper;
import com.booking.booking_clone_backend.models.static_data.Amenity;
import com.booking.booking_clone_backend.models.static_data.AmenityGroup;
import com.booking.booking_clone_backend.models.static_data.Language;
import com.booking.booking_clone_backend.models.static_data.Country;
import com.booking.booking_clone_backend.repos.AmenitiesRepo;
import com.booking.booking_clone_backend.repos.CountryRepo;
import com.booking.booking_clone_backend.repos.LanguageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService{
    @Autowired
    AmenitiesRepo amenitiesRepo;
    @Autowired
    LanguageRepo languageRepo;
    @Autowired
    CountryRepo countryRepo;
    @Autowired
    DictionaryMapper dictionaryMapper;

    @Override
    public List<AmenitiesDictionaryItemDTO> getAmenitiesDictionary() {
        List<AmenitiesDictionaryItemDTO> result = new ArrayList<>();
        List<Amenity> amenities = amenitiesRepo.findAll();
        //improve to one query and group by group name in java
        for(AmenityGroup group : AmenityGroup.values()){
            List<Amenity> amenitiesByGroup = amenities.stream()
                    .filter(amenity -> amenity.getGroupName().equals(group))
                    .toList();
            List<AmenityDTO> amenityDTOs = dictionaryMapper.amenitiesToDtoList(amenitiesByGroup);
            result.add(new AmenitiesDictionaryItemDTO(group.name(), group.getLabel(), amenityDTOs));
        }

        return result;
    }

    @Override
    public List<LanguageDTO> getLanguageDictionary() {
        List<Language> languages = languageRepo.findAll();
        return dictionaryMapper.languagesToDtoList(languages);
    }

    @Override
    public List<CountryDTO> getCountryDictionary() {
        List<Country> countries = countryRepo.findAll();
        return dictionaryMapper.countriesToDtoList(countries);
    }

    @Override
    public boolean isCountryExists(String code) {
        return countryRepo.existsByCode(code);
    }
}
