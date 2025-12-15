package se.yrgo.event_service.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import se.yrgo.event_service.domain.Event;

@Service
public class EventMessageProducer {

    private final JmsTemplate jmsTemplate;
    private final String destination;

    public EventMessageProducer(JmsTemplate jmsTemplate,
                                @Value("${app.jms.event-queue}") String destination) {
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
    }

    public void sendEventUpdated(String eventId) {
        EventChangeMessage msg = new EventChangeMessage(
                eventId,
                "UPDATED"
        );
        jmsTemplate.convertAndSend(destination, msg);
    }

    public void sendEventDeleted(String eventId) {
        EventChangeMessage msg = new EventChangeMessage(
                eventId,
                "DELETED"
        );
        jmsTemplate.convertAndSend(destination, msg);
    }
}
