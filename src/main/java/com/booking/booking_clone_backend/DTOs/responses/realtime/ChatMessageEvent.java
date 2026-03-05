package com.booking.booking_clone_backend.DTOs.responses.realtime;

import java.time.Instant;
import java.util.UUID;

public record ChatMessageEvent(
        UUID chatUuid,
        UUID bookingUuid,
        UUID messageUuid,
        UUID authorUuid,
        String content,
        String photo,
        Instant createdAt
) {}
