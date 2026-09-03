package AirlineManagement.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import AirlineManagement.model.CrewMember;
import AirlineManagement.model.Flight;

public class CrewManager {

    private final Map<
            String,
            List<CrewMember>
            > flightCrew = new HashMap<>();

    public void assignCrew(
            Flight flight,
            List<CrewMember> crew
    ) {

        flightCrew.put(
                flight.getFlightNumber(),
                crew
        );
    }

    public List<CrewMember> getCrew(
            String flightNumber
    ) {

        return flightCrew.get(flightNumber);
    }
}