package se.yrgo.bookingservice.integrations.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
/**
 * Simple representation to decide if tickets are to be
 * reserved or existing reservations canceled.
 */
public enum EventRequestMethod {
    RESERVE("reserve"),
    CANCEL("cancel");

    private final String value;
}
