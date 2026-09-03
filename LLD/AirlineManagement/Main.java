package AirlineManagement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import AirlineManagement.enums.UserType;
import AirlineManagement.factory.UserFactory;
import AirlineManagement.model.Aircraft;
import AirlineManagement.model.Airport;
import AirlineManagement.model.Booking;
import AirlineManagement.model.Flight;
import AirlineManagement.model.Passenger;
import AirlineManagement.payment.UpiPayment;

public class Main {

  public static void main(String[] args) {

    // 1. Create Airline Management System
    AirlineManagementSystem system = new AirlineManagementSystem();

    // 2. Create passenger using Factory
    Passenger passenger =
        (Passenger)
            UserFactory.createUser(UserType.PASSENGER, "P1", "Harshit", "harshit@gmail.com");

    // 3. Create airports
    Airport delhi = new Airport("DEL", "Indira Gandhi International Airport", "Delhi");

    Airport bangalore = new Airport("BLR", "Kempegowda International Airport", "Bangalore");

    // 4. Create aircraft
    Aircraft aircraft = new Aircraft("A1", "Boeing 737", 100);

    // 5. Create flight
    Flight flight =
        new Flight(
            "AI101",
            delhi,
            bangalore,
            LocalDateTime.of(2026, 9, 10, 10, 0),
            LocalDateTime.of(2026, 9, 10, 13, 0),
            aircraft);

    // Add flight
    system.getFlightManager().addFlight(flight);

    // ------------------------------------------------
    // SEARCH FLIGHTS
    // ------------------------------------------------

    System.out.println("Searching flights...");

    List<Flight> flights = system.searchFlights(delhi, bangalore, LocalDate.of(2026, 9, 10));

    for (Flight f : flights) {
      System.out.println("Found flight: " + f.getFlightNumber());
    }

    // ------------------------------------------------
    // BOOK FLIGHT
    // ------------------------------------------------

    Booking booking = system.bookFlight(passenger, flight);

    System.out.println("Booking created: " + booking.getBookingId());

    // ------------------------------------------------
    // SELECT SEAT
    // ------------------------------------------------

    boolean seatSelected = system.selectSeat(booking, "12");

    System.out.println("Seat selection: " + seatSelected);

    // ------------------------------------------------
    // PAYMENT
    // ------------------------------------------------

    booking.setAmount(5000);

    boolean paymentSuccessful = system.makePayment(booking, new UpiPayment());

    System.out.println("Payment successful: " + paymentSuccessful);

    System.out.println("Booking status: " + booking.getStatus());

    // ------------------------------------------------
    // CANCEL BOOKING
    // ------------------------------------------------

    system.cancelBooking(booking);

    System.out.println("Booking status after cancellation: " + booking.getStatus());
  }
}
