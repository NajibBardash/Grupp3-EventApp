import React, { useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Alert,
} from "@mui/material";
import { createCategory } from "../api/UserFetchingService";
import { useAuth } from "../context/AuthContext";

interface CreateCategoryModalProps {
  open: boolean;
  onClose: () => void;
  onSuccess: () => void;
}

function CreateCategoryModal({ open, onClose, onSuccess }: CreateCategoryModalProps) {
  const [type, setType] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const { getAuthHeader } = useAuth();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await createCategory({ type }, getAuthHeader());
      onSuccess();
      onClose();
      setType("");
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : "Failed to create category. Make sure you have admin permissions."
      );
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    setError("");
    setType("");
    onClose();
  };

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
      <DialogTitle>Create New Category</DialogTitle>
      <form onSubmit={handleSubmit}>
        <DialogContent>
          {error && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {error}
            </Alert>
          )}

          <TextField
            fullWidth
            required
            label="Category Type"
            name="type"
            value={type}
            onChange={(e) => setType(e.target.value)}
            disabled={loading}
            placeholder="e.g., Music, Theater, Sports"
            helperText="Enter a unique category name"
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} disabled={loading}>
            Cancel
          </Button>
          <Button type="submit" variant="contained" disabled={loading}>
            {loading ? "Creating..." : "Create Category"}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
}

export default CreateCategoryModal;
