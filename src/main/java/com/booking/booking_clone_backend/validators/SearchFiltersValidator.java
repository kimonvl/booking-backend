package com.booking.booking_clone_backend.validators;

import com.booking.booking_clone_backend.DTOs.requests.auth.RegisterRequest;
import com.booking.booking_clone_backend.DTOs.requests.guest.property.PropertySearchRequest;
import com.booking.booking_clone_backend.services.AuthServiceImpl;
import com.booking.booking_clone_backend.services.DictionaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
@Slf4j
public class SearchFiltersValidator implements Validator {

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return PropertySearchRequest.class == clazz;
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        RegisterRequest registerRequest = (RegisterRequest) target;
        if (!errors.hasFieldErrors("email")) {

        }

    }
}
