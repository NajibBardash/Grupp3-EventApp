package se.yrgo.bookingservice.exceptions.event;

public class NoTicketsAvailableException extends RuntimeException {
    public NoTicketsAvailableException(String message) {
        super(message);
    }

    public NoTicketsAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
