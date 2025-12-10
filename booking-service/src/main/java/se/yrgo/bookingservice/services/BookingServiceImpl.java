package se.yrgo.bookingservice.services;

import org.springframework.stereotype.Service;
import se.yrgo.bookingservice.data.BookingRepository;
import se.yrgo.bookingservice.domain.Booking;
import se.yrgo.bookingservice.dto.BookingCreateDTO;
import se.yrgo.bookingservice.dto.BookingResponseDTO;
import se.yrgo.bookingservice.factory.TicketFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final TicketService ticketService;

    public BookingServiceImpl(BookingRepository bookingRepository, TicketService ticketService) {
        this.bookingRepository = bookingRepository;
        this.ticketService = ticketService;
    }

    public BookingResponseDTO createBooking(BookingCreateDTO dto) {
        Booking booking = Booking.builder()
                .dateOfBooking(LocalDateTime.now(ZoneId.of("UTC")))
                .eventId(dto.getEventId())
                .customerId(dto.getCustomerId())
                .tickets(TicketFactory.createTickets(dto.getNumberOfTickets())) // replace
                .isRefundable(dto.isRefundable())
                .build();
        bookingRepository.save(booking);

        return mapToResponseDTO(booking);
    }

    @Override
    public BookingResponseDTO getBooking(String bookingId) {
        return mapToResponseDTO(bookingRepository.findByBookingId(bookingId));
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream().map(
                this::mapToResponseDTO
        ).toList();
    }

    @Override
    public void deleteBooking(String bookingId) {
        Booking bookingToDelete = bookingRepository.findByBookingId(bookingId);
        bookingRepository.delete(bookingToDelete);
    }

    private BookingResponseDTO mapToResponseDTO(Booking booking) {
        return new BookingResponseDTO(
                booking.getBookingId(),
                booking.getDateOfBooking(),
                booking.getCustomerId(),
                booking.getEventId(),
                booking.getTickets(),
                booking.isRefundable()
        );
    }
}
