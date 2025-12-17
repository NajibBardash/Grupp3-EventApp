package se.yrgo.bookingservice.integrations.event;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import se.yrgo.bookingservice.dto.TicketReservationDetailsDTO;
import se.yrgo.bookingservice.exceptions.event.EventNotFoundException;
import se.yrgo.bookingservice.exceptions.event.EventServiceUnavailableException;
import se.yrgo.bookingservice.exceptions.event.NoTicketsAvailableException;

@Component
public class EventQueryClient {
    private final RestClient restClient;

    public EventQueryClient() {
        this.restClient = RestClient.builder().baseUrl("http://localhost:8081").build();
    }

    public void handleTicketReservation(TicketReservationDetailsDTO ticketReservationDetailsDTO, EventQueryMethod method) {
        try {
            restClient.post()
                    .uri("/api/event/" + method.getValue())
                    .body(ticketReservationDetailsDTO)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new EventNotFoundException(ticketReservationDetailsDTO.getEventId(), e.getCause());
            }
            else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new NoTicketsAvailableException(ticketReservationDetailsDTO.getEventId(), e.getCause());
            }
            else if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                throw new EventServiceUnavailableException(e.getMessage(), e.getCause());
            }
        }
    }

    public void clearReservation(TicketReservationDetailsDTO ticketReservationDetailsDTO) {
        System.out.println("NOT IMPLEMENTED");
    }
}
