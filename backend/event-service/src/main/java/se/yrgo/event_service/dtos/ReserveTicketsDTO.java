package se.yrgo.event_service.dtos;

/**
 * Data when reserving/cancelling an event
 * @param amount of tickets to change
 * @param eventId of the event to change
 */
public record ReserveTicketsDTO(int amount, String eventId) {}
