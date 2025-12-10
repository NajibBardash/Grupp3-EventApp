package se.yrgo.user_service.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.yrgo.user_service.data.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private UserRepository data;


    // Function: Show All Users
    //...

    // Function: Create User
    //...


//    public void addUser (Booking booking) {
//        this.bookings.add(booking);
//        booking.setUser(this);
//    }
//
//    public void removeUser (Booking booking) {
//        this.bookings.remove(booking);
//        booking.setUser(null);
//    }
}