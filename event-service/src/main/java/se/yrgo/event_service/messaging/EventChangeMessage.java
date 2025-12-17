package se.yrgo.event_service.messaging;

import java.io.Serializable;

public class EventChangeMessage implements Serializable {
    private String eventId;
    private String action;

    public EventChangeMessage() {}

    public EventChangeMessage(String eventId, String action) {
        this.eventId = eventId;
        this.action = action;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
