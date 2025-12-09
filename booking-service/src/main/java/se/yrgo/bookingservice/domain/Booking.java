package se.yrgo.bookingservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String bookingId;
    private LocalDateTime dateOfBooking;

    private String customerId;
    private String eventID;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    private boolean refundable;

    public Booking() {}


}
