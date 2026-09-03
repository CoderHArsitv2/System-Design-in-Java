package AirlineManagement.factory;

import AirlineManagement.enums.UserType;
import AirlineManagement.model.*;

public class UserFactory {
  public static User createUser(UserType type, String id, String name, String email) {
    return switch (type) {
      case PASSENGER -> new Passenger(id, name, email);
      case STAFF -> new Staff(id, name, email, "EMP-" + id);
      case ADMINISTRATOR -> new Administrator(id, name, email);
      default -> throw new IllegalArgumentException("Invalid user type: " + type);
    };
  }
}
