package se.yrgo.event_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.yrgo.event_service.dtos.*;
import se.yrgo.event_service.exceptions.InsufficientTicketsException;
import se.yrgo.event_service.service.CategoryService;
import se.yrgo.event_service.service.EventService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final CategoryService categoryService;

    public EventController(EventService eventService, CategoryService categoryService) {
        this.eventService = eventService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<EventResponseDTO> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public EventResponseDTO getEvent(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @GetMapping("/event/{eventId}")
    public EventResponseDTO getEventById(@PathVariable String eventId) {
        return eventService.getEventByEventId(eventId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody EventCreateDTO dto) {
        EventResponseDTO response = eventService.createEvent(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable Long id,
            @RequestBody EventCreateDTO dto
    ) {
        EventResponseDTO updated = eventService.updateEvent(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/reserve")
    public ResponseEntity<EventResponseDTO> reserveEvent(@RequestBody ReserveTicketsDTO dto) {
        try {
            EventResponseDTO response = eventService.reserveEvent(dto);
            return ResponseEntity.ok(response);

        }
        catch (NoSuchElementException ne) {
            return ResponseEntity.notFound().build();
        }
        catch (InsufficientTicketsException ie) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories")
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/categories/{id}")
    public CategoryResponseDTO getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }

    @PostMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryCreateDTO dto) {
        CategoryResponseDTO response = categoryService.createCategory(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryCreateDTO dto
    ) {
        CategoryResponseDTO updated = categoryService.updateCategory(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
