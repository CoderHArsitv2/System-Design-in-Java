package AirlineManagement.model;

import AirlineManagement.enums.SeatStatus;

public class Seat {
  private final String seatNumber;
  private SeatStatus status;

  public Seat(String seatNumber) {
    this.seatNumber = seatNumber;
    this.status = SeatStatus.AVAILABLE;
  }

  public synchronized boolean reserve() {

    if (status != SeatStatus.AVAILABLE) {
      return false;
    }
    status = SeatStatus.RESERVED;
    return true;
  }

  public synchronized void release() {
    status = SeatStatus.AVAILABLE;
  }

  public String getSeatNumber() {
    return seatNumber;
  }

  public synchronized SeatStatus getStatus() {
    return status;
  }
}
// The important part for your interview is:

// public synchronized boolean reserve()

// Two threads can't reserve the same seat simultaneously.

// You can tell the interviewer:

// "In a real distributed system, synchronized would not be sufficient because 
// multiple application instances may be running. I'd use a database transaction 
// with row-level locking or an atomic reservation mechanism."