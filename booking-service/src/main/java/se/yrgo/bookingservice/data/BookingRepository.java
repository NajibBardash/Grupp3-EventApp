package se.yrgo.bookingservice.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.yrgo.bookingservice.domain.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

}
