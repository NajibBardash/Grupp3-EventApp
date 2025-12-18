import { BrowserRouter, Route, Routes } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import HomePage from "./pages/HomePage";
import Events from "./pages/Events";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import MyBookings from "./pages/MyBookings";
import AdminPage from "./pages/AdminPage";
import Navbar from "./components/Navbar";
import ProtectedRoute from "./components/ProtectedRoute";
import { Box, ThemeProvider, createTheme } from "@mui/material";

const theme = createTheme({
  palette: {
    primary: {
      main: "#674ea7",
      light: "#8b73c4",
      dark: "#4a3780",
      contrastText: "#FFFFFF",
    },
    secondary: {
      main: "#674ea7",
      light: "#8b73c4",
      dark: "#4a3780",
      contrastText: "#FFFFFF",
    },
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <BrowserRouter>
        <AuthProvider>
          <Navbar />
          <Box sx={{ marginTop: 2 }}>
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/login" element={<LoginPage />} />
              <Route path="/register" element={<RegisterPage />} />
              <Route path="/events" element={<Events />} />
              <Route
                path="/bookings"
                element={
                  <ProtectedRoute>
                    <MyBookings />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/admin"
                element={
                  <ProtectedRoute requireAdmin={true}>
                    <AdminPage />
                  </ProtectedRoute>
                }
              />
            </Routes>
          </Box>
        </AuthProvider>
      </BrowserRouter>
    </ThemeProvider>
  );
}

export default App;
