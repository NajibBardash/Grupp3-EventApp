package se.yrgo.bookingservice.services;

import org.springframework.stereotype.Service;
import se.yrgo.bookingservice.data.BookingRepository;
import se.yrgo.bookingservice.domain.Booking;
import se.yrgo.bookingservice.domain.Ticket;
import se.yrgo.bookingservice.dto.BookingRequestDTO;
import se.yrgo.bookingservice.dto.BookingResponseDTO;
import se.yrgo.bookingservice.dto.ReserveTicketsDTO;
import se.yrgo.bookingservice.dto.TicketResponseDTO;
import se.yrgo.bookingservice.exceptions.booking.BookingNotFoundException;
import se.yrgo.bookingservice.exceptions.event.EventNotFoundException;
import se.yrgo.bookingservice.exceptions.event.NoTicketsAvailableException;
import se.yrgo.bookingservice.external.event.EventClient;
import se.yrgo.bookingservice.factory.TicketFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final EventClient eventClient;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
        this.eventClient = new EventClient();
    }

    public BookingResponseDTO createBooking(BookingRequestDTO dto) {

        ReserveTicketsDTO reserveTicketsDTO = ReserveTicketsDTO.builder()
                .amount(dto.getNumberOfTickets())
                .eventId(dto.getEventId())
                .build();

        try {
            eventClient.reserveTickets(reserveTicketsDTO);
        } catch (EventNotFoundException e) {
            throw new EventNotFoundException("Could not find event with id: " + dto.getEventId());
        } catch (NoTicketsAvailableException e) {
            throw new NoTicketsAvailableException("No tickets available for event: " + dto.getEventId());
        }

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
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking id cannot be null");
        }

        Booking booking = bookingRepository.findByBookingId(bookingId);

        if (booking == null) {
            throw new BookingNotFoundException("Could not find booking with id: " + bookingId);
        }
        return mapToResponseDTO(booking);
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
        } else {
            throw new BookingNotFoundException("Could not find booking with id: " + bookingId);
        }
    }

    @Override
    public BookingResponseDTO editBooking(String bookingId, BookingRequestDTO bookingRequestDTO) {
        return null;

    }

    @Override
    public List<Ticket> getTicketsForBooking(String bookingId) {
        if (bookingId != null) {
                return bookingRepository.findByBookingId(bookingId).getTickets();
        } else {
            throw new IllegalArgumentException("bookingId is null");
        }
    }


    private void reserveTickets(ReserveTicketsDTO dto) {
        try {
            eventClient.reserveTickets(dto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

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
