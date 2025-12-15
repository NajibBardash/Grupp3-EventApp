import { useEffect, useState } from "react";
import { Typography, Container, Card, CardContent, Grid } from "@mui/material";
import { getUserBookings } from "../api/booking";

interface Booking {
  bookingId: string;
  eventId: string;
  eventName: string;
  date: string;
  location: string;
  tickets: number;
  refundable: boolean;
}

export default function MyBookings() {
  const [bookings, setBookings] = useState<Booking[]>([]);

  useEffect(() => {
    const fetchBookings = async () => {
      const data = await getUserBookings();
      setBookings(data);
    };
    fetchBookings();
  }, []);

  return (
    <Container sx={{ mt: 4 }}>
      <Typography variant="h4" gutterBottom>
        My Bookings
      </Typography>
      <Grid container spacing={3}>
        {bookings.map((booking) => (
          <Grid item xs={12} sm={6} md={4} key={booking.bookingId}>
            <Card>
              <CardContent>
                <Typography variant="h6">{booking.eventName}</Typography>
                <Typography color="textSecondary">
                  {new Date(booking.date).toLocaleString()}
                </Typography>
                <Typography>{booking.location}</Typography>
                <Typography>Tickets: {booking.tickets}</Typography>
                <Typography>
                  Refundable: {booking.refundable ? "Yes" : "No"}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Container>
  );
}
