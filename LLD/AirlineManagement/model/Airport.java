package AirlineManagement.model;

public class Airport {

  private final String code;
  private final String name;
  private final String city;

  public Airport(String code, String name, String city) {
    this.code = code;
    this.name = name;
    this.city = city;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public String getCity() {
    return city;
  }
}
