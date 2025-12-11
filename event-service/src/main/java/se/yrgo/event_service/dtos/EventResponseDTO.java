package se.yrgo.event_service.dtos;

import java.time.LocalDateTime;

public class EventResponseDTO {
    private Long id;
    private String eventId;
    private String name;
    private String description;
    private String location;
    private String category;
    private String artist;
    private int capacity;
    private int availableTickets;
    private LocalDateTime eventDateAndTime;
    private LocalDateTime createdAt;

    public EventResponseDTO() {
    }

    public EventResponseDTO(
            Long id,
            String eventId,
            String name,
            String description,
            String location,
            String category,
            String artist,
            int capacity,
            int availableTickets,
            LocalDateTime eventDateAndTime,
            LocalDateTime createdAt) {
        this.id = id;
        this.eventId = eventId;
        this.name = name;
        this.description = description;
        this.location = location;
        this.category = category;
        this.artist = artist;
        this.capacity = capacity;
        this.availableTickets = availableTickets;
        this.eventDateAndTime = eventDateAndTime;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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

    public LocalDateTime getEventDateAndTime() {
        return eventDateAndTime;
    }

    public void setEventDateAndTime(LocalDateTime eventDateAndTime) {
        this.eventDateAndTime = eventDateAndTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
