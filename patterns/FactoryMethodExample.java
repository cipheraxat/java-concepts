// Factory Method Pattern Example
// Product interface
public interface PaymentGateway {
    void processPayment(double amount);
    boolean refundPayment(String transactionId);
}

// Concrete products
public class StripeGateway implements PaymentGateway {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing $" + amount + " through Stripe");
        // Stripe-specific implementation
    }

    @Override
    public boolean refundPayment(String transactionId) {
        System.out.println("Refunding transaction " + transactionId + " via Stripe");
        return true;
    }
}

public class PayPalGateway implements PaymentGateway {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing $" + amount + " through PayPal");
        // PayPal-specific implementation
    }

    @Override
    public boolean refundPayment(String transactionId) {
        System.out.println("Refunding transaction " + transactionId + " via PayPal");
        return true;
    }
}

// Creator abstract class
public abstract class PaymentGatewayFactory {
    // Factory method - subclasses will implement this
    public abstract PaymentGateway createGateway();

    // Common method that uses the factory method
    public void processOrder(double amount) {
        PaymentGateway gateway = createGateway(); // Call factory method
        gateway.processPayment(amount);
        // Additional order processing logic
    }
}

// Concrete creators
public class StripeFactory extends PaymentGatewayFactory {
    @Override
    public PaymentGateway createGateway() {
        // Stripe-specific initialization
        return new StripeGateway();
    }
}

public class PayPalFactory extends PaymentGatewayFactory {
    @Override
    public PaymentGateway createGateway() {
        // PayPal-specific initialization
        return new PayPalGateway();
    }
}

// Usage example
public class EcommerceSystem {
    public static void main(String[] args) {
        // Configuration-driven factory selection
        String paymentProvider = System.getProperty("payment.provider", "stripe");

        PaymentGatewayFactory factory;
        if ("paypal".equalsIgnoreCase(paymentProvider)) {
            factory = new PayPalFactory();
        } else {
            factory = new StripeFactory(); // Default
        }

        factory.processOrder(99.99);
    }
}