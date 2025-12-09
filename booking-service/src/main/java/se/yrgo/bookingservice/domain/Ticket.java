package se.yrgo.bookingservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String ticketId;
    private double price;

    @ManyToOne
    private Booking booking;

    public Ticket() {}
}
