package AirlineManagement.manager;

import AirlineManagement.enums.BookingStatus;
import AirlineManagement.model.Booking;
import AirlineManagement.model.Seat;
import AirlineManagement.service.RefundService;

public class CancellationManager {

    private final RefundService refundService;

    public CancellationManager(
            RefundService refundService
    ) {
        this.refundService = refundService;
    }

    public void cancelBooking(Booking booking) {

        if (booking.getStatus()
                == BookingStatus.CANCELLED) {

            return;
        }

        // Release seats
        for (Seat seat : booking.getSeats()) {
            seat.release();
        }

        booking.cancel();

        refundService.refund(booking);
    }
}