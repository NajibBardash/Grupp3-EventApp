package se.yrgo.bookingservice.messaging.listeners;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import se.yrgo.bookingservice.dto.BookingResponseDTO;
import se.yrgo.bookingservice.messaging.messages.EventChangeMessage;
import se.yrgo.bookingservice.messaging.messages.NotificationMessage;
import se.yrgo.bookingservice.services.BookingService;

import java.util.List;

@Component
public class EventUpdateListener {
    private BookingService bookingService;

    public EventUpdateListener(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @JmsListener(destination="eventUpdateQueue")
    public void processMessage(EventChangeMessage message) {
        System.out.println("Updated event: " + message.eventId() + ": " + message.message());

        // Get all customers that have tickets for the event
        List<String> affectedUsers = bookingService.getBookingByEventId(message.eventId()).stream()
                .map(BookingResponseDTO::getCustomerId)
                .toList();



//        // Create a DTO with data for notificationService to use.
//        NotificationMessage notice = new NotificationMessage(
//                affectedUsers, message
//        );

    }
}
