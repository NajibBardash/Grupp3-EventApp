import React, { useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Typography,
  Alert,
  Box,
  Chip,
} from "@mui/material";
import { type EventDTO, createBooking } from "../api/UserFetchingService";
import { useAuth } from "../context/AuthContext";

interface BookEventModalProps {
  event: EventDTO | null;
  open: boolean;
  onClose: () => void;
  onBookingSuccess: () => void;
}

function BookEventModal({
  event,
  open,
  onClose,
  onBookingSuccess,
}: BookEventModalProps) {
  const [numberOfTickets, setNumberOfTickets] = useState(1);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const { user, getAuthHeader, isAuthenticated } = useAuth();

  const handleBook = async () => {
    if (!event || !user) return;

    if (numberOfTickets < 1 || numberOfTickets > event.availableTickets) {
      setError(
        `Please enter a valid number of tickets (1-${event.availableTickets})`
      );
      return;
    }

    setLoading(true);
    setError("");

    try {
      await createBooking(
        event.eventId,
        user.customerId,
        numberOfTickets,
        getAuthHeader()
      );
      onBookingSuccess();
      onClose();
      setNumberOfTickets(1);
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : "Failed to create booking. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    setError("");
    setNumberOfTickets(1);
    onClose();
  };

  const totalPrice = numberOfTickets * 100;

  if (!event) return null;

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
      <DialogTitle>Book Event</DialogTitle>
      <DialogContent>
        <Box sx={{ mb: 2 }}>
          <Typography variant="h6">{event.name}</Typography>
          <Typography variant="body2" color="text.secondary" gutterBottom>
            {event.artist} • {event.location}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            {new Date(event.eventDateAndTime).toLocaleString()}
          </Typography>
          <Box sx={{ mt: 2 }}>
            <Chip
              label={`${event.availableTickets} tickets available`}
              color={event.availableTickets > 0 ? "success" : "error"}
              size="small"
            />
          </Box>
        </Box>

        {!isAuthenticated && (
          <Alert severity="warning" sx={{ mb: 2 }}>
            You must be logged in to book tickets
          </Alert>
        )}

        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}

        {isAuthenticated && (
          <>
            <TextField
              fullWidth
              label="Number of Tickets"
              type="number"
              value={numberOfTickets}
              onChange={(e) => setNumberOfTickets(Number(e.target.value))}
              inputProps={{
                min: 1,
                max: event.availableTickets,
              }}
              disabled={loading || event.availableTickets === 0}
              sx={{ mt: 2 }}
            />

            <Box
              sx={{
                mt: 3,
                p: 2,
                bgcolor: "grey.100",
                borderRadius: 1,
              }}
            >
              <Typography variant="body2" gutterBottom>
                <strong>Customer:</strong> {user?.name} ({user?.customerId})
              </Typography>
              <Typography variant="body2" gutterBottom>
                <strong>Number of Tickets:</strong> {numberOfTickets}
              </Typography>
              <Typography variant="body2">
                <strong>Total Price:</strong> ${totalPrice.toFixed(2)}
              </Typography>
            </Box>
          </>
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose} disabled={loading}>
          Cancel
        </Button>
        {isAuthenticated && (
          <Button
            onClick={handleBook}
            variant="contained"
            disabled={loading || event.availableTickets === 0}
          >
            {loading ? "Booking..." : "Confirm Booking"}
          </Button>
        )}
      </DialogActions>
    </Dialog>
  );
}

export default BookEventModal;
