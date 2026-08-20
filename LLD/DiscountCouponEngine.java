import java.io.*;
import java.util.*;
import java.util.concurrent.*;

class Product {
  private String name;
  private String category;
  private double price;

  public Product(String name, String category, double price) {
    this.name = name;
    this.category = category;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public String getCategory() {
    return category;
  }

  public double getPrice() {
    return price;
  }
}

class CartItem {
  private Product product;
  private int quantity;

  public CartItem(Product product, int quantity) {
    this.product = product;
    this.quantity = quantity;
  }

  public double getTotal() {
    return product.getPrice() * quantity;
  }
}

class Cart {
  private ArrayList<CartItem> items = new ArrayList<>();
  private double originalAmount = 0.00;
  private double currentTotal = 0.00;
  private boolean loyalityMember;
  private String paymentBank;
  private String couponCode;

  public Cart() {
    this.loyalityMember = false;
    this.couponCode = "";
  }

  // setter methods
  public void addProduct(Product prod, int qty) {
    CartItem item = new CartItem(prod, qty);
    items.add(item);
    originalAmount += item.getTotal();
    currentTotal += item.getTotal();
  }

  public double getOriginalTotal() {
    return originalAmount;
  }

  public double getCurrentTotal() {
    return currentTotal;
  }

  public void applyDiscount(double d) {
    currentTotal -= d;
    if (currentTotal < 0) {
      currentTotal = 0;
    }
  }

  public void setLoyaltyMember(boolean member) {
    this.loyalityMember = member;
  }

  public boolean isLoyaltyMember() {
    return loyalityMember;
  }

  public void setPaymentBank(String bank) {
    this.paymentBank = bank;
  }

  public String getPaymentBank() {
    return paymentBank;
  }

  public List<CartItem> getItems() {
    return items;
  }
}

// Discount Strategy

interface DiscountStrategy {
  double calculate(double amount);
}

// Alias for backward-compatibility if IDiscountStrategy is referenced
interface IDiscountStrategy extends DiscountStrategy {}

class FlatDiscountStrategy implements DiscountStrategy {
  private double amount;

  public FlatDiscountStrategy(double amount) {
    this.amount = amount;
  }

  @Override
  public double calculate(double baseAmount) {
    return Math.min(amount, baseAmount);
  }
}

class PercentageDiscountStrategy implements DiscountStrategy {
  private double percent;

  public PercentageDiscountStrategy(double pct) {
    this.percent = pct;
  }

  @Override
  public double calculate(double baseAmount) {
    return (percent / 100.0) * baseAmount;
  }
}

class PercentageWithCapStrategy implements DiscountStrategy {
  private double percent;
  private double cap;

  public PercentageWithCapStrategy(double pct, double capVal) {
    this.percent = pct;
    this.cap = capVal;
  }

  @Override
  public double calculate(double baseAmount) {
    double disc = (percent / 100.0) * baseAmount;
    return disc > cap ? cap : disc;
  }
}

enum StrategyType {
  FLAT,
  PERCENT,
  PERCENT_WITH_CAP
}

class DiscountStrategyManager {
  private static DiscountStrategyManager instance;

  private DiscountStrategyManager() {}

  public static synchronized DiscountStrategyManager getInstance() {
    if (instance == null) {
      instance = new DiscountStrategyManager();
    }
    return instance;
  }

  public DiscountStrategy getStrategy(StrategyType type, double param1, double param2) {
    switch (type) {
      case FLAT:
        return new FlatDiscountStrategy(param1);
      case PERCENT:
        return new PercentageDiscountStrategy(param1);
      case PERCENT_WITH_CAP:
        return new PercentageWithCapStrategy(param1, param2);
      default:
        return null;
    }
  }
}

abstract class Coupon {
  private Coupon next;

  public Coupon() {
    this.next = null;
  }

  public void setNext(Coupon c) {
    this.next = c;
  }

  public Coupon getNext() {
    return next;
  }

  public void applyDiscount(Cart cart) {
    if (isApplicable(cart)) {
      double discount = getDiscount(cart);
      cart.applyDiscount(discount);
      System.out.println("Applied " + name() + ": -$" + discount + ", Remaining: $" + cart.getCurrentTotal());
      if (isCombinable() && next != null) {
        next.applyDiscount(cart);
      }
    } else if (next != null) {
      next.applyDiscount(cart);
    }
  }

  public abstract boolean isApplicable(Cart cart);

  public abstract double getDiscount(Cart cart);

  public boolean isCombinable() {
    return true;
  }

  public abstract String name();
}

class LoyaltyDiscountCoupon extends Coupon {
  private DiscountStrategy strategy;

  public LoyaltyDiscountCoupon(DiscountStrategy strategy) {
    this.strategy = strategy;
  }

  @Override
  public boolean isApplicable(Cart cart) {
    return cart.isLoyaltyMember();
  }

  @Override
  public double getDiscount(Cart cart) {
    return strategy.calculate(cart.getCurrentTotal());
  }

  @Override
  public String name() {
    return "Loyalty Coupon";
  }
}

class BankDiscountCoupon extends Coupon {
  private String eligibleBank;
  private DiscountStrategy strategy;

  public BankDiscountCoupon(String eligibleBank, DiscountStrategy strategy) {
    this.eligibleBank = eligibleBank;
    this.strategy = strategy;
  }

  @Override
  public boolean isApplicable(Cart cart) {
    return eligibleBank.equalsIgnoreCase(cart.getPaymentBank());
  }

  @Override
  public double getDiscount(Cart cart) {
    return strategy.calculate(cart.getCurrentTotal());
  }

  @Override
  public String name() {
    return eligibleBank + " Bank Discount Coupon";
  }
}

class MinOrderDiscountCoupon extends Coupon {
  private double minOrderAmount;
  private DiscountStrategy strategy;

  public MinOrderDiscountCoupon(double minOrderAmount, DiscountStrategy strategy) {
    this.minOrderAmount = minOrderAmount;
    this.strategy = strategy;
  }

  @Override
  public boolean isApplicable(Cart cart) {
    return cart.getOriginalTotal() >= minOrderAmount;
  }

  @Override
  public double getDiscount(Cart cart) {
    return strategy.calculate(cart.getCurrentTotal());
  }

  @Override
  public String name() {
    return "Min Order Discount Coupon";
  }
}

public class DiscountCouponEngine {
  public static void main(String[] args) {
    Cart cart = new Cart();
    cart.addProduct(new Product("Laptop", "Electronics", 1200.0), 1);
    cart.addProduct(new Product("Mouse", "Electronics", 50.0), 2);
    cart.setLoyaltyMember(true);
    cart.setPaymentBank("HDFC");

    System.out.println("Original Total: $" + cart.getOriginalTotal());

    DiscountStrategyManager manager = DiscountStrategyManager.getInstance();

    DiscountStrategy loyaltyStrategy = manager.getStrategy(StrategyType.PERCENT, 10, 0);
    DiscountStrategy bankStrategy = manager.getStrategy(StrategyType.PERCENT_WITH_CAP, 15, 100);
    DiscountStrategy minOrderStrategy = manager.getStrategy(StrategyType.FLAT, 50, 0);

    Coupon loyaltyCoupon = new LoyaltyDiscountCoupon(loyaltyStrategy);
    Coupon bankCoupon = new BankDiscountCoupon("HDFC", bankStrategy);
    Coupon minOrderCoupon = new MinOrderDiscountCoupon(500, minOrderStrategy);

    // Chain of responsibility
    loyaltyCoupon.setNext(bankCoupon);
    bankCoupon.setNext(minOrderCoupon);

    loyaltyCoupon.applyDiscount(cart);

    System.out.println("Final Total: $" + cart.getCurrentTotal());
  }
}
