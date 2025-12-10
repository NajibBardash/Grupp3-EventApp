package se.yrgo.bookingservice.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.bookingservice.domain.Booking;
import se.yrgo.bookingservice.dto.BookingCreateDTO;
import se.yrgo.bookingservice.dto.BookingResponseDTO;

import java.util.List;

@Service
@Transactional
public interface BookingService {
    BookingResponseDTO createBooking(BookingCreateDTO bookingCreateDTO);
    BookingResponseDTO getBooking(String bookingId);
    List<BookingResponseDTO> getAllBookings();
    void deleteBooking(String bookingId);
}
