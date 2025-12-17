package se.yrgo.event_service.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import se.yrgo.event_service.domain.Event;

public interface EventDao extends JpaRepository<Event, Long> {
    Event findByEventId(String eventId);
}
