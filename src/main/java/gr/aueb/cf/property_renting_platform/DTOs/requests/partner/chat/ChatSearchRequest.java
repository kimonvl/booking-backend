package gr.aueb.cf.property_renting_platform.DTOs.requests.partner.chat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChatSearchRequest(
        UUID propertyId,
        String searchTerm,
        Boolean isUnreadMessagesOnly,

        // TODO: fix the messages
        @NotNull(message = "{NotNull.propertySearchRequest.page}")
        @Min(value = 0, message = "{Min.propertySearchRequest.page}")
        Integer page,

        @NotNull(message = "{NotNull.propertySearchRequest.size}")
        @Min(value = 1, message = "{Min.propertySearchRequest.size}")
        @Max(value = 100, message = "{Max.propertySearchRequest.size}")
        Integer size
) {
}
