package se.yrgo.bookingservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * DTO to provide ticket data.
 */
public class TicketResponseDTO {
    private String ticketId;
    private String bookingId;
    private double price;
}
