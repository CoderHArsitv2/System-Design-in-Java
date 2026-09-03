package AirlineManagement.model;

public class Staff extends User {

    private final String employeeId;

    public Staff(
            String id,
            String name,
            String email,
            String employeeId
    ) {
        super(id, name, email);
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }
}