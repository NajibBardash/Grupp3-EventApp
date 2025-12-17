import React from "react";
import { Link } from "react-router-dom";

function HomePage() {
  return (
    <div>
      <h1>Welcome to Event-World!</h1>
      <Link to="/events">Events</Link>
    </div>
  );
}

export default HomePage;
