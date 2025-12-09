package se.yrgo.user_service.domain;

import jakarta.persistence.*;
import java.util.*;
import java.time.LocalDate;

@Entity
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String customerId;
    private String name;
    private String email;
    private LocalDate birthdate;
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();

    public User() {

    }

    public User(String customerId, String name, String email, LocalDate birthdate) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.birthdate = birthdate;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

}
