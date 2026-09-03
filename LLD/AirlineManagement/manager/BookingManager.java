package AirlineManagement.manager;

import AirlineManagement.model.Booking;
import AirlineManagement.model.Flight;
import AirlineManagement.model.Passenger;
import AirlineManagement.model.Seat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BookingManager {

  private final Map<String, Booking> bookings = new HashMap<>();

  public Booking createBooking(Passenger passenger, Flight flight) {

    String bookingId = UUID.randomUUID().toString();

    Booking booking = new Booking(bookingId, passenger, flight);

    bookings.put(bookingId, booking);
    passenger.addBooking(booking);

    return booking;
  }

  public boolean selectSeat(String bookingId, String seatNumber) {

    Booking booking = bookings.get(bookingId);

    if (booking == null) {
      return false;
    }

    Seat seat = booking.getFlight().getAircraft().getSeat(seatNumber);

    if (seat == null) {
      return false;
    }

    // Atomic operation
    if (!seat.reserve()) {
      return false;
    }

    booking.addSeat(seat);

    return true;
  }

  public Booking getBooking(String bookingId) {
    return bookings.get(bookingId);
  }
}
