package se.yrgo.bookingservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import se.yrgo.bookingservice.data.BookingRepository;
import se.yrgo.bookingservice.domain.Booking;
import se.yrgo.bookingservice.domain.Ticket;
import se.yrgo.bookingservice.dto.BookingRequestDTO;
import se.yrgo.bookingservice.dto.BookingResponseDTO;
import se.yrgo.bookingservice.dto.TicketReservationDetailsDTO;
import se.yrgo.bookingservice.exceptions.booking.BookingFailedException;
import se.yrgo.bookingservice.exceptions.booking.BookingNotFoundException;
import se.yrgo.bookingservice.exceptions.event.EventNotFoundException;
import se.yrgo.bookingservice.factory.TicketFactory;
import se.yrgo.bookingservice.integrations.event.EventQueryClient;
import se.yrgo.bookingservice.integrations.event.EventRequestMethod;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    BookingRepository bookingRepository;

    @Mock
    EventQueryClient eventQueryClient;

    @InjectMocks
    BookingServiceImpl bookingService;

    BookingRequestDTO bookingRequest;
    Booking bookingEntity;

    @BeforeEach
    void setup() {
        bookingRequest = new BookingRequestDTO();
        bookingRequest.setEventId("EVT123");
        bookingRequest.setCustomerId("USR123");
        bookingRequest.setNumberOfTickets(2);
        bookingRequest.setRefundable(true);

        bookingEntity = Booking.builder()
                .dateOfBooking(LocalDateTime.now())
                .eventId(bookingRequest.getEventId())
                .customerId(bookingRequest.getCustomerId())
                .refundable(bookingRequest.isRefundable())
                .tickets(TicketFactory.createTickets(bookingRequest.getNumberOfTickets(), null))
                .build();
    }

    @Test
    void createBooking_success() {
        when(bookingRepository.save(any(Booking.class))).thenReturn(bookingEntity);

        BookingResponseDTO response = bookingService.createBooking(bookingRequest);

        assertThat(response.getEventId()).isEqualTo("EVT123");
        assertThat(response.getCustomerId()).isEqualTo("USR123");
        assertThat(response.getTickets()).hasSize(2);

        ArgumentCaptor<TicketReservationDetailsDTO> captor = ArgumentCaptor.forClass(TicketReservationDetailsDTO.class);
        verify(eventQueryClient).handleReservation(captor.capture(), eq(EventRequestMethod.RESERVE));

        assertThat(captor.getValue().getAmount()).isEqualTo(2);
        assertThat(captor.getValue().getEventId()).isEqualTo("EVT123");

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBooking_reservationFails_throwsBookingFailedException() throws EventNotFoundException {
        doThrow(new EventNotFoundException("Event missing"))
                .when(eventQueryClient).handleReservation(any(), eq(EventRequestMethod.RESERVE));

        assertThatThrownBy(() -> bookingService.createBooking(bookingRequest))
                .isInstanceOf(BookingFailedException.class)
                .hasMessageContaining("Event missing");

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void getBookingById_success() {
        when(bookingRepository.findByBookingId("BOOK123")).thenReturn(bookingEntity);

        BookingResponseDTO response = bookingService.getBookingById("BOOK123");

        assertThat(response.getEventId()).isEqualTo("EVT123");
        assertThat(response.getCustomerId()).isEqualTo("USR123");
    }

    @Test
    void getBookingById_notFound() {
        when(bookingRepository.findByBookingId("INVALID")).thenReturn(null);

        assertThatThrownBy(() -> bookingService.getBookingById("INVALID"))
                .isInstanceOf(BookingNotFoundException.class);
    }

    @Test
    void getAllBookings_returnsList() {
        when(bookingRepository.findAll()).thenReturn(Collections.singletonList(bookingEntity));

        List<BookingResponseDTO> bookings = bookingService.getAllBookings();

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getFirst().getEventId()).isEqualTo("EVT123");
    }

    @Test
    void deleteBooking_success() {
        when(bookingRepository.findByBookingId("BOOK123")).thenReturn(bookingEntity);

        bookingService.deleteBooking("BOOK123");

        verify(eventQueryClient).handleReservation(any(TicketReservationDetailsDTO.class), eq(EventRequestMethod.CANCEL));
        verify(bookingRepository).delete(bookingEntity);
    }

    @Test
    void deleteBooking_notFound() {
        when(bookingRepository.findByBookingId("INVALID")).thenReturn(null);

        assertThatThrownBy(() -> bookingService.deleteBooking("INVALID"))
                .isInstanceOf(BookingNotFoundException.class);

        verify(bookingRepository, never()).delete(any());
    }

    @Test
    void getTicketsForBooking_success() {
        bookingEntity.setTickets(TicketFactory.createTickets(2, bookingEntity));
        when(bookingRepository.findByBookingId("BOOK123")).thenReturn(bookingEntity);

        List<Ticket> tickets = bookingService.getTicketsForBooking("BOOK123");

        assertThat(tickets).hasSize(2);
    }

    @Test
    void getTicketsForBooking_notFound() {
        when(bookingRepository.findByBookingId("INVALID")).thenReturn(null);

        assertThatThrownBy(() -> bookingService.getTicketsForBooking("INVALID"))
                .isInstanceOf(BookingNotFoundException.class);
    }
}
