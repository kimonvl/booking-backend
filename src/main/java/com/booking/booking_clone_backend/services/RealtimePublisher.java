package com.booking.booking_clone_backend.services;

import com.booking.booking_clone_backend.DTOs.responses.realtime.ChatMessageEvent;
import com.booking.booking_clone_backend.DTOs.responses.realtime.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RealtimePublisher {

    private static final String USER_MESSAGES_DESTINATION = "/queue/messages";
    private static final String USER_NOTIFICATIONS_DESTINATION = "/queue/notifications";

    private final SimpMessagingTemplate messagingTemplate;

    // TODO send messageDTO
    // userDestination uses the authenticated Principal name (email in this project).
    public void sendMessageToUser(String userEmail, ChatMessageEvent event) {
        messagingTemplate.convertAndSendToUser(userEmail, USER_MESSAGES_DESTINATION, event);
    }

    // TODO send notificationDTO
    // userDestination uses the authenticated Principal name (email in this project).
    public void sendNotificationToUser(String userEmail, NotificationEvent event) {
        messagingTemplate.convertAndSendToUser(userEmail, USER_NOTIFICATIONS_DESTINATION, event);
    }
}
