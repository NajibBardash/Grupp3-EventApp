import React, { useEffect, useState } from "react";
import { getAllEvents, type EventDTO } from "../api/UserFetchingService";

function Events() {
  const [events, setEvents] = useState<EventDTO[]>([]);
  const [error, setError] = useState("");

  useEffect(() => {
    getAllEvents()
      .then(setEvents)
      .catch(() => setError("Kunde inte hämta events"));
  }, []);

  function handleClick(event: EventDTO) {
    <Link to="/event">Event</Link>;
  }

  return (
    <div>
      <p>All of the events</p>
      {error && <p>{error}</p>}

      <ul>
        {events.map((event) => (
          <li key={event.id} onClick={handleClick(event)}>
            {event.name}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Events;
