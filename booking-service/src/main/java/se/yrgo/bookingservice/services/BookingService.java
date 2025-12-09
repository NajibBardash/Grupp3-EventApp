package se.yrgo.bookingservice.services;

import org.springframework.stereotype.Service;
import se.yrgo.bookingservice.data.BookingRepository;
import se.yrgo.bookingservice.domain.Booking;
import se.yrgo.bookingservice.domain.Ticket;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final TicketService ticketService;

    public BookingService(BookingRepository bookingRepository, TicketService ticketService) {
        this.bookingRepository = bookingRepository;
        this.ticketService = ticketService;

    }

    private String generateBookingId() {
        return "BKG-" + java.util.UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }

}
