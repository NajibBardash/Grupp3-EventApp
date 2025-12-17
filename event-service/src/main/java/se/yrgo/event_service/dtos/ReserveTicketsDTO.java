package se.yrgo.event_service.dtos;

public class ReserveTicketsDTO {
    private int amount;
    private String eventId;

    public ReserveTicketsDTO() {}

    public ReserveTicketsDTO(int amount, String eventId) {
        this.amount = amount;
        this.eventId = eventId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
