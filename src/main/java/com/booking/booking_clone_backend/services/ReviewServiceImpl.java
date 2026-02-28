package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.responses.review.ReviewSummaryDTO;
import com.booking.booking_clone_backend.repos.ReviewRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepo reviewRepo;

    @Override
    public ReviewSummaryDTO getPropertyReviewSummary(Long propertyId) {
        return reviewRepo.getSummaryByPropertyId(propertyId);
    }
}
