package AirlineManagement;

import java.time.LocalDate;
import java.util.List;

import AirlineManagement.manager.BookingManager;
import AirlineManagement.manager.CancellationManager;
import AirlineManagement.manager.CrewManager;
import AirlineManagement.manager.FlightManager;
import AirlineManagement.manager.PaymentManager;
import AirlineManagement.model.Airport;
import AirlineManagement.model.Booking;
import AirlineManagement.model.Flight;
import AirlineManagement.model.Passenger;
import AirlineManagement.payment.PaymentStrategy;
import AirlineManagement.service.RefundService;

public class AirlineManagementSystem {

    private final FlightManager flightManager;
    private final BookingManager bookingManager;
    private final PaymentManager paymentManager;
    private final CrewManager crewManager;
    private final CancellationManager cancellationManager;

    public AirlineManagementSystem() {

        this.flightManager =
                new FlightManager();

        this.bookingManager =
                new BookingManager();

        this.paymentManager =
                new PaymentManager();

        this.crewManager =
                new CrewManager();

        this.cancellationManager =
                new CancellationManager(
                        new RefundService()
                );
    }

    public List<Flight> searchFlights(
            Airport source,
            Airport destination,
            LocalDate date
    ) {

        return flightManager.searchFlights(
                source,
                destination,
                date
        );
    }

    public Booking bookFlight(
            Passenger passenger,
            Flight flight
    ) {

        return bookingManager.createBooking(
                passenger,
                flight
        );
    }

    public boolean selectSeat(
            Booking booking,
            String seatNumber
    ) {

        return bookingManager.selectSeat(
                booking.getBookingId(),
                seatNumber
        );
    }

    public boolean makePayment(
            Booking booking,
            PaymentStrategy strategy
    ) {

        paymentManager.setPaymentStrategy(strategy);

        boolean success =
                paymentManager.pay(
                        booking.getAmount()
                );

        if (success) {
            booking.confirm();
        }

        return success;
    }

    public void cancelBooking(
            Booking booking
    ) {

        cancellationManager.cancelBooking(
                booking
        );
    }

    public FlightManager getFlightManager() {
        return flightManager;
    }

    public CrewManager getCrewManager() {
        return crewManager;
    }
}