package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.models.booking.BookingStatus;
import com.booking.booking_clone_backend.repos.BookingRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingHoldCleanupJob {

    private final BookingRepo bookingRepo;
    private final PropertyAvailabilityService propertyAvailabilityService;
    // every 1 minute
    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void cleanupExpiredHolds() {
        Instant now = Instant.now();

        List<Long> expiredIds = bookingRepo.findExpiredPendingIds(BookingStatus.PENDING, now);
        if (expiredIds.isEmpty()) return;

        int deletedBlocks = propertyAvailabilityService.deleteBlocksByBookingIds(expiredIds);

        int expiredBookings = bookingRepo.markExpired(
                expiredIds,
                Instant.now(),
                BookingStatus.PENDING,
                BookingStatus.EXPIRED
        );

        log.info("Cleanup expired holds: bookingsExpired={}, blocksDeleted={}", expiredBookings, deletedBlocks);
    }
}
