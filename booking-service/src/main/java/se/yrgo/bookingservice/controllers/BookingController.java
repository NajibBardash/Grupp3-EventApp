package se.yrgo.bookingservice.controllers;

import se.yrgo.bookingservice.services.BookingService;

public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public void registerNewBooking(String eventId, String userId, int noOfTickets, boolean isRefundable  ) {
        bookingService.createBooking(eventId, userId, noOfTickets, isRefundable );
    }
}
