package se.yrgo.bookingservice.services;

import org.springframework.stereotype.Service;
import se.yrgo.bookingservice.data.BookingRepository;
import se.yrgo.bookingservice.domain.Booking;
import se.yrgo.bookingservice.domain.Ticket;
import se.yrgo.bookingservice.dto.BookingRequestDTO;
import se.yrgo.bookingservice.dto.BookingResponseDTO;
import se.yrgo.bookingservice.dto.TicketResponseDTO;
import se.yrgo.bookingservice.factory.TicketFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public BookingResponseDTO createBooking(BookingRequestDTO dto) {
        List<Ticket> tickets = TicketFactory.createTickets(dto.getNumberOfTickets());
        Booking booking = Booking.builder()
                .dateOfBooking(LocalDateTime.now(ZoneId.of("UTC")))
                .eventId(dto.getEventId())
                .customerId(dto.getCustomerId())
                .tickets(tickets)
                .refundable(dto.isRefundable())
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        return mapToResponseDTO(savedBooking);
    }

    @Override
    public BookingResponseDTO getBookingById(String bookingId) {
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
        if (bookingToDelete != null) {
            bookingRepository.delete(bookingToDelete);
        }
    }

//    @Override
//    public BookingResponseDTO editBooking(BookingRequestDTO editDto) {
//        Booking bookingToEdit = bookingRepository.findByBookingId(editDto.get)
//    }

    private BookingResponseDTO mapToResponseDTO(Booking booking) {
        return new BookingResponseDTO(
                booking.getBookingId(),
                booking.getDateOfBooking(),
                booking.getCustomerId(),
                booking.getEventId(),
                booking.getTickets().stream()
                    .map(ticket ->
                            new TicketResponseDTO(ticket.getTicketId(),
                                    booking.getBookingId(),
                                    ticket.getPrice()))
                .collect(toList()),
                booking.isRefundable()
        );
    }
}
