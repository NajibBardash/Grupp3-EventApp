package se.yrgo.event_service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.event_service.dataaccess.CategoryDao;
import se.yrgo.event_service.dataaccess.EventDao;
import se.yrgo.event_service.domain.Category;
import se.yrgo.event_service.domain.Event;
import se.yrgo.event_service.dtos.EventCreateDTO;
import se.yrgo.event_service.dtos.EventResponseDTO;
import se.yrgo.event_service.dtos.ReserveTicketsDTO;
import se.yrgo.event_service.exceptions.CategoryNotFoundException;
import se.yrgo.event_service.exceptions.EventNotFoundException;
import se.yrgo.event_service.messaging.EventMessageProducer;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventServiceProdImpl implements EventService {

    private final EventDao eventDao;
    private final CategoryDao categoryDao;
    private final EventMessageProducer eventMessageProducer;

    public EventServiceProdImpl(EventDao eventDao, CategoryDao categoryDao, EventMessageProducer eventMessageProducer) {
        this.eventDao = eventDao;
        this.categoryDao = categoryDao;
        this.eventMessageProducer = eventMessageProducer;
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
                .orElseThrow(() -> new EventNotFoundException("Event not found with id " + id));

        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setLocation(dto.getLocation());
        event.setArtist(dto.getArtist());
        event.setCapacity(dto.getCapacity());
        event.setAvailableTickets(dto.getAvailableTickets());
        event.setEventDateAndTime(dto.getEventDateAndTime());

        Category category = categoryDao.findByCategoryId(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id " + dto.getCategoryId()));
        event.setCategory(category);

        EventResponseDTO responseDTO = mapToResponse(event);
        eventMessageProducer.sendEventUpdated(responseDTO.eventId());

        return responseDTO;
    }

    @Override
    @Transactional
    public EventResponseDTO reserveEvent(ReserveTicketsDTO dto) {
        Event event = eventDao.findByEventId(dto.eventId());
        if (event == null) {
            throw new EventNotFoundException("Event not found with id " + dto.eventId());
        }

        event.decreaseAvailableTickets(dto.amount());
        return mapToResponse(event);
    }

    @Override
    @Transactional
    public void cancelBooking(ReserveTicketsDTO dto) {
        Event event = eventDao.findByEventId(dto.eventId());
        if (event == null) {
            throw new EventNotFoundException("Event not found with id " + dto.eventId());
        }

        event.increaseAvailableTickets(dto.amount());
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventDao.findById(id).orElseThrow(() -> new EventNotFoundException("Event not found with id " + id));
        eventDao.deleteById(event.getId());

        eventMessageProducer.sendEventDeleted(event.getEventId());
    }

    @Override
    public EventResponseDTO getEventById(Long id) {
        Event event = eventDao.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id " + id));

        return mapToResponse(event);
    }

    @Override
    public EventResponseDTO getEventByEventId(String eventId) {
        Event event = eventDao.findByEventId(eventId);
        if (event == null) {
            throw new EventNotFoundException("Event not found with EventId " + eventId);
        }
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
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id " + dto.getCategoryId()));

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
