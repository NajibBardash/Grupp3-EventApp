package se.yrgo.bookingservice.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import se.yrgo.bookingservice.dto.BookingRequestDTO;
import se.yrgo.bookingservice.services.BookingService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BookingControllerTest {

    @Mock
    BookingService bookingService;

    BookingRequestDTO validRequest = new BookingRequestDTO();


    @Test
    void registerNewBooking() {
        BookingController controller = new BookingController(bookingService);

    }

    @Test
    void getBooking() {
    }

    @Test
    void getAllBookings() {
    }

    @Test
    void deleteBooking() {
    }
}