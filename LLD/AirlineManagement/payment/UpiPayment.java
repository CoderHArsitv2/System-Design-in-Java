package AirlineManagement.payment;

public class UpiPayment implements PaymentStrategy {

    @Override
    public boolean pay(double amount) {

        System.out.println(
                "Paid ₹" + amount + " using UPI"
        );

        return true;
    }
}