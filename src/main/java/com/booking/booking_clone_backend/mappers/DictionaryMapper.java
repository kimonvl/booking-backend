package com.booking.booking_clone_backend.mappers;

import com.booking.booking_clone_backend.DTOs.responses.dictionaries.amenity.AmenityDTO;
import com.booking.booking_clone_backend.models.amenity.Amenity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DictionaryMapper.class})
public interface DictionaryMapper {
    List<AmenityDTO> amenitiesToDtoList(List<Amenity> amenities);
}
