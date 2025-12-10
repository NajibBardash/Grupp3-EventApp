package se.yrgo.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.yrgo.bookingservice.domain.Ticket;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingResponseDTO {
    private String bookingId;
    private LocalDateTime bookingDate;
    private String customerId;
    private String eventId;
    private List<Ticket> tickets;
    private boolean refundable;
}
