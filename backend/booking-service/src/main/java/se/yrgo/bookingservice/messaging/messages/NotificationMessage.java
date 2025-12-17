package se.yrgo.bookingservice.messaging.messages;

import java.util.List;

public record NotificationMessage(
        List<String> affectedUsers,
        String eventId,
        String eventName,
        String message) {}