package AirlineManagement.model;

import java.util.ArrayList;
import java.util.List;

public class Passenger extends User {

    private final List<Booking> bookings = new ArrayList<>();

    public Passenger(String id, String name, String email) {
        super(id, name, email);
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public List<Booking> getBookings() {
        return bookings;
    }
}