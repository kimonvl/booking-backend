package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.requests.guest.property.PropertySearchRequest;
import com.booking.booking_clone_backend.DTOs.responses.property.PropertyDetailsDTO;
import com.booking.booking_clone_backend.DTOs.responses.property.PropertyShortDTO;
import com.booking.booking_clone_backend.DTOs.responses.review.ReviewSummaryDTO;
import com.booking.booking_clone_backend.constants.MessageConstants;
import com.booking.booking_clone_backend.exceptions.EntityNotFoundException;
import com.booking.booking_clone_backend.mappers.PropertyCustomMapper;
import com.booking.booking_clone_backend.models.property.Property;
import com.booking.booking_clone_backend.repos.PropertyRepo;
import com.booking.booking_clone_backend.repos.ReviewRepo;
import com.booking.booking_clone_backend.repos.specifications.PropertySpecification;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GuestApartmentServiceImpl implements GuestApartmentService{
    @Autowired
    ReviewService reviewService;
    @Autowired
    PropertyRepo propertyRepo;
    @Autowired
    ReviewRepo reviewRepo;
    @Autowired
    PropertyCustomMapper propertyCustomMapper;



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

        List<@NonNull Property> properties = page.getContent();

        // If page is empty, avoid IN () query
        Map<Long, ReviewSummaryDTO> summaryMap;
        if (properties.isEmpty()) {
            summaryMap = Map.of();
        } else {
            List<Long> ids = properties.stream().map(Property::getId).toList();
            summaryMap = reviewRepo.getSummaryMap(ids);
        }

        List<PropertyShortDTO> dtoList = properties.stream()
                .map(p -> propertyCustomMapper.propertyToPropertyShortDTO(
                        p,
                        summaryMap.getOrDefault(p.getId(), new ReviewSummaryDTO(0, 0))
                ))
                .toList();

        return new org.springframework.data.domain.PageImpl<>(
                dtoList,
                page.getPageable(),
                page.getTotalElements()
        );
    }

    @Override
    public PropertyDetailsDTO getPropertyDetails(Long propertyId) {
        Optional<@NonNull Property> propertyOpt = propertyRepo.findById(propertyId);
        if (propertyOpt.isEmpty()) {
            throw new EntityNotFoundException(MessageConstants.PROPERTY_NOT_FOUND);
        }
        ReviewSummaryDTO reviewSummaryDTO = reviewService.getPropertyReviewSummary(propertyOpt.get().getId());
        return propertyCustomMapper.propertyToPropertyDetailsDTO(propertyOpt.get(), reviewSummaryDTO);
    }
}
