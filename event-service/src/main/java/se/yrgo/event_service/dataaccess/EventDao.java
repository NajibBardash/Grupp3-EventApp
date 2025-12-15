package se.yrgo.event_service.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.yrgo.event_service.domain.Category;
import se.yrgo.event_service.domain.Event;
import se.yrgo.event_service.dtos.EventResponseDTO;

import java.util.Optional;

public interface EventDao extends JpaRepository<Event, Long> {
    Optional<Event> findByEventId(String eventId);
}
