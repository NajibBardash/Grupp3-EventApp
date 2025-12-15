import { useEffect, useState } from "react";
import { DataGrid, GridColDef } from "@mui/x-data-grid";
import { Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { getEvents } from "../api/event";

interface Event {
  id: string;
  name: string;
  date: string;
  location: string;
  availableTickets: number;
}

export default function BookEvent() {
  const [events, setEvents] = useState<Event[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchEvents = async () => {
      const data = await getEvents();
      setEvents(data);
    };
    fetchEvents();
  }, []);

  const columns: GridColDef[] = [
    { field: "name", headerName: "Event", flex: 1 },
    { field: "date", headerName: "Date", flex: 1 },
    { field: "location", headerName: "Location", flex: 1 },
    { field: "availableTickets", headerName: "Available Tickets", flex: 1 },
    {
      field: "action",
      headerName: "Book",
      flex: 1,
      renderCell: (params) => (
        <Button
          variant="contained"
          color="primary"
          onClick={() => navigate(`/book/${params.row.id}`)}
        >
          Book
        </Button>
      ),
    },
  ];

  return (
    <div style={{ height: 500, width: "100%" }}>
      <h1>Available Events</h1>
      <DataGrid
        rows={events}
        columns={columns}
        pageSize={10}
        rowsPerPageOptions={[10]}
        getRowId={(row) => row.id}
      />
    </div>
  );
}
