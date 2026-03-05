package com.booking.booking_clone_backend.DTOs.responses.realtime;

import java.time.Instant;

public record NotificationEvent(
        NotificationType type,
        String title,
        String body,
        Object payload,
        Instant createdAt
) {}
