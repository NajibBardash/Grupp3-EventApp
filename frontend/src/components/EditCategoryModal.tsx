import React, { useState, useEffect } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Alert,
} from "@mui/material";
import { updateCategory, type CategoryDTO } from "../api/UserFetchingService";
import { useAuth } from "../context/AuthContext";

interface EditCategoryModalProps {
  open: boolean;
  onClose: () => void;
  onSuccess: () => void;
  category: CategoryDTO | null;
}

function EditCategoryModal({
  open,
  onClose,
  onSuccess,
  category,
}: EditCategoryModalProps) {
  const [type, setType] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const { getAuthHeader } = useAuth();

  useEffect(() => {
    if (category) {
      setType(category.type);
    }
  }, [category]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!category) return;

    setError("");
    setLoading(true);

    try {
      await updateCategory(category.id, { type }, getAuthHeader());
      onSuccess();
      onClose();
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : "Failed to update category. Make sure you have admin permissions."
      );
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    setError("");
    onClose();
  };

  if (!category) return null;

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
      <DialogTitle>Edit Category</DialogTitle>
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
            {loading ? "Updating..." : "Update Category"}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
}

export default EditCategoryModal;
