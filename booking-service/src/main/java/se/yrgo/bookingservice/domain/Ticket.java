package se.yrgo.bookingservice.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String ticketId;
    private double price;

    @ManyToOne
    private Booking booking;

    // Makes sure the entity gets a legit ID before persisting
    @PrePersist
    private void init() {
        if (ticketId == null) {
            ticketId = generateTicketId();
        }
    }

    private String generateTicketId() {
        return "TKT-" + java.util.UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }
}
