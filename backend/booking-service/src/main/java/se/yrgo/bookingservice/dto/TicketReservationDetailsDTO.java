package se.yrgo.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * DTO for integration between booking and event.
 * sent via REST to either reserve tickets for an event
 * or cancel a reservation.
 */
public class TicketReservationDetailsDTO {
    private int amount;
    private String eventId;
}
