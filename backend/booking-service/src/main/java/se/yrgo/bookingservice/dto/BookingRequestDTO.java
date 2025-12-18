package se.yrgo.bookingservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Represents data to build a booking from the frontend.
 * Data submitted by the user and entities representations
 * from other services.
 */
public class BookingRequestDTO {
    private String eventId;
    private String customerId;
    private int numberOfTickets;
    private boolean refundable;
}
