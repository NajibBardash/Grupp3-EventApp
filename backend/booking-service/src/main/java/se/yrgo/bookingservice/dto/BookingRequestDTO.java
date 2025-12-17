package se.yrgo.bookingservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {
    private String eventId;
    private String customerId;
    private int numberOfTickets;
    private boolean refundable;
}
