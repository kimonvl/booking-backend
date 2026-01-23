package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.responses.property.PropertyShortDTO;
import com.booking.booking_clone_backend.mappers.PropertyCustomMapper;
import com.booking.booking_clone_backend.models.property.Property;
import com.booking.booking_clone_backend.repos.PropertyRepo;
import org.springframework.beans.factory.annotation.Autowired;
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
}
