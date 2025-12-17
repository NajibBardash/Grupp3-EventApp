package se.yrgo.event_service.service;


import se.yrgo.event_service.dtos.EventCreateDTO;
import se.yrgo.event_service.dtos.EventResponseDTO;
import se.yrgo.event_service.dtos.ReserveTicketsDTO;

import java.util.List;

public interface EventService {
    EventResponseDTO createEvent(EventCreateDTO eventCreateDTO);
    EventResponseDTO updateEvent(Long id, EventCreateDTO dto);
    EventResponseDTO reserveEvent(ReserveTicketsDTO dto);
    void cancelBooking(ReserveTicketsDTO dto);
    void deleteEvent(Long id);
    EventResponseDTO getEventById(Long id);
    EventResponseDTO getEventByEventId(String eventId);
    List<EventResponseDTO> getAllEvents();
}
