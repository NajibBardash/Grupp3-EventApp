package se.yrgo.event_service.domain;

import jakarta.persistence.*;
import se.yrgo.event_service.exceptions.InsufficientTicketsException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This class represents an event-entity.
 * Has a many-to-one relationship to category.
 * Each category has a column in the event-table with the categoryId.
 */
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventId;
    private String name;
    private String description;
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String artist;
    private int capacity;
    private int availableTickets;
    private LocalDateTime eventDateAndTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Event() {
    }

    public Event(
            String eventId,
            String name,
            String description,
            String location,
            Category category,
            String artist,
            int capacity,
            int availableTickets,
            LocalDateTime eventDateAndTime) {
        this.eventId = eventId;
        this.name = name;
        this.description = description;
        this.location = location;
        this.category = category;
        this.artist = artist;
        this.capacity = capacity;
        this.availableTickets = availableTickets;
        this.eventDateAndTime = eventDateAndTime;
    }

    public Long getId() {
        return id;
    }

    public String getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    /**
     * When a reservation for an event is made, the number of available tickets is decreased.
     * If the number of reserved tickets is greater than the number of available -> exception
     * If the number of reserved tickets is less than 1 -> exception
     * @param decrease the number available tickets with the decrease-amount
     */
    public void decreaseAvailableTickets(int decrease) {
        if (decrease > this.availableTickets) {
            throw new InsufficientTicketsException("There are fewer tickets available than you have requested.");
        } else if (decrease <= 0) {
            throw new IllegalArgumentException("You can't book less than 1 ticket.");
        } else {
            this.availableTickets -= decrease;
        }
    }

    /**
     * When a reservation for an event is cancelled, the number of available tickets is increased.
     * If the number of cancelled tickets is greater than the number of booked -> exception
     * If the number of cancelled tickets is less than 1 -> exception
     * @param increase the number available tickets with the increase-amount
     */
    public void increaseAvailableTickets(int increase) {
        if (increase > (this.capacity - this.availableTickets)) {
            throw new InsufficientTicketsException("Can't return more tickets than there are tickets booked.");
        }
        else if (increase <= 0) {
            throw new IllegalArgumentException("You can't return less than 1 ticket.");
        }
        this.availableTickets += increase;
    }

    public LocalDateTime getEventDateAndTime() {
        return eventDateAndTime;
    }

    public void setEventDateAndTime(LocalDateTime eventDateAndTime) {
        this.eventDateAndTime = eventDateAndTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * When an event is created, the entity gets a timestamp of when it was created and updated
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    /**
     * When an event is uodated, the entity gets a timestamp of when it was updated
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Event event)) return false;
        return Objects.equals(id, event.id) && Objects.equals(eventId, event.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventId);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", eventId='" + eventId + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", categoryId=" + (category != null ? category.getCategoryId() : null) +
                ", artist='" + artist + '\'' +
                ", capacity=" + capacity +
                ", availableTickets=" + availableTickets +
                ", eventDateAndTime=" + eventDateAndTime +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
