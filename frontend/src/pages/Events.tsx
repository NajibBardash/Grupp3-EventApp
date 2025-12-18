import React, { useEffect, useState } from "react";
import { getAllEvents, type EventDTO } from "../api/UserFetchingService";
import {
  Container,
  Grid,
  Card,
  CardContent,
  Typography,
  Alert,
  CircularProgress,
  Box,
  Chip,
  Button,
  Snackbar,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import BookEventModal from "../components/BookEventModal";
import { useAuth } from "../context/AuthContext";

function Events() {
  const [events, setEvents] = useState<EventDTO[]>([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  const [selectedEvent, setSelectedEvent] = useState<EventDTO | null>(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    loadEvents();
  }, []);

  const loadEvents = () => {
    setLoading(true);
    getAllEvents()
      .then((data) => {
        setEvents(data);
        setLoading(false);
      })
      .catch(() => {
        setError("Kunde inte hämta events");
        setLoading(false);
      });
  };

  const handleEventClick = (event: EventDTO) => {
    setSelectedEvent(event);
    setModalOpen(true);
  };

  const handleBookingSuccess = () => {
    setSuccessMessage("Booking successful! Check 'My Bookings' to see details.");
    loadEvents();
  };

  if (loading) {
    return (
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          minHeight: "50vh",
        }}
      >
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ mt: 4 }}>
      <Typography variant="h3" component="h1" gutterBottom>
        All Events
      </Typography>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      {events.length === 0 && !error && (
        <Alert severity="info">No events available at the moment.</Alert>
      )}

      <Grid container spacing={3}>
        {events.map((event) => (
          <Grid item xs={12} sm={6} md={4} key={event.id}>
            <Card
              sx={{
                height: "100%",
                display: "flex",
                flexDirection: "column",
                cursor: "pointer",
                "&:hover": {
                  boxShadow: 6,
                },
              }}
            >
              <CardContent sx={{ flexGrow: 1 }}>
                <Typography gutterBottom variant="h5" component="h2">
                  {event.name}
                </Typography>
                <Chip
                  label={event.category}
                  color="primary"
                  size="small"
                  sx={{ mb: 1 }}
                />
                <Typography variant="body2" color="text.secondary" paragraph>
                  {event.description}
                </Typography>
                <Typography variant="body2">
                  <strong>Artist:</strong> {event.artist}
                </Typography>
                <Typography variant="body2">
                  <strong>Location:</strong> {event.location}
                </Typography>
                <Typography variant="body2">
                  <strong>Date:</strong>{" "}
                  {new Date(event.eventDateAndTime).toLocaleString()}
                </Typography>
                <Box sx={{ mt: 2, mb: 2 }}>
                  <Chip
                    label={`${event.availableTickets} / ${event.capacity} tickets available`}
                    color={event.availableTickets > 0 ? "success" : "error"}
                    size="small"
                  />
                </Box>
                <Button
                  variant="contained"
                  fullWidth
                  onClick={(e) => {
                    e.stopPropagation();
                    handleEventClick(event);
                  }}
                  disabled={event.availableTickets === 0}
                >
                  {event.availableTickets > 0
                    ? "Book Tickets"
                    : "Sold Out"}
                </Button>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      <BookEventModal
        event={selectedEvent}
        open={modalOpen}
        onClose={() => setModalOpen(false)}
        onBookingSuccess={handleBookingSuccess}
      />

      <Snackbar
        open={!!successMessage}
        autoHideDuration={6000}
        onClose={() => setSuccessMessage("")}
        message={successMessage}
      />
    </Container>
  );
}

export default Events;
