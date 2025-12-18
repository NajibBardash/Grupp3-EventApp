package se.yrgo.bookingservice.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import se.yrgo.bookingservice.dto.BookingRequestDTO;
import se.yrgo.bookingservice.dto.BookingResponseDTO;
import se.yrgo.bookingservice.exceptions.booking.BookingFailedException;
import se.yrgo.bookingservice.exceptions.booking.BookingNotFoundException;
import se.yrgo.bookingservice.factory.TicketFactory;
import se.yrgo.bookingservice.services.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    BookingService bookingService;

    @InjectMocks
    BookingController controller;


    // Mock data
    BookingRequestDTO request = new BookingRequestDTO();

    BookingResponseDTO expectedResponse =
            new BookingResponseDTO(
                    "valid",
                    null,
                    "cust1",
                    "evt1",
                    List.of(),
                    false
            );


    @Test
    void testValidBooking() {
        when(bookingService.createBooking(any())).thenReturn(expectedResponse);

        ResponseEntity<BookingResponseDTO> response = controller.registerNewBooking(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(bookingService).createBooking(request);
    }

    @Test
    void testExceptionsBeingThrownCorrectly() {
        when(bookingService.createBooking(any())).thenThrow(new BookingFailedException("fail"));

        ResponseEntity<BookingResponseDTO> response = controller.registerNewBooking(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getExistingBooking() {
        when(bookingService.getBookingById(any())).thenReturn(expectedResponse);

        ResponseEntity<BookingResponseDTO> response = controller.getBooking("id");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(bookingService).getBookingById(any());
    }

    @Test
    void getNonExistingBooking() {
        when(bookingService.getBookingById("book"))
                .thenThrow(new BookingNotFoundException("fail"));

        assertThatExceptionOfType(BookingNotFoundException.class)
                .isThrownBy(() -> controller.getBooking("book"));

        verify(bookingService).getBookingById("book");
    }

    @Test
    void getAllBookings() {
        when(bookingService.getAllBookings()).thenReturn(List.of(expectedResponse, expectedResponse, expectedResponse));

        ResponseEntity<List<BookingResponseDTO>> response = controller.getAllBookings();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(List.class);
        assertThat(response.getBody()).isEqualTo(List.of(expectedResponse, expectedResponse, expectedResponse));
    }

    @Test
    void deleteBooking() {
        // act
        ResponseEntity<Void> response = controller.deleteBooking("book123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        verify(bookingService).deleteBooking("book123");
    }
}