package se.yrgo.bookingservice.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.bookingservice.domain.Ticket;

@Service
@Transactional
public interface TicketService {
    Ticket createTicket(Ticket ticket);
    void deleteTicket(Ticket ticket);
}
