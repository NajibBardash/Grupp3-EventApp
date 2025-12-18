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

export interface UserDTO {
  id: number;
  customerId: string;
  username: string;
  name: string;
  email: string;
  birthdate: string;
}

export interface BookingDTO {
  bookingId: string;
  bookingDate: string;
  customerId: string;
  eventId: string;
  tickets: TicketDTO[];
  refundable: boolean;
}

export interface TicketDTO {
  ticketId: string;
  bookingId: string;
  price: number;
}

export async function getAllEvents(): Promise<EventDTO[]> {
  const res = await fetch("http://localhost:8081/api/events");

  if (!res.ok) {
    throw new Error("Kunde inte hämta events");
  }

  const data: EventDTO[] = await res.json();
  return data;
}

export async function getEventById(eventId: string): Promise<EventDTO> {
  const res = await fetch(`http://localhost:8081/api/events/event/${eventId}`);

  if (!res.ok) {
    throw new Error("Kunde inte hämta event");
  }

  const data: EventDTO = await res.json();
  return data;
}

export async function createBooking(
  eventId: string,
  customerId: string,
  numberOfTickets: number,
  authHeader: string
): Promise<BookingDTO> {
  const res = await fetch("http://localhost:8082/api/booking", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: authHeader,
    },
    body: JSON.stringify({
      eventId,
      customerId,
      numberOfTickets,
      refundable: true,
    }),
  });

  if (!res.ok) {
    const error = await res.text();
    throw new Error(error || "Kunde inte skapa bokning");
  }

  const data: BookingDTO = await res.json();
  return data;
}

export async function getAllBookings(authHeader: string): Promise<BookingDTO[]> {
  const res = await fetch("http://localhost:8082/api/booking", {
    headers: {
      Authorization: authHeader,
    },
  });

  if (!res.ok) {
    throw new Error("Kunde inte hämta bokningar");
  }

  const data: BookingDTO[] = await res.json();
  return data;
}

export async function deleteBooking(
  bookingId: string,
  authHeader: string
): Promise<void> {
  const res = await fetch(`http://localhost:8082/api/booking/${bookingId}`, {
    method: "DELETE",
    headers: {
      Authorization: authHeader,
    },
  });

  if (!res.ok) {
    throw new Error("Kunde inte ta bort bokning");
  }
}

// Admin Event Management
export interface EventCreateDTO {
  name: string;
  description: string;
  location: string;
  categoryId: string;
  artist: string;
  capacity: number;
  availableTickets: number;
  eventDateAndTime: string;
}

export async function createEvent(
  eventData: EventCreateDTO,
  authHeader: string
): Promise<EventDTO> {
  const res = await fetch("http://localhost:8081/api/events", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: authHeader,
    },
    body: JSON.stringify(eventData),
  });

  if (!res.ok) {
    const error = await res.text();
    throw new Error(error || "Kunde inte skapa event");
  }

  const data: EventDTO = await res.json();
  return data;
}

export async function updateEvent(
  eventId: number,
  eventData: EventCreateDTO,
  authHeader: string
): Promise<EventDTO> {
  const res = await fetch(`http://localhost:8081/api/events/${eventId}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: authHeader,
    },
    body: JSON.stringify(eventData),
  });

  if (!res.ok) {
    const error = await res.text();
    throw new Error(error || "Kunde inte uppdatera event");
  }

  const data: EventDTO = await res.json();
  return data;
}

export async function deleteEvent(
  eventId: number,
  authHeader: string
): Promise<void> {
  const res = await fetch(`http://localhost:8081/api/events/${eventId}`, {
    method: "DELETE",
    headers: {
      Authorization: authHeader,
    },
  });

  if (!res.ok) {
    const error = await res.text();
    throw new Error(error || "Kunde inte ta bort event");
  }
}

// Categories
export interface CategoryDTO {
  id: number;
  categoryId: string;
  type: string;
}

export async function getAllCategories(): Promise<CategoryDTO[]> {
  const res = await fetch("http://localhost:8081/api/events/categories");

  if (!res.ok) {
    throw new Error("Kunde inte hämta kategorier");
  }

  const data: CategoryDTO[] = await res.json();
  return data;
}

// Admin Category Management
export interface CategoryCreateDTO {
  type: string;
}

export async function createCategory(
  categoryData: CategoryCreateDTO,
  authHeader: string
): Promise<CategoryDTO> {
  const res = await fetch("http://localhost:8081/api/events/categories", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: authHeader,
    },
    body: JSON.stringify(categoryData),
  });

  if (!res.ok) {
    const error = await res.text();
    throw new Error(error || "Kunde inte skapa kategori");
  }

  const data: CategoryDTO = await res.json();
  return data;
}

export async function updateCategory(
  categoryId: number,
  categoryData: CategoryCreateDTO,
  authHeader: string
): Promise<CategoryDTO> {
  const res = await fetch(`http://localhost:8081/api/events/categories/${categoryId}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: authHeader,
    },
    body: JSON.stringify(categoryData),
  });

  if (!res.ok) {
    const error = await res.text();
    throw new Error(error || "Kunde inte uppdatera kategori");
  }

  const data: CategoryDTO = await res.json();
  return data;
}

export async function deleteCategory(
  categoryId: number,
  authHeader: string
): Promise<void> {
  const res = await fetch(`http://localhost:8081/api/events/categories/${categoryId}`, {
    method: "DELETE",
    headers: {
      Authorization: authHeader,
    },
  });

  if (!res.ok) {
    const error = await res.text();
    throw new Error(error || "Kunde inte ta bort kategori");
  }
}
