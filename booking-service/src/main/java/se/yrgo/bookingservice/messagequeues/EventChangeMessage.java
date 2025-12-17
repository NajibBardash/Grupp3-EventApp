package se.yrgo.bookingservice.messagequeues;

public record EventChangeMessage(
        String eventId,
        String message){

}

