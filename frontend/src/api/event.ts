import axios from "axios";

// Event service base URL
const API_BASE = "http://localhost:8081/api/events";

export interface EventDTO {
  id: number;
  name: string;
  date: string;
  location: string;
  availableTickets: number;
  category: string;
}

// Get all events
export const getEvents = async (): Promise<EventDTO[]> => {
  try {
    const res = await axios.get<EventDTO[]>(API_BASE);
    return res.data;
  } catch (err) {
    console.error("Failed to fetch events:", err);
    return [];
  }
};

// Get single event by id
export const getEvent = async (id: number): Promise<EventDTO | null> => {
  try {
    const res = await axios.get<EventDTO>(`${API_BASE}/${id}`);
    return res.data;
  } catch (err) {
    console.error("Failed to fetch event:", err);
    return null;
  }
};

// If you need categories
export const getCategories = async (): Promise<
  { id: number; name: string }[]
> => {
  try {
    const res = await axios.get(`${API_BASE}/categories`);
    return res.data;
  } catch (err) {
    console.error("Failed to fetch categories:", err);
    return [];
  }
};
