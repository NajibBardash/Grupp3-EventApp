package se.yrgo.event_service.dtos;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventCreateDTO {
    private String name;
    private String description;
    private String location;
    private String categoryId;
    private String artist;
    private int capacity;
    private int availableTickets;
    private LocalDateTime eventDateAndTime;

    public EventCreateDTO() {}

    public EventCreateDTO(
            String name,
            String description,
            String location,
            String categoryId,
            String artist,
            int capacity,
            int availableTickets,
            LocalDateTime eventDateAndTime
    ) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.categoryId = categoryId;
        this.artist = artist;
        this.capacity = capacity;
        this.availableTickets = availableTickets;
        this.eventDateAndTime = eventDateAndTime;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public int getAvailableTickets() { return availableTickets; }

    public void setAvailableTickets(int availableTickets) { this.availableTickets = availableTickets; }

    public LocalDateTime getEventDateAndTime() {
        return eventDateAndTime;
    }

    public void setEventDateAndTime(LocalDateTime eventDateAndTime) {
        this.eventDateAndTime = eventDateAndTime;
    }
}
