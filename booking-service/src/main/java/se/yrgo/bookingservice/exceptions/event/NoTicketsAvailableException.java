package se.yrgo.bookingservice.exceptions.event;

public class NoTicketsAvailableException extends RuntimeException {
    public NoTicketsAvailableException(String message) {
        super(message);
    }
}
