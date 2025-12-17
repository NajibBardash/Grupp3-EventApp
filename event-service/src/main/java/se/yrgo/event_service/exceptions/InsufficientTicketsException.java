package se.yrgo.event_service.exceptions;

public class InsufficientTicketsException extends RuntimeException {
    public InsufficientTicketsException(String message) { super(message); }
}
