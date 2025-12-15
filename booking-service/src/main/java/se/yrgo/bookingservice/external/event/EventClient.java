package se.yrgo.bookingservice.external.event;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import se.yrgo.bookingservice.dto.ReserveTicketsDTO;

@Component
public class EventClient {
    private final RestClient restClient;

    public EventClient() {
        this.restClient = RestClient.builder().baseUrl("http://localhost:8080").build();
    }

    public void reserveTickets(ReserveTicketsDTO reserveTicketsDTO) {
        try {
            restClient.post()
                    .uri("/api/events/reserve")
                    .body(reserveTicketsDTO)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
