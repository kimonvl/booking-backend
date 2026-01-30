package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.requests.guest.property.PropertySearchRequest;
import com.booking.booking_clone_backend.DTOs.responses.property.PropertyShortDTO;
import com.booking.booking_clone_backend.mappers.PropertyCustomMapper;
import com.booking.booking_clone_backend.models.property.Property;
import com.booking.booking_clone_backend.repos.PropertyRepo;
import com.booking.booking_clone_backend.repos.specifications.PropertySpecification;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestApartmentServiceImpl implements GuestApartmentService{
    @Autowired
    PropertyRepo propertyRepo;
    @Autowired
    PropertyCustomMapper propertyCustomMapper;

    @Override
    public List<PropertyShortDTO> getPropertiesByCity(String city) {
        List<Property> properties = propertyRepo.findByAddress_CityIgnoreCase(city);
        return propertyCustomMapper.propertiesToPropertyShortDTOList(properties);
    }

    @Override
    public Page<@NonNull PropertyShortDTO> search(PropertySearchRequest request) {
        Specification<@NonNull Property> spec = Specification
                //.where(PropertySpecification.isPublished())
                .where(PropertySpecification.cityEqualsIgnoreCase(request.city()))
                .and(PropertySpecification.allowPets(request.pets()))
                .and(PropertySpecification.guestsAtLeast(request.maxGuest()))
                .and(PropertySpecification.bedroomsAtLeast(request.bedroomCount()))
                .and(PropertySpecification.bathroomsAtLeast(request.bathroomCount()))
                .and(PropertySpecification.priceBetween(request.minPrice(), request.maxPrice()))
                .and(PropertySpecification.availableBetween(request.checkIn(), request.checkOut()))
                .and(PropertySpecification.hasAllAmenities(request.amenities()));

        Page<@NonNull Property> page = propertyRepo.findAll(spec, PageRequest.of(request.page(), request.size()));
        System.out.println(page.stream().count());
        return page.map(propertyCustomMapper::propertyToPropertyShortDTO);
    }
}
