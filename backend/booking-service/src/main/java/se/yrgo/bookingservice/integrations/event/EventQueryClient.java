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

    public void handleReservation(TicketReservationDetailsDTO ticketReservationDetailsDTO, EventRequestMethod method) {
        restClient.put()
                .uri("/api/events/" + method.getValue())
                .body(ticketReservationDetailsDTO)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.NOT_FOUND,
                        (req, res) -> {
                            throw new EventNotFoundException(
                                    "Event with id " + ticketReservationDetailsDTO.getEventId() + " not found"
                            );
                        }
                )
                .onStatus(
                        status -> status == HttpStatus.BAD_REQUEST,
                        (req, res) -> {
                            throw new NoTicketsAvailableException(
                                    "Event " + ticketReservationDetailsDTO.getEventId() + " has no tickets available"
                            );
                        }
                )
                .onStatus(
                        status -> status == HttpStatus.INTERNAL_SERVER_ERROR,
                        (req, res) -> {
                            throw new EventServiceUnavailableException(
                                    "Event service unavailable"
                            );
                        }
                )
                .toBodilessEntity();
    }
}
