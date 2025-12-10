package se.yrgo.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class BookingCreateDTO {
    private String eventId;
    private String customerId;
    private int numberOfTickets;
    private boolean isRefundable;
}
