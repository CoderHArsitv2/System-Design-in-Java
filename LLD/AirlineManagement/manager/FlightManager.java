package AirlineManagement.manager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import AirlineManagement.enums.FlightStatus;
import AirlineManagement.model.Airport;
import AirlineManagement.model.Flight;

public class FlightManager {
    private final List<Flight> flights = new ArrayList<>();

    public void addFlight(Flight flight){
        flights.add(flight);
    }

    public void removeFlight(String flightNumber){
        flights.removeIf(flight -> flight.getFlightNumber().equals(flightNumber));
    }

       public void cancelFlight(String flightNumber) {

        for (Flight flight : flights) {

            if (flight.getFlightNumber()
                    .equals(flightNumber)) {

                flight.cancel();
                return;
            }
        }
    }

    public List<Flight> searchFlights(
            Airport source,
            Airport destination,
            LocalDate date
    ) {

        return flights.stream()
                .filter(f ->
                        f.getSource().getCode()
                                .equals(source.getCode())
                        &&
                        f.getDestination().getCode()
                                .equals(destination.getCode())
                        &&
                        f.getDepartureTime()
                                .toLocalDate()
                                .equals(date)
                        &&
                        f.getStatus()
                                == FlightStatus.SCHEDULED
                )
                .toList();
    }
}