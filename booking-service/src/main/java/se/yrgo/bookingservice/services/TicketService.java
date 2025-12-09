package se.yrgo.bookingservice.services;

import org.springframework.stereotype.Service;
import se.yrgo.bookingservice.data.TicketRepository;
import se.yrgo.bookingservice.domain.Ticket;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }


    private String generateTicketId() {
        return "TKT-" + java.util.UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }

}
