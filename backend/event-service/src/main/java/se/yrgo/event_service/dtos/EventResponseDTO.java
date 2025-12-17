package se.yrgo.event_service.dtos;

import java.time.LocalDateTime;

public record EventResponseDTO(Long id, String eventId, String name, String description, String location,
                               String category, String artist, int capacity, int availableTickets,
                               LocalDateTime eventDateAndTime, LocalDateTime createdAt) {
}
