package AirlineManagement.payment;

public class CardPayment implements PaymentStrategy {

    @Override
    public boolean pay(double amount) {

        System.out.println(
                "Paid ₹" + amount + " using Card"
        );

        return true;
    }
}