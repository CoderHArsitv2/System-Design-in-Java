package AirlineManagement.manager;

import AirlineManagement.payment.PaymentStrategy;

public class PaymentManager {

    private PaymentStrategy strategy;

    public void setPaymentStrategy(
            PaymentStrategy strategy
    ) {
        this.strategy = strategy;
    }

    public boolean pay(double amount) {

        if (strategy == null) {
            throw new IllegalStateException(
                    "Payment method not selected"
            );
        }

        return strategy.pay(amount);
    }
}
