package se.yrgo.bookingservice.exceptions.booking;

/**
 * This exception is used as a "collector" for other exceptions in
 * the "exceptions.event" folder, it provides a simpler context.
 */
public class BookingFailedException extends RuntimeException {
    public BookingFailedException(String message) {
        super(message);
    }

    public BookingFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}

