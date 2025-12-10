package se.yrgo.bookingservice.controllers;

import se.yrgo.bookingservice.dto.BookingCreateDTO;
import se.yrgo.bookingservice.services.BookingServiceImpl;

public class BookingController {
    private final BookingServiceImpl bookingService;

    public BookingController(BookingServiceImpl bookingService) {
        this.bookingService = bookingService;
    }

    public void registerNewBooking(BookingCreateDTO bookingCreateDTO) {
        bookingService.createBooking(bookingCreateDTO);
    }
}
