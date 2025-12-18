package se.yrgo.event_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit-tests EventServiceProdImpl. Since we have repository- & messaging-interaction, I've mocked them with mockito.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings
class EventServiceTest {

    @Mock
    EventDao eventDao;

    @Mock
    CategoryDao categoryDao;

    @Mock
    EventMessageProducer eventMessageProducer;

    @InjectMocks
    EventServiceProdImpl es;

    @Test
    void testCreateEvent() {
        Category category = new Category("CAT-001", "Music");
        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);

        EventCreateDTO dto = new EventCreateDTO(
                "Concert",
                "Great show",
                "Gothenburg",
                "CAT-001",
                "Artist",
                100,
                100,
                dateTime
        );

        when(categoryDao.findByCategoryId("CAT-001")).thenReturn(Optional.of(category));

        Event saved = new Event(
                "EVT-12345678",
                dto.getName(),
                dto.getDescription(),
                dto.getLocation(),
                category,
                dto.getArtist(),
                dto.getCapacity(),
                dto.getAvailableTickets(),
                dto.getEventDateAndTime()
        );
        when(eventDao.save(any(Event.class))).thenReturn(saved);

        EventResponseDTO result = es.createEvent(dto);

        assertThat(result).isNotNull();
        assertThat(result.eventId()).isEqualTo("EVT-12345678");
        assertThat(result.name()).isEqualTo("Concert");
        assertThat(result.category()).isEqualTo("Music");

        verify(eventDao).save(any(Event.class));
        verify(categoryDao).findByCategoryId("CAT-001");
    }

    @Test
    void testThatCategoryNotFoundExceptionIsThrownWhenCategoryNotFound() {
        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);
        EventCreateDTO dto = new EventCreateDTO(
                "Concert", "Great show", "Gothenburg", "CAT-404",
                "Artist", 100, 100, dateTime
        );

        when(categoryDao.findByCategoryId("CAT-404")).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> es.createEvent(dto));
        verify(eventDao, never()).save(any(Event.class));
    }

    @Test
    void testUpdateEvent() {
        Category oldCategory = new Category("CAT-001", "Music");
        Category newCategory = new Category("CAT-002", "Sports");

        Event existing = new Event(
                "EVT-AAAA1111",
                "Old name",
                "Old desc",
                "Old loc",
                oldCategory,
                "Old artist",
                50,
                50,
                LocalDateTime.now().plusDays(2)
        );

        EventCreateDTO dto = new EventCreateDTO(
                "New name",
                "New desc",
                "New loc",
                "CAT-002",
                "New artist",
                200,
                120,
                LocalDateTime.now().plusDays(5)
        );

        when(eventDao.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryDao.findByCategoryId("CAT-002")).thenReturn(Optional.of(newCategory));

        EventResponseDTO result = es.updateEvent(1L, dto);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("New name");
        assertThat(result.description()).isEqualTo("New desc");
        assertThat(result.location()).isEqualTo("New loc");
        assertThat(result.artist()).isEqualTo("New artist");
        assertThat(result.capacity()).isEqualTo(200);
        assertThat(result.availableTickets()).isEqualTo(120);
        assertThat(result.category()).isEqualTo("Sports");

        verify(eventMessageProducer).sendEventUpdated(existing.getEventId());
    }

    @Test
    void testThatEventNotFoundExceptionIsThrownWhenUpdateEventNotFound() {
        EventCreateDTO dto = new EventCreateDTO(
                "New name", "New desc", "New loc", "CAT-001",
                "New artist", 200, 120, LocalDateTime.now().plusDays(5)
        );

        when(eventDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> es.updateEvent(1L, dto));
        verify(eventMessageProducer, never()).sendEventUpdated(anyString());
    }

    @Test
    void testThatCategorytNotFoundExceptionIsThrownWhenUpdateEventWithWrongCategory() {
        Category oldCategory = new Category("CAT-001", "Music");
        Event existing = new Event(
                "EVT-AAAA1111",
                "Old name",
                "Old desc",
                "Old loc",
                oldCategory,
                "Old artist",
                50,
                50,
                LocalDateTime.now().plusDays(2)
        );

        EventCreateDTO dto = new EventCreateDTO(
                "New name",
                "New desc",
                "New loc",
                "CAT-404",
                "New artist",
                200,
                120,
                LocalDateTime.now().plusDays(5)
        );

        when(eventDao.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryDao.findByCategoryId("CAT-404")).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> es.updateEvent(1L, dto));
        verify(eventMessageProducer, never()).sendEventUpdated(anyString());
    }

    @Test
    void testReserveEvent() {
        Category category = new Category("CAT-001", "Music");
        Event event = new Event(
                "EVT-AAAA1111",
                "Concert",
                "Desc",
                "Loc",
                category,
                "Artist",
                100,
                10,
                LocalDateTime.now().plusDays(2)
        );

        when(eventDao.findByEventId("EVT-AAAA1111")).thenReturn(event);

        ReserveTicketsDTO dto = new ReserveTicketsDTO(3, "EVT-AAAA1111");

        EventResponseDTO result = es.reserveEvent(dto);

        assertThat(result).isNotNull();
        assertThat(result.availableTickets()).isEqualTo(7);
    }

    @Test
    void testThatEventNotFoundExceptionIsThrownWhenReservingMissingEvent() {
        when(eventDao.findByEventId("EVT-404")).thenReturn(null);

        ReserveTicketsDTO dto = new ReserveTicketsDTO(1, "EVT-404");

        assertThrows(EventNotFoundException.class, () -> es.reserveEvent(dto));
    }

    @Test
    void testCancelBooking() {
        Category category = new Category("CAT-001", "Music");
        Event event = new Event(
                "EVT-AAAA1111",
                "Concert",
                "Desc",
                "Loc",
                category,
                "Artist",
                100,
                10,
                LocalDateTime.now().plusDays(2)
        );

        when(eventDao.findByEventId("EVT-AAAA1111")).thenReturn(event);

        ReserveTicketsDTO dto = new ReserveTicketsDTO(4, "EVT-AAAA1111");

        es.cancelBooking(dto);

        assertThat(event.getAvailableTickets()).isEqualTo(14);
    }

    @Test
    void testThatEventNotFondExceptionIsThrownWhenCancellingMissingEvent() {
        when(eventDao.findByEventId("EVT-404")).thenReturn(null);

        ReserveTicketsDTO dto = new ReserveTicketsDTO( 1, "EVT-404");

        assertThrows(EventNotFoundException.class, () -> es.cancelBooking(dto));
    }

    @Test
    void testDeleteEvent() {
        Category category = new Category("CAT-001", "Music");
        Event event = new Event(
                "EVT-AAAA1111",
                "Concert",
                "Desc",
                "Loc",
                category,
                "Artist",
                100,
                10,
                LocalDateTime.now().plusDays(2)
        );

        when(eventDao.findById(1L)).thenReturn(Optional.of(event));

        es.deleteEvent(1L);

        verify(eventDao).deleteById(event.getId());
        verify(eventMessageProducer).sendEventDeleted(event.getEventId());
    }

    @Test
    void testThatEventNotFoundExceptionIsThrownWhenDeletingMissingEvent() {
        when(eventDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> es.deleteEvent(1L));
        verify(eventDao, never()).deleteById(anyLong());
        verify(eventMessageProducer, never()).sendEventDeleted(anyString());
    }

    @Test
    void testGetEventById() {
        Category category = new Category("CAT-001", "Music");
        Event event = new Event(
                "EVT-AAAA1111",
                "Concert",
                "Desc",
                "Loc",
                category,
                "Artist",
                100,
                10,
                LocalDateTime.now().plusDays(2)
        );

        when(eventDao.findById(1L)).thenReturn(Optional.of(event));

        EventResponseDTO result = es.getEventById(1L);

        assertThat(result).isNotNull();
        assertThat(result.eventId()).isEqualTo("EVT-AAAA1111");
    }

    @Test
    void testThatEventNotFoundExceptionIsThrownWhenFindingMissingEvent() {
        when(eventDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> es.getEventById(1L));
    }

    @Test
    void testGetEventByEventId() {
        Category category = new Category("CAT-001", "Music");
        Event event = new Event(
                "EVT-AAAA1111",
                "Concert",
                "Desc",
                "Loc",
                category,
                "Artist",
                100,
                10,
                LocalDateTime.now().plusDays(2)
        );

        when(eventDao.findByEventId("EVT-AAAA1111")).thenReturn(event);

        EventResponseDTO result = es.getEventByEventId("EVT-AAAA1111");

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Concert");
    }

    @Test
    void testThatEventNotFoundExceptionIsThrownWhenFindingMissingEventId() {
        when(eventDao.findByEventId("EVT-404")).thenReturn(null);

        assertThrows(EventNotFoundException.class, () -> es.getEventByEventId("EVT-404"));
    }

    @Test
    void testGetAllEvents() {
        Category category = new Category("CAT-001", "Music");
        Event e1 = new Event(
                "EVT-AAAA1111",
                "Concert",
                "Desc",
                "Loc",
                category,
                "Artist",
                100,
                10,
                LocalDateTime.now().plusDays(2)
        );

        Event e2 = new Event(
                "EVT-BBBB2222",
                "Match",
                "Desc2",
                "Loc2",
                category,
                "Team",
                200,
                50,
                LocalDateTime.now().plusDays(3)
        );

        when(eventDao.findAll()).thenReturn(List.of(e1, e2));

        List<EventResponseDTO> result = es.getAllEvents();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).eventId()).isEqualTo("EVT-AAAA1111");
        assertThat(result.get(1).eventId()).isEqualTo("EVT-BBBB2222");
    }
}