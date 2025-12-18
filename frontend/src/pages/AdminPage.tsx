import React, { useEffect, useState } from "react";
import {
  Container,
  Typography,
  Box,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  IconButton,
  Alert,
  CircularProgress,
  Tabs,
  Tab,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from "@mui/material";
import { Edit, Delete, Add } from "@mui/icons-material";
import {
  getAllEvents,
  deleteEvent,
  getAllCategories,
  deleteCategory,
  type EventDTO,
  type CategoryDTO,
} from "../api/UserFetchingService";
import { useAuth } from "../context/AuthContext";
import CreateEventModal from "../components/CreateEventModal";
import EditEventModal from "../components/EditEventModal";
import CreateCategoryModal from "../components/CreateCategoryModal";
import EditCategoryModal from "../components/EditCategoryModal";

function AdminPage() {
  const [events, setEvents] = useState<EventDTO[]>([]);
  const [categories, setCategories] = useState<CategoryDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [tabValue, setTabValue] = useState(0);
  const [createModalOpen, setCreateModalOpen] = useState(false);
  const [editModalOpen, setEditModalOpen] = useState(false);
  const [selectedEvent, setSelectedEvent] = useState<EventDTO | null>(null);
  const [createCategoryModalOpen, setCreateCategoryModalOpen] = useState(false);
  const [editCategoryModalOpen, setEditCategoryModalOpen] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState<CategoryDTO | null>(null);
  const [deleteDialog, setDeleteDialog] = useState<{
    open: boolean;
    event: EventDTO | null;
  }>({ open: false, event: null });
  const [deleteCategoryDialog, setDeleteCategoryDialog] = useState<{
    open: boolean;
    category: CategoryDTO | null;
  }>({ open: false, category: null });
  const [deleting, setDeleting] = useState(false);
  const { getAuthHeader } = useAuth();

  useEffect(() => {
    loadEvents();
    loadCategories();
  }, []);

  const loadEvents = async () => {
    setLoading(true);
    setError("");
    try {
      const data = await getAllEvents();
      setEvents(data);
    } catch (err) {
      setError("Failed to load events");
    } finally {
      setLoading(false);
    }
  };

  const loadCategories = async () => {
    setError("");
    try {
      const data = await getAllCategories();
      setCategories(data);
    } catch (err) {
      setError("Failed to load categories");
    }
  };

  const handleDeleteClick = (event: EventDTO) => {
    setDeleteDialog({ open: true, event });
  };

  const handleDeleteConfirm = async () => {
    if (!deleteDialog.event) return;

    setDeleting(true);
    setError("");

    try {
      await deleteEvent(deleteDialog.event.id, getAuthHeader());
      setSuccess("Event deleted successfully!");
      setDeleteDialog({ open: false, event: null });
      await loadEvents();
    } catch (err) {
      setError("Failed to delete event. Make sure you have admin permissions.");
    } finally {
      setDeleting(false);
    }
  };

  const handleEditClick = (event: EventDTO) => {
    setSelectedEvent(event);
    setEditModalOpen(true);
  };

  const handleEventCreated = () => {
    setSuccess("Event created successfully!");
    loadEvents();
  };

  const handleEventUpdated = () => {
    setSuccess("Event updated successfully!");
    loadEvents();
  };

  const handleDeleteCategoryClick = (category: CategoryDTO) => {
    setDeleteCategoryDialog({ open: true, category });
  };

  const handleDeleteCategoryConfirm = async () => {
    if (!deleteCategoryDialog.category) return;

    setDeleting(true);
    setError("");

    try {
      await deleteCategory(deleteCategoryDialog.category.id, getAuthHeader());
      setSuccess("Category deleted successfully!");
      setDeleteCategoryDialog({ open: false, category: null });
      await loadCategories();
    } catch (err) {
      setError("Failed to delete category. Make sure you have admin permissions.");
    } finally {
      setDeleting(false);
    }
  };

  const handleEditCategoryClick = (category: CategoryDTO) => {
    setSelectedCategory(category);
    setEditCategoryModalOpen(true);
  };

  const handleCategoryCreated = () => {
    setSuccess("Category created successfully!");
    loadCategories();
  };

  const handleCategoryUpdated = () => {
    setSuccess("Category updated successfully!");
    loadCategories();
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
    <Container maxWidth="xl" sx={{ mt: 4 }}>
      <Box sx={{ display: "flex", justifyContent: "space-between", mb: 3 }}>
        <Typography variant="h3" component="h1">
          Admin Dashboard
        </Typography>
      </Box>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError("")}>
          {error}
        </Alert>
      )}

      {success && (
        <Alert
          severity="success"
          sx={{ mb: 2 }}
          onClose={() => setSuccess("")}
        >
          {success}
        </Alert>
      )}

      <Box sx={{ borderBottom: 1, borderColor: "divider", mb: 3 }}>
        <Tabs value={tabValue} onChange={(_, newValue) => setTabValue(newValue)}>
          <Tab label="Events" />
          <Tab label="Categories" />
        </Tabs>
      </Box>

      {tabValue === 0 && (
        <Box>
          <Box sx={{ display: "flex", justifyContent: "flex-end", mb: 2 }}>
            <Button
              variant="contained"
              startIcon={<Add />}
              onClick={() => setCreateModalOpen(true)}
            >
              Create Event
            </Button>
          </Box>

          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Event ID</TableCell>
                  <TableCell>Name</TableCell>
                  <TableCell>Artist</TableCell>
                  <TableCell>Category</TableCell>
                  <TableCell>Location</TableCell>
                  <TableCell>Date & Time</TableCell>
                  <TableCell>Capacity</TableCell>
                  <TableCell>Available</TableCell>
                  <TableCell>Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {events.map((event) => (
                  <TableRow key={event.id}>
                    <TableCell>{event.eventId}</TableCell>
                    <TableCell>{event.name}</TableCell>
                    <TableCell>{event.artist}</TableCell>
                    <TableCell>{event.category}</TableCell>
                    <TableCell>{event.location}</TableCell>
                    <TableCell>
                      {new Date(event.eventDateAndTime).toLocaleString()}
                    </TableCell>
                    <TableCell>{event.capacity}</TableCell>
                    <TableCell>{event.availableTickets}</TableCell>
                    <TableCell>
                      <IconButton
                        size="small"
                        onClick={() => handleEditClick(event)}
                        color="primary"
                      >
                        <Edit />
                      </IconButton>
                      <IconButton
                        size="small"
                        onClick={() => handleDeleteClick(event)}
                        color="error"
                      >
                        <Delete />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </Box>
      )}

      {tabValue === 1 && (
        <Box>
          <Box sx={{ display: "flex", justifyContent: "flex-end", mb: 2 }}>
            <Button
              variant="contained"
              startIcon={<Add />}
              onClick={() => setCreateCategoryModalOpen(true)}
            >
              Create Category
            </Button>
          </Box>

          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Category ID</TableCell>
                  <TableCell>Type</TableCell>
                  <TableCell>Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {categories.map((category) => (
                  <TableRow key={category.id}>
                    <TableCell>{category.categoryId}</TableCell>
                    <TableCell>{category.type}</TableCell>
                    <TableCell>
                      <IconButton
                        size="small"
                        onClick={() => handleEditCategoryClick(category)}
                        color="primary"
                      >
                        <Edit />
                      </IconButton>
                      <IconButton
                        size="small"
                        onClick={() => handleDeleteCategoryClick(category)}
                        color="error"
                      >
                        <Delete />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </Box>
      )}

      <CreateEventModal
        open={createModalOpen}
        onClose={() => setCreateModalOpen(false)}
        onSuccess={handleEventCreated}
      />

      <EditEventModal
        open={editModalOpen}
        event={selectedEvent}
        onClose={() => {
          setEditModalOpen(false);
          setSelectedEvent(null);
        }}
        onSuccess={handleEventUpdated}
      />

      <Dialog
        open={deleteDialog.open}
        onClose={() => setDeleteDialog({ open: false, event: null })}
      >
        <DialogTitle>Delete Event</DialogTitle>
        <DialogContent>
          <Typography>
            Are you sure you want to delete this event? This action cannot be
            undone.
          </Typography>
          {deleteDialog.event && (
            <Box sx={{ mt: 2, p: 2, bgcolor: "grey.100", borderRadius: 1 }}>
              <Typography variant="body2">
                <strong>Name:</strong> {deleteDialog.event.name}
              </Typography>
              <Typography variant="body2">
                <strong>Event ID:</strong> {deleteDialog.event.eventId}
              </Typography>
              <Typography variant="body2">
                <strong>Artist:</strong> {deleteDialog.event.artist}
              </Typography>
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() => setDeleteDialog({ open: false, event: null })}
            disabled={deleting}
          >
            Cancel
          </Button>
          <Button
            onClick={handleDeleteConfirm}
            color="error"
            variant="contained"
            disabled={deleting}
          >
            {deleting ? "Deleting..." : "Delete Event"}
          </Button>
        </DialogActions>
      </Dialog>

      <CreateCategoryModal
        open={createCategoryModalOpen}
        onClose={() => setCreateCategoryModalOpen(false)}
        onSuccess={handleCategoryCreated}
      />

      <EditCategoryModal
        open={editCategoryModalOpen}
        category={selectedCategory}
        onClose={() => {
          setEditCategoryModalOpen(false);
          setSelectedCategory(null);
        }}
        onSuccess={handleCategoryUpdated}
      />

      <Dialog
        open={deleteCategoryDialog.open}
        onClose={() => setDeleteCategoryDialog({ open: false, category: null })}
      >
        <DialogTitle>Delete Category</DialogTitle>
        <DialogContent>
          <Typography>
            Are you sure you want to delete this category? This action cannot be
            undone.
          </Typography>
          {deleteCategoryDialog.category && (
            <Box sx={{ mt: 2, p: 2, bgcolor: "grey.100", borderRadius: 1 }}>
              <Typography variant="body2">
                <strong>Type:</strong> {deleteCategoryDialog.category.type}
              </Typography>
              <Typography variant="body2">
                <strong>Category ID:</strong> {deleteCategoryDialog.category.categoryId}
              </Typography>
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() => setDeleteCategoryDialog({ open: false, category: null })}
            disabled={deleting}
          >
            Cancel
          </Button>
          <Button
            onClick={handleDeleteCategoryConfirm}
            color="error"
            variant="contained"
            disabled={deleting}
          >
            {deleting ? "Deleting..." : "Delete Category"}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}

export default AdminPage;
