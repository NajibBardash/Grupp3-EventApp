package se.yrgo.bookingservice.services;

import se.yrgo.bookingservice.data.BookingRepository;
import se.yrgo.bookingservice.domain.Booking;
import se.yrgo.bookingservice.domain.Ticket;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    private void createBooking(String eventId, String userId, int noOfTickets, boolean isRefundable){
        Booking booking = new Booking();
        try {
            booking.setBookingId(generateBookingId());
            booking.setDateOfBooking(LocalDateTime.now(ZoneId.of("UTC")));
            booking.setEventID(eventId);
            booking.setCustomerId(userId);
            booking.setTickets(generateTickets(noOfTickets));
            booking.setRefundable(isRefundable);
            bookingRepository.save(booking);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateBookingId() {
        return "BKG-" + java.util.UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }

    private static List<Ticket> generateTickets(int amount) {
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Ticket ticket = new Ticket();
            tickets.add(ticket);
        }
        return tickets;
    }
}
