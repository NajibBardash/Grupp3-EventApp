package se.yrgo.bookingservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String bookingId;
    private LocalDateTime dateOfBooking;

    private String customerId;
    private String eventId;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    private boolean isRefundable;

    // Makes sure the entity gets a legit ID before persisting
    @PrePersist
    private void init() {
        if (bookingId == null) {
            bookingId = generateBookingId();
        }
    }

    private String generateBookingId() {
        return "BKG-" + java.util.UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }
}
