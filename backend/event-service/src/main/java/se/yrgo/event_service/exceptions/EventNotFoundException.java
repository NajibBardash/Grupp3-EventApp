package se.yrgo.event_service.exceptions;

/**
 * Custom exception for when an event cannot be found
 */
public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) { super(message); }
}