package se.yrgo.event_service.messaging;

import java.io.Serializable;

/**
 * A class the represents the message to be pushed to a message-broker when an event is updated/cancelled
 * @param eventId of the event to change
 * @param action is either update/delete
 */
public record EventChangeMessage(String eventId, String action) implements Serializable {}
