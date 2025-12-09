package se.yrgo.bookingservice.services;

import se.yrgo.bookingservice.data.TicketRepository;

public class TicketService {
    private TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    private static String generateTicketId() {
        return "TKT-" + java.util.UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }

}
