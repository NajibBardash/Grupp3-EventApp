package se.yrgo.bookingservice.factory;

import org.junit.jupiter.api.Test;
import se.yrgo.bookingservice.domain.Booking;
import se.yrgo.bookingservice.domain.Ticket;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TicketFactoryTest {

    Booking booking = Booking.builder()
            .dateOfBooking(LocalDateTime.now())
            .refundable(false)
            .eventId("EVT123")
            .customerId("USR123")
            .build();

    @Test
    void createTickets_createsCorrectAmountWithBookingAndPrice() {
        // act
        List<Ticket> tickets = TicketFactory.createTickets(10, booking);

        // assert
        assertThat(tickets).isNotNull();
        assertThat(tickets).hasSize(10);

        assertThat(tickets).allSatisfy(ticket -> {
            assertThat(ticket).isNotNull();
            assertThat(ticket.getBooking()).isSameAs(booking);
            assertThat(ticket.getPrice()).isEqualTo(0);
        });
    }
}
