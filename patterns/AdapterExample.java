// Adapter Pattern Example
// Target interface that clients expect
public interface PaymentProcessor {
    void processPayment(double amount, String currency);
    boolean verifyPayment(String transactionId);
}

// Adaptee: Existing third-party gateway with incompatible interface
public class LegacyPaymentGateway {
    // Legacy method signatures
    public void makePayment(int amountInCents, String currencyCode) {
        System.out.println("Legacy gateway: Processing " + amountInCents + " cents in " + currencyCode);
    }

    public String checkPaymentStatus(String reference) {
        System.out.println("Legacy gateway: Checking status for " + reference);
        return "SUCCESS"; // Simplified
    }
}

// Adapter class that implements target interface and wraps adaptee
public class PaymentGatewayAdapter implements PaymentProcessor {
    private final LegacyPaymentGateway legacyGateway;

    // Constructor injection of adaptee
    public PaymentGatewayAdapter(LegacyPaymentGateway legacyGateway) {
        this.legacyGateway = legacyGateway;
    }

    @Override
    public void processPayment(double amount, String currency) {
        // Convert double to int (cents) and adapt method call
        int amountInCents = (int) (amount * 100); // Convert dollars to cents
        legacyGateway.makePayment(amountInCents, currency);
    }

    @Override
    public boolean verifyPayment(String transactionId) {
        // Adapt return type: String status to boolean
        String status = legacyGateway.checkPaymentStatus(transactionId);
        return "SUCCESS".equals(status); // Convert string result to boolean
    }
}

// Modern gateway that already implements the interface
public class ModernPaymentGateway implements PaymentProcessor {
    @Override
    public void processPayment(double amount, String currency) {
        System.out.println("Modern gateway: Processing $" + amount + " in " + currency);
    }

    @Override
    public boolean verifyPayment(String transactionId) {
        System.out.println("Modern gateway: Verifying " + transactionId);
        return true;
    }
}

// Client code that works with any PaymentProcessor
public class EcommercePlatform {
    private final PaymentProcessor paymentProcessor;

    public EcommercePlatform(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    public void checkout(double amount, String currency) {
        paymentProcessor.processPayment(amount, currency);
        // Additional checkout logic
    }

    public boolean verifyTransaction(String transactionId) {
        return paymentProcessor.verifyPayment(transactionId);
    }
}

// Usage example
public class AdapterDemo {
    public static void main(String[] args) {
        // Use adapter for legacy gateway
        LegacyPaymentGateway legacy = new LegacyPaymentGateway();
        PaymentProcessor adaptedProcessor = new PaymentGatewayAdapter(legacy);

        EcommercePlatform platform = new EcommercePlatform(adaptedProcessor);
        platform.checkout(99.99, "USD");

        // Can easily switch to modern gateway without changing client code
        PaymentProcessor modernProcessor = new ModernPaymentGateway();
        EcommercePlatform modernPlatform = new EcommercePlatform(modernProcessor);
        modernPlatform.checkout(99.99, "USD");
    }
}