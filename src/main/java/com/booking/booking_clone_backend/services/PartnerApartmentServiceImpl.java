package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.requests.apartment.BedType;
import com.booking.booking_clone_backend.DTOs.requests.apartment.BedroomDTO;
import com.booking.booking_clone_backend.DTOs.requests.apartment.CreateApartmentRequest;
import com.booking.booking_clone_backend.constants.MessageConstants;
import com.booking.booking_clone_backend.exceptions.InvalidCountryCodeException;
import com.booking.booking_clone_backend.models.amenity.Amenity;
import com.booking.booking_clone_backend.models.amenity.PropertyAmenity;
import com.booking.booking_clone_backend.models.language.Language;
import com.booking.booking_clone_backend.models.language.PropertyLanguage;
import com.booking.booking_clone_backend.models.property.*;
import com.booking.booking_clone_backend.models.user.User;
import com.booking.booking_clone_backend.repos.AmenitiesRepo;
import com.booking.booking_clone_backend.repos.CountryRepo;
import com.booking.booking_clone_backend.repos.LanguageRepo;
import com.booking.booking_clone_backend.repos.PropertyRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PartnerApartmentServiceImpl implements PartnerApartmentService {
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    private PropertyRepo propertyRepo;
    @Autowired
    private AmenitiesRepo amenitiesRepo;
    @Autowired
    private CountryRepo countryRepo;
    @Autowired
    private LanguageRepo languageRepo;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void addApartment(CreateApartmentRequest request, List<MultipartFile> photos, Integer mainIndex, User user) {
        Property property = new Property();
        property.setOwner(user);
        property.setType(PropertyType.APARTMENT);
        property.setStatus(PropertyStatus.DRAFT);
        property.setName(request.propertyName());
        property.setPricePerNight(request.pricePerNight());
        property.setCurrency(CurrencyCode.EUR);
        property.setMaxGuests(request.guestCount());
        property.setSizeSqm(request.aptSize());
        property.setChildrenAllowed(request.allowChildren());
        property.setCotsOffered(request.offerCots());
        property.setBreakfastServed(request.serveBreakfast());
        property.setParkingPolicy(request.isParkingAvailable());
        property.setSmokingAllowed(request.smokingAllowed());
        property.setPartiesAllowed(request.partiesAllowed());
        property.setPetsPolicy(request.petsAllowed());
        property.setCheckInFrom(LocalTime.parse(request.checkInFrom()));
        property.setCheckInUntil(LocalTime.parse(request.checkInUntil()));
        property.setCheckOutFrom(LocalTime.parse(request.checkOutFrom()));
        property.setCheckOutUntil(LocalTime.parse(request.checkOutUntil()));
        property.setBathrooms(request.bathroomCount());
        try {
            property.setSleepingAreasJson(objectMapper.writeValueAsString(request.sleepingAreas()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //Bedroom and Living room beds
        Integer bedCount = 0;
        int livingRoomCount = 0;
        Map<BedType, Integer> totalBeds = new HashMap<>();
        for (BedroomDTO bedroom : request.sleepingAreas().bedrooms()) {
            if (bedroom != null) {
                for (Map.Entry<BedType, Integer> entry : bedroom.beds().entrySet()) {
                    bedCount += entry.getValue();
                    totalBeds.compute(
                            entry.getKey(),
                            (key, value) -> value == null ? entry.getValue() : value + entry.getValue()
                    );
                }
            }
        }
        if (request.sleepingAreas().livingRoom() != null) {
            livingRoomCount += 1;
            for (Map.Entry<BedType, Integer> entry : request.sleepingAreas().livingRoom().beds().entrySet()) {
                bedCount += entry.getValue();
                totalBeds.compute(
                        entry.getKey(),
                        (key, value) -> value == null ? entry.getValue() : value + entry.getValue()
                );
            }
        }
        StringBuilder bedSummary = new StringBuilder(bedCount + " beds (");
        for (Map.Entry<BedType, Integer> entry : totalBeds.entrySet()) {
            if (entry.getValue() > 0)
                bedSummary.append(entry.getValue()).append(" ").append(entry.getKey().name()).append(", ");
        }
        bedSummary.delete(bedSummary.length()-2, bedSummary.length()-1);
        bedSummary.append(")");
        property.setLivingRoomCount(livingRoomCount);
        property.setBedroomCount(request.sleepingAreas().bedrooms().size());
        property.setBedCount(bedCount);
        property.setBedSummary(bedSummary.toString());

        Property savedProperty = propertyRepo.save(property);

        //Property-Amenity
        List<Amenity> amenities = amenitiesRepo.findByCodeIn(request.amenities());
        for (Amenity amenity : amenities) {
            PropertyAmenity pa = new PropertyAmenity();
            pa.setAmenity(amenity);
            pa.setProperty(savedProperty);
            pa.setId(new PropertyAmenity.PropertyAmenityId(savedProperty.getId(), amenity.getId()));

            savedProperty.getPropertyAmenities().add(pa);
        }

        //Property-Address
        Optional<Country> countryOpt = countryRepo.findByCode(request.address().country());
        if (countryOpt.isEmpty())
            throw new InvalidCountryCodeException(MessageConstants.INVALID_COUNTRY_CODE);
        Country country = countryOpt.get();
        PropertyAddress address = getPropertyAddress(request, country, savedProperty);
        savedProperty.setAddress(address);

        //Property-Languages
        List<Language> languages = languageRepo.findByCodeIn(request.languages());
        for (Language lang : languages) {
            PropertyLanguage pl = new PropertyLanguage();
            pl.setProperty(savedProperty);
            pl.setLanguage(lang);
            pl.setId(new PropertyLanguage.PropertyLanguageId(savedProperty.getId(), lang.getId()));

            savedProperty.getPropertyLanguages().add(pl);
        }

        //Upload photos to cloudinary
        String folder = "booking/properties/" + savedProperty.getId();
        for (int i = 0; i < photos.size(); i++) {
            //implement main photo later
            CloudinaryService.UploadResult res = cloudinaryService.uploadImage(photos.get(i), folder, "photo_" + i);
            PropertyPhoto pp = new PropertyPhoto();
            pp.setProperty(savedProperty);
            pp.setUrl(res.url());
            pp.setPublicId(res.publicId());

            savedProperty.getPropertyPhotos().add(pp);
            if (mainIndex == i) {
                // fix mainPhotoId
                savedProperty.setMainPhotoUrl(res.url());
            }
        }

        propertyRepo.save(savedProperty);
    }

    private static PropertyAddress getPropertyAddress(CreateApartmentRequest request, Country country, Property savedProperty) {
        PropertyAddress address = new PropertyAddress();
        address.setProperty(savedProperty);
        address.setCountry(country);
        address.setCity(request.address().city());
        address.setPostcode(request.address().postcode());
        address.setStreet(request.address().street());
        address.setStreetNumber(request.address().streetNumber());
        return address;
    }
}
