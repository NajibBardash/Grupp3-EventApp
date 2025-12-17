package se.yrgo.bookingservice.integrations.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventRequestMethod {
    RESERVE("reserve"),
    CANCEL("cancel");

    private final String value;
}
