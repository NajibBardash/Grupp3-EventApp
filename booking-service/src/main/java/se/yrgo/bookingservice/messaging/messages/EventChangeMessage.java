package se.yrgo.bookingservice.messaging.messages;

public record EventChangeMessage(
        String eventId,
        String message){

}

