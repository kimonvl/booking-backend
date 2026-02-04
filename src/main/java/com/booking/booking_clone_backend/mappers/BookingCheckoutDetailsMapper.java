package com.booking.booking_clone_backend.mappers;

import com.booking.booking_clone_backend.DTOs.requests.booking.CheckOutDetailsDTO;
import com.booking.booking_clone_backend.models.booking.BookingCheckoutDetails;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookingCheckoutDetailsMapper {

    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "bookingId", ignore = true)
    @Mapping(target = "contactEmail", source = "email")
    BookingCheckoutDetails toEntity(CheckOutDetailsDTO dto);

}
