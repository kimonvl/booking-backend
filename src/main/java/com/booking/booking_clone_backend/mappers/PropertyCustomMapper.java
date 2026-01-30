package com.booking.booking_clone_backend.mappers;

import com.booking.booking_clone_backend.DTOs.responses.dictionaries.amenity.AmenityDTO;
import com.booking.booking_clone_backend.DTOs.responses.property.PropertyShortDTO;
import com.booking.booking_clone_backend.models.amenity.PropertyAmenity;
import com.booking.booking_clone_backend.models.property.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PropertyCustomMapper {
    @Autowired
    DictionaryMapper dictionaryMapper;
    @Autowired
    AddressMapper addressMapper;

    public List<PropertyShortDTO> propertiesToPropertyShortDTOList(List<Property> properties) {
        List<PropertyShortDTO> result = new ArrayList<>();
        for (Property property : properties) {
            PropertyShortDTO psDTO = new PropertyShortDTO(
                    property.getId(),
                    propertyAmenitiesToAmenityDTO(property.getPropertyAmenities()),
                    addressMapper.toDto(property.getAddress()),
                    property.getType(),
                    property.getStatus(),
                    property.getName(),
                    property.getPricePerNight(),
                    property.getCurrency(),
                    property.getSizeSqm(),
                    property.getMaxGuests(),
                    property.getBathrooms(),
                    property.getLivingRoomCount(),
                    property.getBedroomCount(),
                    property.getBedSummary(),
                    property.getMainPhotoUrl()
            );
            result.add(psDTO);
        }
        return result;
    }

    public PropertyShortDTO propertyToPropertyShortDTO(Property property) {

        return new PropertyShortDTO(
                property.getId(),
                propertyAmenitiesToAmenityDTO(property.getPropertyAmenities()),
                addressMapper.toDto(property.getAddress()),
                property.getType(),
                property.getStatus(),
                property.getName(),
                property.getPricePerNight(),
                property.getCurrency(),
                property.getSizeSqm(),
                property.getMaxGuests(),
                property.getBathrooms(),
                property.getLivingRoomCount(),
                property.getBedroomCount(),
                property.getBedSummary(),
                property.getMainPhotoUrl()
        );
    }

    private Set<AmenityDTO> propertyAmenitiesToAmenityDTO(Set<PropertyAmenity> propertyAmenities) {
        Set<AmenityDTO> amenityDTOs = new HashSet<>();
        for (PropertyAmenity pa : propertyAmenities) {
            amenityDTOs.add(dictionaryMapper.amenityToDto(pa.getAmenity()));
        }
        return amenityDTOs;
    }
}
