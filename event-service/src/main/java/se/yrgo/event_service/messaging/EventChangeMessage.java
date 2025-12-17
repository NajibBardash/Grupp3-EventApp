package se.yrgo.event_service.messaging;

import java.io.Serializable;

public record EventChangeMessage(String eventId, String action) implements Serializable {}
