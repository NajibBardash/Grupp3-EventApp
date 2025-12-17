package se.yrgo.event_service.exceptions;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) { super(message); }
}