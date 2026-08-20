import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

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

  public String getCategory() {
    return product.getCategory();
  }
}

class Cart {
  private ArrayList<CartItem> items = new ArrayList<>();
  private double originalAmount = 0.00;
  private double currentTotal = 0.00;
  private boolean loyalityMember;
  private String paymentBank;

  public Cart() {
    this.loyalityMember = false;
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

interface IDiscountStrategy {
  double calculate(double amount);
}

class FlatDiscountStrategy implements IDiscountStrategy {
  private double amount;

  public FlatDiscountStrategy(double amount) {
    this.amount = amount;
  }

  @Override
  public double calculate(double baseAmount) {
    return Math.min(amount, baseAmount);
  }
}

class PercentageDiscountStrategy implements IDiscountStrategy {
  private double percent;

  public PercentageDiscountStrategy(double pct) {
    this.percent = pct;
  }

  @Override
  public double calculate(double baseAmount) {
    return (percent / 100.0) * baseAmount;
  }
}

class PercentageWithCapStrategy implements IDiscountStrategy {
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

  public IDiscountStrategy getStrategy(StrategyType type, double param1, double param2) {
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

  public void applyDiscoun(Cart cart) {
    if (isApplicable(cart)) {}
  }

  public abstract boolean isApplicable(Cart cart);

  public abstract double getDiscount(Cart cart);

  public boolean isCombinable() {
    return true;
  }

  public abstract String name();
}

class SeasonalOffer extends Coupon {
  private double percent;
  private String category;
  private IDiscountStrategy strat;

  public SeasonalOffer(double pct, String cat) {
    this.percent = pct;
    this.category = cat;
    this.strat =
        DiscountStrategyManager.getInstance().getStrategy(StrategyType.PERCENT, percent, 0.0);
  }

  @Override
  public boolean isApplicable(Cart cart) {
    for (CartItem item : cart.getItems()) {
      if (item.getCategory().equals(category)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public double getDiscount(Cart cart) {
    double subtotal = 0.0;
    for (CartItem item : cart.getItems()) {
      if (item.getCategory().equals(category)) {
        subtotal += item.getTotal();
      }
    }
    return strat.calculate(subtotal);
  }

  @Override
  public String name() {
    return "Seasonal Offer " + (int) percent + "% off " + category;
  }
}

class LoyaltyDiscount extends Coupon {
  private double percent;
  private IDiscountStrategy strat;

  public LoyaltyDiscount(double pct) {
    this.percent = pct;
    this.strat =
        DiscountStrategyManager.getInstance().getStrategy(StrategyType.PERCENT, percent, 0.0);
  }

  @Override
  public boolean isApplicable(Cart cart) {
    return cart.isLoyaltyMember();
  }

  @Override
  public double getDiscount(Cart cart) {
    return strat.calculate(cart.getCurrentTotal());
  }

  @Override
  public String name() {
    return "Loyalty Discount " + (int) percent + "% off";
  }
}

class BulkPurchaseDiscount extends Coupon {
  private double threshold;
  private double flatOff;
  private IDiscountStrategy strat;

  public BulkPurchaseDiscount(double thr, double off) {
    this.threshold = thr;
    this.flatOff = off;
    this.strat = DiscountStrategyManager.getInstance().getStrategy(StrategyType.FLAT, flatOff, 0.0);
  }

  @Override
  public boolean isApplicable(Cart cart) {
    return cart.getOriginalTotal() >= threshold;
  }

  @Override
  public double getDiscount(Cart cart) {
    return strat.calculate(cart.getCurrentTotal());
  }

  @Override
  public String name() {
    return "Bulk Purchase Rs " + (int) flatOff + " off over " + (int) threshold;
  }
}

class BankingCoupon extends Coupon {
  private String bank;
  private double minSpend;
  private double percent;
  private double offCap;
  private IDiscountStrategy strat;

  public BankingCoupon(String b, double ms, double percent, double offCap) {
    this.bank = b;
    this.minSpend = ms;
    this.percent = percent;
    this.offCap = offCap;
    this.strat =
        DiscountStrategyManager.getInstance()
            .getStrategy(StrategyType.PERCENT_WITH_CAP, percent, offCap);
  }

  @Override
  public boolean isApplicable(Cart cart) {
    return cart.getPaymentBank().equals(bank) && cart.getOriginalTotal() >= minSpend;
  }

  @Override
  public double getDiscount(Cart cart) {
    return strat.calculate(cart.getCurrentTotal());
  }

  @Override
  public String name() {
    return bank + " Bank Rs " + (int) percent + " off upto " + (int) offCap;
  }
}

class CouponManager {
  private static CouponManager instance;
  private Coupon head;
  private final ReentrantLock lock = new ReentrantLock();

  private CouponManager() {
    this.head = null;
  }

  public static synchronized CouponManager getInstance() {
    if (instance == null) {
      instance = new CouponManager();
    }
    return instance;
  }

  public double applyAll(Cart cart) {
    lock.lock();
    try {
      if (head != null) {
        head.applyDiscoun(cart);
      }
      return cart.getCurrentTotal();
    } finally {
      lock.unlock();
    }
  }

  public void registerCoupon(Coupon coupon) {
    lock.lock();
    try {
      if (head == null) {
        head = coupon;
      } else {
        Coupon cur = head;
        while (cur.getNext() != null) {
          cur = cur.getNext();
        }
        cur.setNext(coupon);
      }
    } finally {
      lock.unlock();
    }
  }

  public List<String> getApplicable(Cart cart) {
    lock.lock();
    try {
      List<String> res = new ArrayList<>();
      Coupon cur = head;
      while (cur != null) {
        if (cur.isApplicable(cart)) {
          res.add(cur.name());
        }
        cur = cur.getNext();
      }
      return res;
    } finally {
      lock.unlock();
    }
  }
}

// ----------------------------
public class DiscountCouponEngine {
  public static void main(String[] args) {
    CouponManager mgr = CouponManager.getInstance();
    mgr.registerCoupon(new SeasonalOffer(10, "Clothing"));
    mgr.registerCoupon(new LoyaltyDiscount(5));
    mgr.registerCoupon(new BulkPurchaseDiscount(1000, 100));
    mgr.registerCoupon(new BankingCoupon("ABC", 2000, 15, 500));

    Product p1 = new Product("Winter Jacket", "Clothing", 1000);
    Product p2 = new Product("Smartphone", "Electronics", 20000);
    Product p3 = new Product("Jeans", "Clothing", 1000);
    Product p4 = new Product("Headphones", "Electronics", 2000);

    Cart cart = new Cart();
    cart.addProduct(p1, 1);
    cart.addProduct(p2, 1);
    cart.addProduct(p3, 2);
    cart.addProduct(p4, 1);
    cart.setLoyaltyMember(true);
    cart.setPaymentBank("ABC");

    System.out.println("Original Cart Total: " + cart.getOriginalTotal() + " Rs");

    List<String> applicable = mgr.getApplicable(cart);
    System.out.println("Applicable Coupons:");
    for (String name : applicable) {
      System.out.println(" - " + name);
    }

    double finalTotal = mgr.applyAll(cart);
    System.out.println("Final Cart Total after discounts: " + finalTotal + " Rs");
  }
}
