package se.yrgo.bookingservice.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.yrgo.bookingservice.domain.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Booking findByBookingId(String bookingId);
    List<Booking> findByCustomerId(String customerId);
    List<Booking> findByEventId(String eventId);
}
