export interface EventDTO {
  id: number;
  eventId: string;
  name: string;
  description: string;
  location: string;
  category: string;
  artist: string;
  capacity: number;
  availableTickets: number;
  eventDateAndTime: string;
  createdAt: string;
}

export async function getAllEvents(): Promise<EventDTO[]> {
  const res = await fetch("http://localhost:8081/api/events");

  if (!res.ok) {
    throw new Error("Kunde inte hämta events");
  }

  const data: EventDTO[] = await res.json();
  return data;
}
