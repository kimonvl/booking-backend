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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DictionaryServiceImpl implements DictionaryService{

    private final AmenitiesRepo amenitiesRepo;
    private final LanguageRepo languageRepo;
    private final CountryRepo countryRepo;
    private final DictionaryMapper dictionaryMapper;

    @Override
    public List<AmenitiesDictionaryItemDTO> getAmenitiesDictionaryGroupByGroupName() {
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

    @Override
    public List<String> findIncorrectAmenityCodes(List<String> codes) {
        List<Amenity> existing = amenitiesRepo.findByCodeIn(codes);
        Set<Amenity> existingSet = new HashSet<>(existing);

        return codes.stream()
                .filter(code -> {
                    Amenity amenity = new Amenity();
                    amenity.setCode(code);
                    return !existingSet.contains(amenity);
                })
                .toList();
    }

    @Override
    public List<String> findIncorrectLanguageCodes(List<String> codes) {
        List<Language> existing = languageRepo.findByCodeInIgnoreCase(codes);
        Set<Language> existingSet = new HashSet<>(existing);

        return codes.stream()
                .filter(code -> {
                    Language language = new Language();
                    language.setCode(code);
                    return !existingSet.contains(language);
                })
                .toList();
    }

}
