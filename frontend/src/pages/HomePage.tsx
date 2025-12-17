import React from "react";
import { Link } from "react-router-dom";
import { Container, Box, Typography, Button, Paper } from "@mui/material";
import { useAuth } from "../context/AuthContext";

function HomePage() {
  const { isAuthenticated, user } = useAuth();

  return (
    <Container maxWidth="md">
      <Box
        sx={{
          marginTop: 8,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <Paper elevation={3} sx={{ padding: 4, width: "100%", textAlign: "center" }}>
          <Typography variant="h2" component="h1" gutterBottom>
            Welcome to Event-World!
          </Typography>

          {isAuthenticated ? (
            <>
              <Typography variant="h5" color="text.secondary" sx={{ mb: 4 }}>
                Hello, {user?.name}! Ready to explore amazing events?
              </Typography>
              <Button
                variant="contained"
                size="large"
                component={Link}
                to="/events"
                sx={{ mr: 2 }}
              >
                Browse Events
              </Button>
              <Button
                variant="outlined"
                size="large"
                component={Link}
                to="/bookings"
              >
                My Bookings
              </Button>
            </>
          ) : (
            <>
              <Typography variant="h5" color="text.secondary" sx={{ mb: 4 }}>
                Discover and book tickets for the best events in town!
              </Typography>
              <Button
                variant="contained"
                size="large"
                component={Link}
                to="/login"
                sx={{ mr: 2 }}
              >
                Login
              </Button>
              <Button
                variant="outlined"
                size="large"
                component={Link}
                to="/register"
                sx={{ mr: 2 }}
              >
                Register
              </Button>
              <Button
                variant="text"
                size="large"
                component={Link}
                to="/events"
              >
                Browse Events
              </Button>
            </>
          )}

          <Box sx={{ mt: 6 }}>
            <Typography variant="h6" gutterBottom>
              Features
            </Typography>
            <Typography variant="body1" color="text.secondary">
              • Browse all available events
              <br />
              • Book tickets securely
              <br />
              • Manage your bookings
              <br />• Get real-time updates on ticket availability
            </Typography>
          </Box>
        </Paper>
      </Box>
    </Container>
  );
}

export default HomePage;
