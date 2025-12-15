package se.yrgo.bookingservice.messagequeues;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class EventUpdateMessageListener {
    @JmsListener(destination="eventUpdateQueue")

    public void processMessage(String placeholder) {
        System.out.println(placeholder);
    }
}
