package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.responses.dictionaries.amenity.AmenitiesDictionaryItemDTO;
import com.booking.booking_clone_backend.DTOs.responses.dictionaries.amenity.AmenityDTO;
import com.booking.booking_clone_backend.mappers.DictionaryMapper;
import com.booking.booking_clone_backend.models.amenity.Amenity;
import com.booking.booking_clone_backend.models.amenity.AmenityGroup;
import com.booking.booking_clone_backend.repos.AmenitiesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService{
    @Autowired
    AmenitiesRepo amenitiesRepo;
    @Autowired
    DictionaryMapper dictionaryMapper;

    @Override
    public List<AmenitiesDictionaryItemDTO> getAmenitiesDictionary() {
        List<AmenitiesDictionaryItemDTO> result = new ArrayList<>();
        //improve to one query and group by group name in java
        for(AmenityGroup group : AmenityGroup.values()){
            List<Amenity> amenities = amenitiesRepo.findByGroupName(group);
            List<AmenityDTO> amenityDTOs = dictionaryMapper.amenitiesToDtoList(amenities);
            result.add(new AmenitiesDictionaryItemDTO(group.name(), amenityDTOs));
        }

        return result;
    }
}
