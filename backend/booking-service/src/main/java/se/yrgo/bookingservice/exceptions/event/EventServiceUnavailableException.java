package se.yrgo.bookingservice.exceptions.event;

public class EventServiceUnavailableException extends RuntimeException {

    public EventServiceUnavailableException(String message) {
        super(message);
    }

    public EventServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
