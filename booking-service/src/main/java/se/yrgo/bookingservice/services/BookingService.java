package se.yrgo.bookingservice.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.bookingservice.domain.Ticket;
import se.yrgo.bookingservice.dto.BookingRequestDTO;
import se.yrgo.bookingservice.dto.BookingResponseDTO;

import java.util.List;

@Service
@Transactional
public interface BookingService {
    BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO);
    BookingResponseDTO getBookingById(String bookingId);
    List<BookingResponseDTO> getAllBookings();
    void deleteBooking(String bookingId);
    BookingResponseDTO editBooking(String bookingId, BookingRequestDTO bookingRequestDTO);
    List<Ticket> getTicketsForBooking(String bookingId);
}
