package se.yrgo.event_service.exceptions;

/**
 * Custom exception for when the number of tickets to update for an event are faulty
 */
public class InsufficientTicketsException extends RuntimeException {
    public InsufficientTicketsException(String message) { super(message); }
}
