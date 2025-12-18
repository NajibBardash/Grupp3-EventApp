package se.yrgo.event_service.domain;

import org.junit.jupiter.api.Test;
import se.yrgo.event_service.exceptions.InsufficientTicketsException;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void testSuccessfulDecreaseAvailableTicketsOnBooking() {
        Event event = new Event();
        event.setAvailableTickets(10);
        event.decreaseAvailableTickets(1);

        assertEquals(9, event.getAvailableTickets());
    }

    @Test
    void testThatCustomExceptionIsThrownWhenBookingTooManyTickets() {
        Event event = new Event();
        event.setAvailableTickets(10);

        assertThrows(InsufficientTicketsException.class, () -> event.decreaseAvailableTickets(11));
    }

    @Test
    void testThatExceptionIsThrownWhenBookingTooFewTickets() {
        Event event = new Event();
        event.setAvailableTickets(10);

        assertThrows(IllegalArgumentException.class, () -> event.decreaseAvailableTickets(0));
        assertThrows(IllegalArgumentException.class, () -> event.decreaseAvailableTickets(-1));
    }

    @Test
    void testSuccessfulIncreaseOfAvailableTicketsOnCancellation() {
        Event event = new Event();
        event.setCapacity(10);
        event.setAvailableTickets(9);
        event.increaseAvailableTickets(1);

        assertEquals(10, event.getAvailableTickets());
    }

    @Test
    void testThatCustomExceptionIsThrownWhenCancellingTooManyTickets() {
        Event event = new Event();
        event.setCapacity(10);
        event.setAvailableTickets(9);

        assertThrows(InsufficientTicketsException.class, () -> event.increaseAvailableTickets(2));
    }

    @Test
    void testThatExceptionIsThrownWhenCancellingTooFewTickets() {
        Event event = new Event();
        event.setCapacity(10);
        event.setAvailableTickets(9);

        assertThrows(IllegalArgumentException.class, () -> event.increaseAvailableTickets(0));
        assertThrows(IllegalArgumentException.class, () -> event.increaseAvailableTickets(-1));
    }
}