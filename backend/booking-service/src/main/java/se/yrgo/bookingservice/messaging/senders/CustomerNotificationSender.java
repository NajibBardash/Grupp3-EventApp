package se.yrgo.bookingservice.messaging.senders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import se.yrgo.bookingservice.messaging.messages.NotificationMessage;

/**
 * NOT IMPLEMENTED, THIS IS CANCELLED
 */
@Service
public class CustomerNotificationSender {
    private final JmsTemplate jmsTemplate;
    private final String destination;

    public CustomerNotificationSender(JmsTemplate jmsTemplate, @Value("${app.jms.notify-customer-queue}") String destination) {
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
    }

    public void sendToNotificationService(NotificationMessage dto) {
        jmsTemplate.convertAndSend(destination, dto);
    }
}
