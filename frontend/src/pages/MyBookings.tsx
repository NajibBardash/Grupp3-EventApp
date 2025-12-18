import React, { useEffect, useState } from "react";
import {
  Container,
  Typography,
  Card,
  CardContent,
  Grid,
  Alert,
  CircularProgress,
  Box,
  Button,
  Chip,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from "@mui/material";
import {
  getAllBookings,
  deleteBooking,
  getEventById,
  type BookingDTO,
  type EventDTO,
} from "../api/UserFetchingService";
import { useAuth } from "../context/AuthContext";

interface BookingWithEvent extends BookingDTO {
  event?: EventDTO;
}

function MyBookings() {
  const [bookings, setBookings] = useState<BookingWithEvent[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [cancelDialog, setCancelDialog] = useState<{
    open: boolean;
    booking: BookingDTO | null;
  }>({ open: false, booking: null });
  const [canceling, setCanceling] = useState(false);
  const { getAuthHeader, user } = useAuth();

  useEffect(() => {
    loadBookings();
  }, []);

  const loadBookings = async () => {
    setLoading(true);
    setError("");

    try {
      const bookingsData = await getAllBookings(getAuthHeader());

      // Filter bookings for current user
      const userBookings = bookingsData.filter(
        (booking) => booking.customerId === user?.customerId
      );

      // Fetch event details for each booking
      const bookingsWithEvents = await Promise.all(
        userBookings.map(async (booking) => {
          try {
            const event = await getEventById(booking.eventId);
            return { ...booking, event };
          } catch {
            return booking;
          }
        })
      );

      setBookings(bookingsWithEvents);
    } catch (err) {
      setError("Failed to load bookings. Please try again.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleCancelClick = (booking: BookingDTO) => {
    setCancelDialog({ open: true, booking });
  };

  const handleCancelConfirm = async () => {
    if (!cancelDialog.booking) return;

    setCanceling(true);

    try {
      await deleteBooking(cancelDialog.booking.bookingId, getAuthHeader());
      setCancelDialog({ open: false, booking: null });
      await loadBookings();
    } catch (err) {
      setError("Failed to cancel booking. Please try again.");
    } finally {
      setCanceling(false);
    }
  };

  const calculateTotalPrice = (booking: BookingDTO) => {
    return booking.tickets.reduce((sum, ticket) => sum + ticket.price, 0);
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
        My Bookings
      </Typography>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      {bookings.length === 0 && !error && (
        <Alert severity="info">
          You don't have any bookings yet. Browse events to make a booking!
        </Alert>
      )}

      <Grid container spacing={3}>
        {bookings.map((booking) => (
          <Grid item xs={12} md={6} key={booking.bookingId}>
            <Card>
              <CardContent>
                <Box
                  sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "flex-start",
                    mb: 2,
                  }}
                >
                  <Typography variant="h6">
                    {booking.event?.name || "Event Details Loading..."}
                  </Typography>
                  <Chip
                    label={booking.refundable ? "Refundable" : "Non-refundable"}
                    color={booking.refundable ? "success" : "default"}
                    size="small"
                  />
                </Box>

                {booking.event && (
                  <>
                    <Typography variant="body2" color="text.secondary">
                      <strong>Artist:</strong> {booking.event.artist}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      <strong>Location:</strong> {booking.event.location}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      <strong>Event Date:</strong>{" "}
                      {new Date(booking.event.eventDateAndTime).toLocaleString()}
                    </Typography>
                  </>
                )}

                <Box
                  sx={{
                    mt: 2,
                    p: 2,
                    bgcolor: "grey.100",
                    borderRadius: 1,
                  }}
                >
                  <Typography variant="body2">
                    <strong>Booking ID:</strong> {booking.bookingId}
                  </Typography>
                  <Typography variant="body2">
                    <strong>Booking Date:</strong>{" "}
                    {new Date(booking.bookingDate).toLocaleString()}
                  </Typography>
                  <Typography variant="body2">
                    <strong>Number of Tickets:</strong> {booking.tickets.length}
                  </Typography>
                  <Typography variant="body2">
                    <strong>Total Price:</strong> $
                    {calculateTotalPrice(booking).toFixed(2)}
                  </Typography>
                </Box>

                {booking.refundable && (
                  <Button
                    variant="outlined"
                    color="error"
                    fullWidth
                    sx={{ mt: 2 }}
                    onClick={() => handleCancelClick(booking)}
                  >
                    Cancel Booking
                  </Button>
                )}
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      <Dialog
        open={cancelDialog.open}
        onClose={() => setCancelDialog({ open: false, booking: null })}
      >
        <DialogTitle>Cancel Booking</DialogTitle>
        <DialogContent>
          <Typography>
            Are you sure you want to cancel this booking? This action cannot be
            undone.
          </Typography>
          {cancelDialog.booking && (
            <Box sx={{ mt: 2, p: 2, bgcolor: "grey.100", borderRadius: 1 }}>
              <Typography variant="body2">
                <strong>Booking ID:</strong> {cancelDialog.booking.bookingId}
              </Typography>
              <Typography variant="body2">
                <strong>Tickets:</strong> {cancelDialog.booking.tickets.length}
              </Typography>
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() => setCancelDialog({ open: false, booking: null })}
            disabled={canceling}
          >
            Keep Booking
          </Button>
          <Button
            onClick={handleCancelConfirm}
            color="error"
            variant="contained"
            disabled={canceling}
          >
            {canceling ? "Canceling..." : "Cancel Booking"}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}

export default MyBookings;
