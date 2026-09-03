package AirlineManagement.service;

import AirlineManagement.model.Booking;

public class RefundService {

    public void refund(Booking booking) {

        System.out.println(
                "Refunding ₹" +
                booking.getAmount() +
                " for booking " +
                booking.getBookingId()
        );
    }
}