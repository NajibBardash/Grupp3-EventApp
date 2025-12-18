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

    /**
     * This is a neat little REST call to the event-service to
     * reserve or cancel ticket reservations depending on payment success.
     * It throws different exceptions based on the response from even-service and
     * these bubble up and get caught as BookingFailedExceptions.
     * The reason for the enums are to avoid repeated code.
     * @param ticketReservationDetailsDTO the amount of tickets and the event this concerns
     * @param method are we making/removing a reservation?
     */
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
                                    "No available tickets for event with id " + ticketReservationDetailsDTO.getEventId()
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
