package AirlineManagement.model;

import java.util.ArrayList;
import java.util.List;

public class Aircraft {

    private final String aircraftId;
    private final String model;
    private final List<Seat> seats;

    public Aircraft(
            String aircraftId,
            String model,
            int numberOfSeats
    ) {
        this.aircraftId = aircraftId;
        this.model = model;
        this.seats = new ArrayList<>();

        for (int i = 1; i <= numberOfSeats; i++) {
            seats.add(new Seat(String.valueOf(i)));
        }
    }

    public Seat getSeat(String seatNumber) {

        return seats.stream()
                .filter(s -> s.getSeatNumber().equals(seatNumber))
                .findFirst()
                .orElse(null);
    }

    public List<Seat> getSeats() {
        return seats;
    }
}