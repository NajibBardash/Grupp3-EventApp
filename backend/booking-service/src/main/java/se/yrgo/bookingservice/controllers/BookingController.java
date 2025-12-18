package se.yrgo.bookingservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.yrgo.bookingservice.dto.BookingRequestDTO;
import se.yrgo.bookingservice.dto.BookingResponseDTO;
import se.yrgo.bookingservice.exceptions.booking.BookingFailedException;
import se.yrgo.bookingservice.services.BookingService;
import se.yrgo.bookingservice.services.BookingServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponseDTO> registerNewBooking(@RequestBody BookingRequestDTO bookingRequestDTO) {

        try {
            BookingResponseDTO created =  bookingService.createBooking(bookingRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (BookingFailedException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/{bookingId}")
    public BookingResponseDTO getBooking(@PathVariable String bookingId) {
        if (bookingId != null) {
            return bookingService.getBookingById(bookingId);
        }
        return null;
    }

    @GetMapping
    public List<BookingResponseDTO> getAllBookings() {
        return bookingService.getAllBookings();
    }

//    @PutMapping("/{bookingId}")
//    public BookingResponseDTO updateBooking(@PathVariable String bookingId, @RequestBody BookingRequestDTO bookingRequestDTO) {
//        return null;
//    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable String bookingId) {
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.noContent().build();
    }
}
