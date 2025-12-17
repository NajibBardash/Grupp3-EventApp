import React, { useState, useEffect } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Alert,
  MenuItem,
  Grid,
} from "@mui/material";
import { updateEvent, type EventDTO } from "../api/UserFetchingService";
import { useAuth } from "../context/AuthContext";

interface EditEventModalProps {
  open: boolean;
  event: EventDTO | null;
  onClose: () => void;
  onSuccess: () => void;
}

const categories = [
  { id: 1, type: "Music" },
  { id: 2, type: "Theater" },
  { id: 3, type: "Sports" },
];

function EditEventModal({
  open,
  event,
  onClose,
  onSuccess,
}: EditEventModalProps) {
  const [formData, setFormData] = useState({
    name: "",
    description: "",
    location: "",
    categoryId: 1,
    artist: "",
    capacity: 100,
    availableTickets: 100,
    eventDateAndTime: "",
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const { getAuthHeader } = useAuth();

  useEffect(() => {
    if (event) {
      // Convert category name to categoryId
      const categoryMap: { [key: string]: number } = {
        Music: 1,
        Theater: 2,
        Sports: 3,
      };

      // Format datetime for input
      const dateTime = new Date(event.eventDateAndTime)
        .toISOString()
        .slice(0, 16);

      setFormData({
        name: event.name,
        description: event.description,
        location: event.location,
        categoryId: categoryMap[event.category] || 1,
        artist: event.artist,
        capacity: event.capacity,
        availableTickets: event.availableTickets,
        eventDateAndTime: dateTime,
      });
    }
  }, [event]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]:
        name === "capacity" ||
        name === "availableTickets" ||
        name === "categoryId"
          ? Number(value)
          : value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!event) return;

    setError("");
    setLoading(true);

    try {
      await updateEvent(event.id, formData, getAuthHeader());
      onSuccess();
      onClose();
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : "Failed to update event. Make sure you have admin permissions."
      );
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    setError("");
    onClose();
  };

  if (!event) return null;

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="md" fullWidth>
      <DialogTitle>Edit Event</DialogTitle>
      <form onSubmit={handleSubmit}>
        <DialogContent>
          {error && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {error}
            </Alert>
          )}

          <Grid container spacing={2}>
            <Grid item xs={12}>
              <TextField
                fullWidth
                required
                label="Event Name"
                name="name"
                value={formData.name}
                onChange={handleChange}
                disabled={loading}
              />
            </Grid>

            <Grid item xs={12}>
              <TextField
                fullWidth
                required
                multiline
                rows={3}
                label="Description"
                name="description"
                value={formData.description}
                onChange={handleChange}
                disabled={loading}
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                required
                label="Artist/Performer"
                name="artist"
                value={formData.artist}
                onChange={handleChange}
                disabled={loading}
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                required
                label="Location"
                name="location"
                value={formData.location}
                onChange={handleChange}
                disabled={loading}
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                required
                select
                label="Category"
                name="categoryId"
                value={formData.categoryId}
                onChange={handleChange}
                disabled={loading}
              >
                {categories.map((category) => (
                  <MenuItem key={category.id} value={category.id}>
                    {category.type}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                required
                type="datetime-local"
                label="Event Date & Time"
                name="eventDateAndTime"
                value={formData.eventDateAndTime}
                onChange={handleChange}
                disabled={loading}
                InputLabelProps={{
                  shrink: true,
                }}
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                required
                type="number"
                label="Capacity"
                name="capacity"
                value={formData.capacity}
                onChange={handleChange}
                disabled={loading}
                inputProps={{ min: 1 }}
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                required
                type="number"
                label="Available Tickets"
                name="availableTickets"
                value={formData.availableTickets}
                onChange={handleChange}
                disabled={loading}
                inputProps={{ min: 0, max: formData.capacity }}
                helperText="Should not exceed capacity"
              />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} disabled={loading}>
            Cancel
          </Button>
          <Button type="submit" variant="contained" disabled={loading}>
            {loading ? "Updating..." : "Update Event"}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
}

export default EditEventModal;
