package com.booking.booking_clone_backend.mappers;

import com.booking.booking_clone_backend.DTOs.responses.dictionaries.amenity.AmenityDTO;
import com.booking.booking_clone_backend.DTOs.responses.property.PropertyDetailsDTO;
import com.booking.booking_clone_backend.DTOs.responses.property.PropertyShortDTO;
import com.booking.booking_clone_backend.DTOs.responses.review.ReviewSummaryDTO;
import com.booking.booking_clone_backend.models.property.PropertyAmenity;
import com.booking.booking_clone_backend.models.property.Property;
import com.booking.booking_clone_backend.models.property.PropertyPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PropertyCustomMapper {
    @Autowired
    DictionaryMapper dictionaryMapper;
    @Autowired
    AddressMapper addressMapper;

    public PropertyShortDTO propertyToPropertyShortDTO(Property property, ReviewSummaryDTO reviewSummaryDTO) {

        return new PropertyShortDTO(
                property.getId(),
                propertyAmenitiesToAmenityDTO(property.getAllPropertyAmenities()),
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
                property.getMainPhotoUrl(),
                reviewSummaryDTO
        );
    }

    public PropertyDetailsDTO propertyToPropertyDetailsDTO(Property property, ReviewSummaryDTO reviewSummaryDTO) {
        DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");
        return new PropertyDetailsDTO(
                property.getId(),
                propertyAmenitiesToAmenityDTO(property.getAllPropertyAmenities()),
                addressMapper.toDto(property.getAddress()),
                property.getType(),
                property.getName(),
                property.getPricePerNight(),
                property.getCurrency(),
                property.getSizeSqm(),
                property.getMaxGuests(),
                property.getBathrooms(),
                property.getLivingRoomCount(),
                property.getBedroomCount(),
                property.getBedSummary(),
                propertyPhotosToString(property.getAllPropertyPhotos()),
                property.getMainPhotoUrl(),
                reviewSummaryDTO,

                property.getCheckInFrom().format(TIME_FMT),
                property.getCheckInUntil().format(TIME_FMT),
                property.getCheckOutFrom().format(TIME_FMT),
                property.getCheckOutUntil().format(TIME_FMT),
                property.getChildrenAllowed(),
                property.getCotsOffered(),
                property.getSmokingAllowed(),
                property.getPartiesAllowed(),
                property.getPetsPolicy()
        );
    }

    private Set<String> propertyPhotosToString(List<PropertyPhoto> photos) {
        Set<String> photoUrls = new HashSet<>();
        for (PropertyPhoto pp : photos) {
            photoUrls.add(pp.getUrl());
        }
        return photoUrls;
    }

    private Set<AmenityDTO> propertyAmenitiesToAmenityDTO(Set<PropertyAmenity> propertyAmenities) {
        Set<AmenityDTO> amenityDTOs = new HashSet<>();
        for (PropertyAmenity pa : propertyAmenities) {
            amenityDTOs.add(dictionaryMapper.amenityToDto(pa.getAmenity()));
        }
        return amenityDTOs;
    }
}
