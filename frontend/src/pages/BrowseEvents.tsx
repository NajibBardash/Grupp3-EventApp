import { useEffect, useState } from "react";
import {
  Card,
  CardContent,
  CardActions,
  Button,
  Typography,
  Grid,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import { getEvents, type EventDTO } from "../api/event";

export default function BrowseEvents() {
  const [events, setEvents] = useState<EventDTO[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchEvents = async () => {
      const data = await getEvents();
      setEvents(data);
    };
    fetchEvents();
  }, []);

  return (
    <div style={{ padding: 20 }}>
      <Typography variant="h4" gutterBottom>
        Browse Events
      </Typography>
      <Grid container spacing={3}>
        {events.map((event) => (
          <Grid item xs={12} sm={6} md={4} key={event.id}>
            <Card>
              <CardContent>
                <Typography variant="h6">{event.name}</Typography>
                <Typography color="textSecondary">
                  {new Date(event.date).toLocaleString()}
                </Typography>
                <Typography>Location: {event.location}</Typography>
                <Typography>Category: {event.category}</Typography>
                <Typography>Description: {event.description}</Typography>
                <Typography>Tickets Left: {event.availableTickets}</Typography>
              </CardContent>
              <CardActions>
                <Button
                  size="small"
                  color="primary"
                  onClick={() => navigate(`/book/${event.id}`)}
                >
                  Book Now
                </Button>
              </CardActions>
            </Card>
          </Grid>
        ))}
      </Grid>
    </div>
  );
}
