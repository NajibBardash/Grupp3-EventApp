package se.yrgo.bookingservice.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.bookingservice.data.BookingRepository;
import se.yrgo.bookingservice.domain.Booking;
import se.yrgo.bookingservice.domain.Ticket;
import se.yrgo.bookingservice.dto.BookingRequestDTO;
import se.yrgo.bookingservice.dto.BookingResponseDTO;
import se.yrgo.bookingservice.dto.TicketReservationDetailsDTO;
import se.yrgo.bookingservice.dto.TicketResponseDTO;
import se.yrgo.bookingservice.exceptions.booking.BookingFailedException;
import se.yrgo.bookingservice.exceptions.booking.BookingNotFoundException;
import se.yrgo.bookingservice.exceptions.event.EventNotFoundException;
import se.yrgo.bookingservice.exceptions.event.EventServiceUnavailableException;
import se.yrgo.bookingservice.exceptions.event.NoTicketsAvailableException;
import se.yrgo.bookingservice.integrations.event.EventQueryClient;
import se.yrgo.bookingservice.factory.TicketFactory;
import se.yrgo.bookingservice.integrations.event.EventRequestMethod;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final EventQueryClient eventQueryClient;

    public BookingServiceImpl(BookingRepository bookingRepository, EventQueryClient eventQueryClient) {
        this.bookingRepository = bookingRepository;
        this.eventQueryClient = eventQueryClient;
    }

    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO dto) {

        TicketReservationDetailsDTO reservationDetails = getReservationDetails(dto);

        reserveTickets(reservationDetails);

        //MOCK
        boolean paymentSuccess = true;

        if (paymentSuccess) {
            Booking booking = Booking.builder()
                    .dateOfBooking(LocalDateTime.now(ZoneId.of("UTC")))
                    .eventId(dto.getEventId())
                    .customerId(dto.getCustomerId())
                    .refundable(dto.isRefundable())
                    .build();

            List<Ticket> tickets = TicketFactory.createTickets(
                    dto.getNumberOfTickets(),
                    booking);

            booking.setTickets(tickets);

            Booking savedBooking = bookingRepository.save(booking);

            return mapToResponseDTO(savedBooking);
        } else {
            cancelTicketReservation(reservationDetails);
            throw new BookingFailedException("Payment failed!");
        }
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
    @Transactional
    public void deleteBooking(String bookingId) {
        Booking bookingToDelete = bookingRepository.findByBookingId(bookingId);
        if (bookingToDelete != null) {
            bookingRepository.delete(bookingToDelete);
        } else {
            throw new BookingNotFoundException("Could not find booking with id: " + bookingId);
        }
    }

    @Override
    public List<Ticket> getTicketsForBooking(String bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId);
        if (booking == null) {
            throw new BookingNotFoundException("Could not find booking with id: " + bookingId);
        }
        return booking.getTickets();
    }

    @Override
    public List<BookingResponseDTO> getBookingByEventId(String eventId) {
        return bookingRepository.findByEventId(eventId).stream().map(
                this::mapToResponseDTO
        ).toList();
    }

    private void reserveTickets(TicketReservationDetailsDTO reservationDetails) {

        try {
            eventQueryClient.handleReservation(reservationDetails, EventRequestMethod.RESERVE);
        }  catch (EventNotFoundException
                  | NoTicketsAvailableException
                  | EventServiceUnavailableException e) {
            throw new BookingFailedException("Failed to reserve tickets for event "
                    + reservationDetails.getEventId()
                    + ": "
                    + e.getMessage(),
                    e);
        }
    }

    private void cancelTicketReservation(TicketReservationDetailsDTO reservationDetails) {
        try {
            eventQueryClient.handleReservation(reservationDetails, EventRequestMethod.CANCEL);
        } catch (EventNotFoundException
                 | NoTicketsAvailableException
                 | EventServiceUnavailableException e) {
            System.err.println("Failed to cancel reservation for event " + reservationDetails.getEventId() + ": " + e.getMessage());
        }
    }

    private static TicketReservationDetailsDTO getReservationDetails(BookingRequestDTO dto) {
        return TicketReservationDetailsDTO.builder()
                .amount(dto.getNumberOfTickets())
                .eventId(dto.getEventId())
                .build();
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
