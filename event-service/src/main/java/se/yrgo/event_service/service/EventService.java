package se.yrgo.event_service.service;


import se.yrgo.event_service.dtos.EventCreateDTO;
import se.yrgo.event_service.dtos.EventResponseDTO;

import java.util.List;

public interface EventService {
    EventResponseDTO createEvent(EventCreateDTO eventCreateDTO);
    EventResponseDTO updateEvent(Long id, EventCreateDTO dto);
    void deleteEvent(Long id);
    EventResponseDTO getEvent(Long id);
    List<EventResponseDTO> getAllEvents();
}
