package se.yrgo.bookingservice.factory;

import se.yrgo.bookingservice.domain.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketFactory {
    public static List<Ticket> createTickets(int amount) {
        List<Ticket> tickets = new ArrayList<Ticket>();
        for (int i = 0; i < amount; i++) {
            Ticket ticket = new Ticket();
            tickets.add(ticket);
        }
        return tickets;
    }
}
