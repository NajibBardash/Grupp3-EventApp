package se.yrgo.event_service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.event_service.dataaccess.CategoryDao;
import se.yrgo.event_service.dataaccess.EventDao;
import se.yrgo.event_service.domain.Category;
import se.yrgo.event_service.domain.Event;
import se.yrgo.event_service.dtos.EventCreateDTO;
import se.yrgo.event_service.dtos.EventResponseDTO;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventServiceProdImpl implements EventService {

    private final EventDao eventDao;
    private final CategoryDao categoryDao;

    public EventServiceProdImpl(EventDao eventDao, CategoryDao categoryDao) {
        this.eventDao = eventDao;
        this.categoryDao = categoryDao;
    }

    @Override
    @Transactional
    public EventResponseDTO createEvent(EventCreateDTO dto) {
        Event event = mapToEntity(dto);
        Event saved = eventDao.save(event);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public EventResponseDTO updateEvent(Long id, EventCreateDTO dto) {

        Event event = eventDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setLocation(dto.getLocation());
        event.setArtist(dto.getArtist());
        event.setCapacity(dto.getCapacity());
        event.setEventDateAndTime(dto.getEventDateAndTime());

        Category category = categoryDao.findByCategoryId(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        event.setCategory(category);

        return mapToResponse(event);
    }


    @Override
    @Transactional
    public void deleteEvent(Long id) {
        eventDao.deleteById(id);
    }

    @Override
    public EventResponseDTO getEventById(Long id) {
        Event event = eventDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        return mapToResponse(event);
    }

    @Override
    public List<EventResponseDTO> getAllEvents() {
        List<Event> events = eventDao.findAll();
        List<EventResponseDTO> eventResponseDTOS = new ArrayList<>();

        for (Event event : events) {
            eventResponseDTOS.add(mapToResponse(event));
        }

        return eventResponseDTOS;
    }

    private Event mapToEntity(EventCreateDTO dto) {
        Category category = categoryDao.findByCategoryId(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return new Event(
                generateEventId(),
                dto.getName(),
                dto.getDescription(),
                dto.getLocation(),
                category,
                dto.getArtist(),
                dto.getCapacity(),
                dto.getAvailableTickets(),
                dto.getEventDateAndTime()
        );
    }

    private EventResponseDTO mapToResponse(Event event) {
        return new EventResponseDTO(
                event.getId(),
                event.getEventId(),
                event.getName(),
                event.getDescription(),
                event.getLocation(),
                event.getCategory().getType(),
                event.getArtist(),
                event.getCapacity(),
                event.getAvailableTickets(),
                event.getEventDateAndTime(),
                event.getCreatedAt()
        );
    }

    private String generateEventId() {
        return "EVT-" + java.util.UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }
}
