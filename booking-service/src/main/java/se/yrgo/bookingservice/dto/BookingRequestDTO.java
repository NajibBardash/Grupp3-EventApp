package se.yrgo.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {
    private String eventId;
    private String customerId;
    private int numberOfTickets;
    private boolean refundable;
}
