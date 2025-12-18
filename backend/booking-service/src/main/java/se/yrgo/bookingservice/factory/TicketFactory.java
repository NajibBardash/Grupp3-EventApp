package se.yrgo.bookingservice.factory;

import se.yrgo.bookingservice.domain.Booking;
import se.yrgo.bookingservice.domain.Ticket;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to create a desired amount of tickets after payment has been
 * confirmed.
 * TODO: pricing is not implemented
 */
public class TicketFactory {
    public static List<Ticket> createTickets(int amount, Booking booking) {
        List<Ticket> tickets = new ArrayList<Ticket>();
        for (int i = 0; i < amount; i++) {
            Ticket ticket = new Ticket(0, booking);
            tickets.add(ticket);
        }
        return tickets;
    }
}
