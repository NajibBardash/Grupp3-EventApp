package se.yrgo.bookingservice.dto;

import lombok.*;
import se.yrgo.bookingservice.domain.Ticket;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * DTO to return full context data for a booking, except database PK.
 */
public class BookingResponseDTO {
    private String bookingId;
    private LocalDateTime bookingDate;
    private String customerId;
    private String eventId;
    private List<TicketResponseDTO> tickets;
    private boolean refundable;
}
