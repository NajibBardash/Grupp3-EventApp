package se.yrgo.bookingservice.services;

import org.springframework.stereotype.Service;
import se.yrgo.bookingservice.data.TicketRepository;
import se.yrgo.bookingservice.domain.Ticket;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketServiceImpl {
    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> generateNewTickets(int amount) {
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Ticket ticket = new Ticket();
            tickets.add(ticket);
        }
        return tickets;
    }
}
