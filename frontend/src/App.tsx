import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import BrowseEvents from "./pages/BrowseEvents";
import BookEvent from "./pages/BookEvent";
import MyBookings from "./pages/MyBookings";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/events" element={<BrowseEvents />} />
        <Route path="/book/:id" element={<BookEvent />} />
        <Route path="/my-bookings" element={<MyBookings />} />
      </Routes>
    </Router>
  );
}

export default App;
