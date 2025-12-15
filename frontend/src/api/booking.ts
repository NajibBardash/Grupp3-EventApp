import axios from "axios";

const API_BASE = "http://localhost:8082/api/booking"; // adjust port if needed

export interface BookingRequestDTO {
  eventId: string;
  customerId: string;
  numberOfTickets: number;
  refundable: boolean;
}

export interface TicketResponseDTO {
  ticketId: string;
  bookingId: string;
  price: number;
}

export interface BookingResponseDTO {
  bookingId: string;
  dateOfBooking: string;
  customerId: string;
  eventId: string;
  tickets: TicketResponseDTO[];
  refundable: boolean;
}

export const createBooking = async (booking: BookingRequestDTO) => {
  const res = await axios.post<BookingResponseDTO>(API_BASE, booking);
  return res.data;
};

export const getBookingById = async (bookingId: string) => {
  const res = await axios.get<BookingResponseDTO>(`${API_BASE}/${bookingId}`);
  return res.data;
};

export const getAllBookings = async () => {
  const res = await axios.get<BookingResponseDTO[]>(API_BASE);
  return res.data;
};
