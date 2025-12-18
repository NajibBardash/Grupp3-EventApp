import { AppBar, Toolbar, Typography, Button, Box } from "@mui/material";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function Navbar() {
  const { user, isAuthenticated, isAdmin, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <AppBar position="static">
      <Toolbar>
        <Typography
          variant="h6"
          component={Link}
          to="/"
          sx={{
            flexGrow: 1,
            textDecoration: "none",
            color: "inherit",
          }}
        >
          Event-World
        </Typography>

        <Box sx={{ display: "flex", gap: 2 }}>
          <Button color="inherit" component={Link} to="/events">
            Events
          </Button>

          {isAuthenticated ? (
            <>
              <Button color="inherit" component={Link} to="/bookings">
                My Bookings
              </Button>
              {isAdmin && (
                <Button color="inherit" component={Link} to="/admin">
                  Admin
                </Button>
              )}
              <Typography
                variant="body1"
                sx={{
                  display: "flex",
                  alignItems: "center",
                  marginRight: 2,
                }}
              >
                Welcome, {user?.name}
              </Typography>
              <Button color="inherit" onClick={handleLogout}>
                Logout
              </Button>
            </>
          ) : (
            <>
              <Button color="inherit" component={Link} to="/login">
                Login
              </Button>
              <Button color="inherit" component={Link} to="/register">
                Register
              </Button>
            </>
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
}

export default Navbar;
