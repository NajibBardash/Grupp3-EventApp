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
    /**
     * Before a booking is registered, some things must be considered:
     * Are tickets available? Then try to reserve them!
     * Is the payment successful?(mocked as f**k) Create tickets, add them to a new booking
     * Did the reservation fail? Don't go any further!
     * Did the payment fail? cancel the reservation!
     */
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
            // Refund tickets back to the event before deleting the booking
            TicketReservationDetailsDTO refundDetails = TicketReservationDetailsDTO.builder()
                    .amount(bookingToDelete.getTickets().size())
                    .eventId(bookingToDelete.getEventId())
                    .build();

            cancelTicketReservation(refundDetails);

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

    /**
     * Try to reserve tickets via event-service, wrap all expected
     * exceptions in a BookingFailedException with cause.
     * @param reservationDetails how many tickets are we trying to reserve and what event.
     */
    private void reserveTickets(TicketReservationDetailsDTO reservationDetails) {
        try {
            eventQueryClient.handleReservation(reservationDetails, EventRequestMethod.RESERVE);
        }  catch (EventNotFoundException
                  | NoTicketsAvailableException
                  | EventServiceUnavailableException e) {
            throw new BookingFailedException(e.getMessage());
        }
    }

    /**
     * Try to cancel a ticket reservation if the booking process failed.
     * Also handles exceptions but they aren't really expected.
     * @param reservationDetails how many tickets are reserved and need cancelling, and the event.
     */
    private void cancelTicketReservation(TicketReservationDetailsDTO reservationDetails) {
        try {
            eventQueryClient.handleReservation(reservationDetails, EventRequestMethod.CANCEL);
        }
        // should only throw EventNotFound and EventServiceUnavailableExceptions
        catch (EventNotFoundException
                 | NoTicketsAvailableException
                 | EventServiceUnavailableException e) {
            System.err.println("Failed to cancel reservation for event " + reservationDetails.getEventId() + ": " + e.getMessage());
        }
    }

    /**
     * Small helper to keep concerns separate and methods shorter.
     * It just takes the needed data for a reservation request.
     * @param dto data for Booking info, not needed.
     * @return a dto with just ticket amount and event id to mirror the
     * dto in event-service
     */
    private static TicketReservationDetailsDTO getReservationDetails(BookingRequestDTO dto) {
        return TicketReservationDetailsDTO.builder()
                .amount(dto.getNumberOfTickets())
                .eventId(dto.getEventId())
                .build();
    }

    /**
     * A stupid mapper, makes DTOs out of booking-entity objects
     * @param booking JPA entity, copy into the DTO
     * @return a DTO clone of the parameter booking.
     */
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
