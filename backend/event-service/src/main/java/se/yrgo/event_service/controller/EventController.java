package se.yrgo.event_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.yrgo.event_service.dtos.*;
import se.yrgo.event_service.exceptions.CategoryNotFoundException;
import se.yrgo.event_service.exceptions.EventNotFoundException;
import se.yrgo.event_service.exceptions.InsufficientTicketsException;
import se.yrgo.event_service.service.CategoryService;
import se.yrgo.event_service.service.EventService;

import java.util.List;

/**
 * This controller handles the interaction with events.
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final CategoryService categoryService;

    public EventController(EventService eventService, CategoryService categoryService) {
        this.eventService = eventService;
        this.categoryService = categoryService;
    }

    /**
     * Looks up all events
     * @return all events
     */
    @GetMapping
    public List<EventResponseDTO> getAllEvents() {
        return eventService.getAllEvents();
    }

    /**
     * Looks up an event
     * @param id of the event to be found
     * @return the event with given id (database-id) else 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable Long id) {
        try {
            EventResponseDTO response = eventService.getEventById(id);
            return ResponseEntity.ok(response);
        }
        catch (EventNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Looks up an event
     * @param eventId of the event to be found
     * @return the event with given eventId (non-database id), else 404
     */
    @GetMapping("/event/{eventId}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable String eventId) {
        try {
            EventResponseDTO response = eventService.getEventByEventId(eventId);
            return ResponseEntity.ok(response);
        } catch (EventNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates an event (only for admin)
     * @param dto is the data of the event to be created
     * @return 201 if successful
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody EventCreateDTO dto) {
        EventResponseDTO response = eventService.createEvent(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     *  Updates an event (only for admin)
     * @param id of the event to update
     * @param dto the new data for the event
     * @return 200 if ok, else 404
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable Long id,
            @RequestBody EventCreateDTO dto
    ) {
        try {
            EventResponseDTO updated = eventService.updateEvent(id, dto);
            return ResponseEntity.ok(updated);
        } catch (EventNotFoundException | CategoryNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Makes a reservation for an event by modifying available tickets.
     * @param dto is data with (number of tickets + eventId)
     * @return 200 if ok, 404 if not found, 400 if wrong input of tickets
     */
    @PutMapping("/reserve")
    public ResponseEntity<EventResponseDTO> reserveEvent(@RequestBody ReserveTicketsDTO dto) {
        try {
            EventResponseDTO response = eventService.reserveEvent(dto);
            return ResponseEntity.ok(response);

        } catch (EventNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (InsufficientTicketsException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Cancels ticket-reservation for an event by increasing available tickets.
     * @param dto is data with (number of tickets + eventId)
     * @return 201 if ok, 404 if not found, 400 if wrong input of tickets
     */
    @PutMapping("/cancel")
    public ResponseEntity<Void> cancelBooking(@RequestBody ReserveTicketsDTO dto) {
        try {
            eventService.cancelBooking(dto);
            return ResponseEntity.noContent().build();

        } catch (EventNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (InsufficientTicketsException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Deletes an event (only for admin)
     * @param id of the event to delete
     * @return 201 if successful, 404 if not found
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.noContent().build();
        } catch (EventNotFoundException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Return all categories
     * @return all event-categories
     */
    @GetMapping("/categories")
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * Returns a specific category
     * @param id of the catefory to return
     * @return 200 if successful, else 404 if not found
     */
    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategory(@PathVariable Long id) {
        try {
            CategoryResponseDTO response = categoryService.getCategory(id);
            return ResponseEntity.ok(response);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create a new categoru (only for admin)
     * @param dto with data of the category
     * @return 201 if successful
     */
    @PostMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryCreateDTO dto) {
        CategoryResponseDTO response = categoryService.createCategory(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update a category (only for admin)
     * @param id of the category to update
     * @param dto with new data for the category
     * @return 200 if successful, else 404 if not found
     */
    @PutMapping("/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryCreateDTO dto
    ) {
        try {
            CategoryResponseDTO updated = categoryService.updateCategory(id, dto);
            return ResponseEntity.ok(updated);
        } catch (CategoryNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a category (only for admin)
     * @param id of the category to delete
     * @return 201 if successful, else 404 if not found
     */
    @DeleteMapping("/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (CategoryNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
