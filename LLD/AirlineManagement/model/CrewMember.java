package AirlineManagement.model;

import AirlineManagement.enums.CrewRole;

public class CrewMember {

    private final String id;
    private final String name;
    private final CrewRole role;

    public CrewMember(
            String id,
            String name,
            CrewRole role
    ) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public CrewRole getRole() {
        return role;
    }
}