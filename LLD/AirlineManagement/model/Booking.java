package AirlineManagement.model;

import java.util.ArrayList;
import java.util.List;

import AirlineManagement.enums.BookingStatus;

public class Booking {

    private final String bookingId;
    private final Passenger passenger;
    private final Flight flight;

    private final List<Seat> seats = new ArrayList<>();

    private double amount;
    private BookingStatus status;

    public Booking(
            String bookingId,
            Passenger passenger,
            Flight flight
    ) {
        this.bookingId = bookingId;
        this.passenger = passenger;
        this.flight = flight;
        this.status = BookingStatus.PENDING;
    }

    public void addSeat(Seat seat) {
        seats.add(seat);
    }

    public void confirm() {
        status = BookingStatus.CONFIRMED;
    }

    public void cancel() {
        status = BookingStatus.CANCELLED;
    }

    public String getBookingId() {
        return bookingId;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Flight getFlight() {
        return flight;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public BookingStatus getStatus() {
        return status;
    }
}