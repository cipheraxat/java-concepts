// Strategy Pattern Example
// Strategy interface - defines the algorithm contract
public interface PaymentStrategy {
    PaymentResult processPayment(PaymentRequest request);
    boolean supportsRefund();
    RefundResult processRefund(RefundRequest request);
}

// Context class that uses the strategy
public class PaymentProcessor {
    private PaymentStrategy strategy; // Current strategy - can be changed at runtime

    // Constructor injection or setter injection
    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    // Context method that delegates to the strategy
    public PaymentResult process(PaymentRequest request) {
        if (strategy == null) {
            throw new IllegalStateException("Payment strategy not set");
        }
        return strategy.processPayment(request);
    }

    // Additional context methods
    public RefundResult refund(RefundRequest request) {
        if (strategy == null) {
            throw new IllegalStateException("Payment strategy not set");
        }
        if (!strategy.supportsRefund()) {
            throw new UnsupportedOperationException("Refund not supported by current strategy");
        }
        return strategy.processRefund(request);
    }
}

// Concrete strategies

// Credit card payment strategy
public class CreditCardStrategy implements PaymentStrategy {
    private final String merchantId;
    private final String apiKey;

    public CreditCardStrategy(String merchantId, String apiKey) {
        this.merchantId = merchantId;
        this.apiKey = apiKey;
    }

    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        System.out.println("Processing credit card payment for $" + request.getAmount());
        // Simulate credit card processing logic
        // In real implementation, integrate with payment gateway
        return new PaymentResult(true, "CC-" + System.currentTimeMillis(), "Credit card payment successful");
    }

    @Override
    public boolean supportsRefund() {
        return true; // Credit cards support refunds
    }

    @Override
    public RefundResult processRefund(RefundRequest request) {
        System.out.println("Processing credit card refund for transaction " + request.getTransactionId());
        // Refund logic specific to credit cards
        return new RefundResult(true, "Refund processed successfully");
    }
}

// PayPal payment strategy
public class PayPalStrategy implements PaymentStrategy {
    private final String clientId;
    private final String clientSecret;

    public PayPalStrategy(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        System.out.println("Processing PayPal payment for $" + request.getAmount());
        // PayPal-specific processing logic
        return new PaymentResult(true, "PP-" + System.currentTimeMillis(), "PayPal payment successful");
    }

    @Override
    public boolean supportsRefund() {
        return true; // PayPal supports refunds
    }

    @Override
    public RefundResult processRefund(RefundRequest request) {
        System.out.println("Processing PayPal refund for transaction " + request.getTransactionId());
        // PayPal refund logic
        return new RefundResult(true, "PayPal refund processed");
    }
}

// Cryptocurrency payment strategy
public class CryptoStrategy implements PaymentStrategy {
    private final String walletAddress;
    private final String network;

    public CryptoStrategy(String walletAddress, String network) {
        this.walletAddress = walletAddress;
        this.network = network;
    }

    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        System.out.println("Processing crypto payment for $" + request.getAmount() + " on " + network);
        // Crypto-specific processing - might involve wallet interactions
        return new PaymentResult(true, "CRYPTO-" + System.currentTimeMillis(), "Crypto payment initiated");
    }

    @Override
    public boolean supportsRefund() {
        return false; // Crypto transactions are typically irreversible
    }

    @Override
    public RefundResult processRefund(RefundRequest request) {
        throw new UnsupportedOperationException("Crypto payments do not support refunds");
    }
}

// Supporting classes
public class PaymentRequest {
    private final double amount;
    private final String currency;
    private final Map<String, String> paymentDetails;

    public PaymentRequest(double amount, String currency, Map<String, String> paymentDetails) {
        this.amount = amount;
        this.currency = currency;
        this.paymentDetails = new HashMap<>(paymentDetails);
    }

    public double getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public Map<String, String> getPaymentDetails() { return new HashMap<>(paymentDetails); }
}

public class PaymentResult {
    private final boolean success;
    private final String transactionId;
    private final String message;

    public PaymentResult(boolean success, String transactionId, String message) {
        this.success = success;
        this.transactionId = transactionId;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getTransactionId() { return transactionId; }
    public String getMessage() { return message; }
}

public class RefundRequest {
    private final String transactionId;
    private final double amount;

    public RefundRequest(String transactionId, double amount) {
        this.transactionId = transactionId;
        this.amount = amount;
    }

    public String getTransactionId() { return transactionId; }
    public double getAmount() { return amount; }
}

public class RefundResult {
    private final boolean success;
    private final String message;

    public RefundResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}

// Strategy factory for runtime selection
public class PaymentStrategyFactory {
    private static final Map<String, PaymentStrategy> strategies = new HashMap<>();

    static {
        // Initialize strategies with configuration
        strategies.put("CREDIT_CARD", new CreditCardStrategy("merchant123", "apiKey456"));
        strategies.put("PAYPAL", new PayPalStrategy("client123", "secret456"));
        strategies.put("CRYPTO", new CryptoStrategy("wallet123", "ethereum"));
    }

    public static PaymentStrategy getStrategy(String type) {
        PaymentStrategy strategy = strategies.get(type.toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown payment type: " + type);
        }
        return strategy;
    }
}

// Usage example
public class StrategyDemo {
    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentProcessor();

        // Create payment request
        Map<String, String> details = new HashMap<>();
        details.put("cardNumber", "4111111111111111");
        details.put("expiry", "12/25");

        PaymentRequest request = new PaymentRequest(99.99, "USD", details);

        // Use different strategies at runtime
        System.out.println("=== Credit Card Payment ===");
        processor.setPaymentStrategy(PaymentStrategyFactory.getStrategy("CREDIT_CARD"));
        PaymentResult result1 = processor.process(request);
        System.out.println("Result: " + result1.getMessage());

        System.out.println("\n=== PayPal Payment ===");
        processor.setPaymentStrategy(PaymentStrategyFactory.getStrategy("PAYPAL"));
        PaymentResult result2 = processor.process(request);
        System.out.println("Result: " + result2.getMessage());

        System.out.println("\n=== Crypto Payment ===");
        processor.setPaymentStrategy(PaymentStrategyFactory.getStrategy("CRYPTO"));
        PaymentResult result3 = processor.process(request);
        System.out.println("Result: " + result3.getMessage());

        // Try refund on crypto (should fail)
        try {
            RefundRequest refundRequest = new RefundRequest(result3.getTransactionId(), 99.99);
            processor.refund(refundRequest);
        } catch (UnsupportedOperationException e) {
            System.out.println("Refund failed: " + e.getMessage());
        }
    }
}