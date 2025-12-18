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
import { createEvent, getAllCategories } from "../api/UserFetchingService";
import { useAuth } from "../context/AuthContext";

interface CreateEventModalProps {
  open: boolean;
  onClose: () => void;
  onSuccess: () => void;
}

interface Category {
  id: number;
  categoryId: string;
  type: string;
}

function CreateEventModal({ open, onClose, onSuccess }: CreateEventModalProps) {
  const [categories, setCategories] = useState<Category[]>([]);
  const [formData, setFormData] = useState({
    name: "",
    description: "",
    location: "",
    categoryId: "",
    artist: "",
    capacity: 100,
    availableTickets: 100,
    eventDateAndTime: "",
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const { getAuthHeader } = useAuth();

  useEffect(() => {
    if (open) {
      loadCategories();
    }
  }, [open]);

  const loadCategories = async () => {
    try {
      const cats = await getAllCategories();
      setCategories(cats);
      if (cats.length > 0) {
        setFormData((prev) => ({ ...prev, categoryId: cats[0].categoryId }));
      }
    } catch (err) {
      console.error("Failed to load categories", err);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === "capacity" || name === "availableTickets"
        ? Number(value)
        : value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await createEvent(formData, getAuthHeader());
      onSuccess();
      onClose();
      setFormData({
        name: "",
        description: "",
        location: "",
        categoryId: categories.length > 0 ? categories[0].categoryId : "",
        artist: "",
        capacity: 100,
        availableTickets: 100,
        eventDateAndTime: "",
      });
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : "Failed to create event. Make sure you have admin permissions."
      );
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    setError("");
    onClose();
  };

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="md" fullWidth>
      <DialogTitle>Create New Event</DialogTitle>
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
                  <MenuItem key={category.categoryId} value={category.categoryId}>
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
            {loading ? "Creating..." : "Create Event"}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
}

export default CreateEventModal;
