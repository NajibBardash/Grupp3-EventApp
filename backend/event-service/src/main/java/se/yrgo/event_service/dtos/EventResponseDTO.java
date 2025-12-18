package se.yrgo.event_service.dtos;

import java.time.LocalDateTime;

/**
 * A dto-class when returning an event from the database
 * @param id of the event (database)
 * @param eventId of the event
 * @param name of the event
 * @param description of the event
 * @param location of the event
 * @param category of the event
 * @param artist of the event
 * @param capacity of the event
 * @param availableTickets to the evenbt
 * @param eventDateAndTime of the event
 * @param createdAt timestamp of event
 */
public record EventResponseDTO(Long id, String eventId, String name, String description, String location,
                               String category, String artist, int capacity, int availableTickets,
                               LocalDateTime eventDateAndTime, LocalDateTime createdAt) {
}
