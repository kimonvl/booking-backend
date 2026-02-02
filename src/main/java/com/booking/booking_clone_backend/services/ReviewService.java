package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.responses.review.ReviewSummaryDTO;

public interface ReviewService {
    ReviewSummaryDTO getPropertyReviewSummary(Long propertyId);
}
