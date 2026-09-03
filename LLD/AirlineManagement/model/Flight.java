package AirlineManagement.model;

import java.time.LocalDateTime;

import AirlineManagement.enums.FlightStatus;

public class Flight {

    private final String flightNumber;
    private final Airport source;
    private final Airport destination;

    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;

    private final Aircraft aircraft;

    private FlightStatus status;

    public Flight(
            String flightNumber,
            Airport source,
            Airport destination,
            LocalDateTime departureTime,
            LocalDateTime arrivalTime,
            Aircraft aircraft
    ) {
        this.flightNumber = flightNumber;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.aircraft = aircraft;
        this.status = FlightStatus.SCHEDULED;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Airport getSource() {
        return source;
    }

    public Airport getDestination() {
        return destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void cancel() {
        status = FlightStatus.CANCELLED;
    }
}