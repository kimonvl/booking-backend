package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.responses.realtime.MessageDTO;
import com.booking.booking_clone_backend.DTOs.responses.realtime.NotificationEvent;

public interface WebsocketService {
    public void sendMessageToUser(String userEmail, MessageDTO event);
    public void sendNotificationToUser(String userEmail, NotificationEvent event);
}
