package se.yrgo.bookingservice.integrations.event;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import se.yrgo.bookingservice.dto.ReserveTicketsDTO;
import se.yrgo.bookingservice.exceptions.event.EventNotFoundException;
import se.yrgo.bookingservice.exceptions.event.EventServiceUnavailableException;
import se.yrgo.bookingservice.exceptions.event.NoTicketsAvailableException;

@Component
public class EventClient {
    private final RestClient restClient;

    public EventClient() {
        this.restClient = RestClient.builder().baseUrl("http://localhost:8080").build();
    }

    public void reserveTickets(ReserveTicketsDTO reserveTicketsDTO) {
        try {
            restClient.post()
                    .uri("/api/event/reserve")
                    .body(reserveTicketsDTO)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new EventNotFoundException(reserveTicketsDTO.getEventId(), e.getCause());
            }
            else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new NoTicketsAvailableException(reserveTicketsDTO.getEventId(), e.getCause());
            }
            else if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                throw new EventServiceUnavailableException(e.getMessage(), e.getCause());
            }
        }
    }
}
