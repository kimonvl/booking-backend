package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.responses.review.ReviewSummaryDTO;
import com.booking.booking_clone_backend.repos.ReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService{
    @Autowired
    ReviewRepo reviewRepo;

    @Override
    public ReviewSummaryDTO getPropertyReviewSummary(Long propertyId) {
        return reviewRepo.getSummaryByPropertyId(propertyId);
    }
}
