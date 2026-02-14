package com.booking.booking_clone_backend.mappers;

import com.booking.booking_clone_backend.DTOs.requests.booking.CreateBookingRequest;
import com.booking.booking_clone_backend.models.booking.Booking;
import com.booking.booking_clone_backend.models.booking.BookingCheckoutDetails;
import com.booking.booking_clone_backend.models.booking.BookingStatus;
import com.booking.booking_clone_backend.models.booking.PaymentStatus;
import com.booking.booking_clone_backend.models.property.Property;
import com.booking.booking_clone_backend.models.user.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BookingCustomMapper {
    public Booking createBookingRequestToBooking(
            CreateBookingRequest request,
            Property property,
            User user,
            BookingCheckoutDetails details,
            BigDecimal total
    ) {
        Booking booking = new Booking();
        booking.setProperty(property);
        booking.setGuest(user);
        booking.setCheckInDate(request.checkIn());
        booking.setCheckOutDate(request.checkOut());
        booking.setGuestCount(request.guestCount());
        booking.setStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.REQUIRES_PAYMENT);
        booking.setAmountTotal(total);
        booking.setCheckoutDetails(details);
        return booking;
    }
}
