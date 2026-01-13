# Design Patterns in Java: SDE2 Interview Preparation Guide

*Prepared by a Senior Staff Engineer and Technical Interviewer at a top-tier tech company*

This guide covers essential design patterns in Java, focusing on SDE2-level understanding with real-world system design applications, production-grade implementations, and interview insights. Each pattern includes detailed code with concurrency considerations and common pitfalls.

## Creational Patterns

### Singleton

**Concept:** The Singleton pattern ensures a class has only one instance and provides a global point of access to it. It solves the problem of controlling object creation to maintain a single shared state across an application.

**SDE2-Level Real-World Use Case:** Database connection pools in microservices architectures, where multiple services need to share a single pool of connections to avoid resource exhaustion and ensure consistent connection management across the system.

**Java Implementation (The Code):**
```java
public class DatabaseConnectionPool {
    // Volatile ensures visibility of the instance across threads in a multi-threaded environment
    // Without volatile, different threads might see a partially constructed instance due to CPU caching
    private static volatile DatabaseConnectionPool instance;

    // Private constructor prevents external instantiation, enforcing the single instance rule
    private DatabaseConnectionPool() {
        // Initialize connection pool resources here
        System.out.println("Database connection pool initialized");
    }

    // Public static method provides global access point
    // Double-checked locking ensures thread safety while minimizing synchronization overhead
    public static DatabaseConnectionPool getInstance() {
        // First check: Avoid synchronization if instance already exists (performance optimization)
        if (instance == null) {
            // Synchronized block: Only one thread can enter at a time to create the instance
            synchronized (DatabaseConnectionPool.class) {
                // Second check: Ensure another thread didn't create the instance while waiting
                if (instance == null) {
                    instance = new DatabaseConnectionPool();
                }
            }
        }
        return instance;
    }

    // Example method demonstrating pool usage
    public void getConnection() {
        System.out.println("Providing database connection from pool");
    }
}

// Usage example
public class Main {
    public static void main(String[] args) {
        // Multiple calls return the same instance
        DatabaseConnectionPool pool1 = DatabaseConnectionPool.getInstance();
        DatabaseConnectionPool pool2 = DatabaseConnectionPool.getInstance();

        System.out.println("Same instance: " + (pool1 == pool2)); // true

        pool1.getConnection();
    }
}
```

**When to Use vs. When NOT to Use:**
- **Use when:** You need exactly one instance to coordinate actions across the system (e.g., configuration managers, thread pools, caches)
- **Use when:** Resource management requires centralized control (e.g., logging systems, device drivers)
- **NOT to use when:** The single instance creates tight coupling and makes unit testing difficult
- **NOT to use when:** In distributed systems where multiple instances might be needed across different JVMs
- **NOT to use when:** When the singleton holds mutable state that could cause concurrency issues

**Interview Gotchas:**
- **Reflection Attack:** How do you prevent breaking Singleton using reflection? Answer: Throw exception in constructor if instance already exists, or use enum-based Singleton.
- **Serialization Issue:** Singletons can be broken during deserialization. Solution: Implement `readResolve()` method to return the singleton instance.
- **ClassLoader Problem:** Multiple class loaders can create separate instances. Solution: Use enum or ensure single class loader.
- **Common Mistake:** Forgetting `volatile` in multi-threaded environments, leading to partially initialized objects.

### Builder

**Concept:** The Builder pattern separates the construction of a complex object from its representation, allowing the same construction process to create different representations. It solves the problem of telescoping constructors and improves readability for objects with many optional parameters.

**SDE2-Level Real-World Use Case:** Constructing HTTP requests in REST clients, where requests may have optional headers, query parameters, timeouts, and authentication, allowing flexible request building without complex constructor overloads.

**Java Implementation (The Code):**
```java
// Product class with many optional fields
public class HttpRequest {
    private final String url;           // Required field
    private final String method;        // Required field
    private final Map<String, String> headers;  // Optional
    private final Map<String, String> queryParams; // Optional
    private final int timeout;          // Optional, with default
    private final String authToken;     // Optional

    // Private constructor ensures objects are created only through Builder
    private HttpRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers != null ? new HashMap<>(builder.headers) : new HashMap<>();
        this.queryParams = builder.queryParams != null ? new HashMap<>(builder.queryParams) : new HashMap<>();
        this.timeout = builder.timeout;
        this.authToken = builder.authToken;
    }

    // Getters omitted for brevity
    public String getUrl() { return url; }
    public String getMethod() { return method; }
    public Map<String, String> getHeaders() { return new HashMap<>(headers); }
    // ... other getters

    // Static inner Builder class
    public static class Builder {
        // Required fields
        private final String url;
        private final String method;

        // Optional fields with defaults
        private Map<String, String> headers = new HashMap<>();
        private Map<String, String> queryParams = new HashMap<>();
        private int timeout = 5000; // Default 5 seconds
        private String authToken = null;

        // Constructor with required parameters
        public Builder(String url, String method) {
            this.url = url;
            this.method = method;
        }

        // Fluent methods for optional parameters
        public Builder addHeader(String key, String value) {
            this.headers.put(key, value);
            return this; // Return this for method chaining
        }

        public Builder addQueryParam(String key, String value) {
            this.queryParams.put(key, value);
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder authToken(String authToken) {
            this.authToken = authToken;
            return this;
        }

        // Build method validates and creates the object
        public HttpRequest build() {
            // Validation logic
            if (url == null || url.trim().isEmpty()) {
                throw new IllegalArgumentException("URL cannot be null or empty");
            }
            if (method == null || method.trim().isEmpty()) {
                throw new IllegalArgumentException("HTTP method cannot be null or empty");
            }
            return new HttpRequest(this);
        }
    }
}

// Usage example
public class HttpClient {
    public static void main(String[] args) {
        // Build a complex request fluently
        HttpRequest request = new HttpRequest.Builder("https://api.example.com/users", "GET")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer token123")
                .addQueryParam("page", "1")
                .addQueryParam("limit", "10")
                .timeout(10000)
                .authToken("token123")
                .build();

        System.out.println("Request URL: " + request.getUrl());
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Headers: " + request.getHeaders());
    }
}
```

**When to Use vs. When NOT to Use:**
- **Use when:** Objects have many optional parameters or complex construction logic
- **Use when:** You want to create immutable objects with validated state
- **Use when:** Construction should be separated from representation
- **NOT to use when:** Objects are simple with few parameters (use constructors instead)
- **NOT to use when:** Performance is critical and builder overhead is unacceptable

**Interview Gotchas:**
- **Immutability:** Forgetting to make the product class immutable can lead to thread safety issues.
- **Validation:** Not validating parameters in the build() method can result in invalid objects.
- **Method Chaining:** Returning 'this' from builder methods is crucial for fluent API.
- **Common Mistake:** Making builder fields public instead of private, breaking encapsulation.

### Factory Method

**Concept:** The Factory Method pattern defines an interface for creating objects but lets subclasses decide which class to instantiate. It solves the problem of tight coupling between client code and specific classes by delegating object creation to subclasses.

**SDE2-Level Real-World Use Case:** Payment processing systems where different payment gateways (Stripe, PayPal, Square) need to be instantiated based on configuration or user preference, allowing the system to support multiple payment providers without changing client code.

**Java Implementation (The Code):**
```java
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

// Client code that decides which factory to use
public class PaymentProcessor {
    private final PaymentGatewayFactory factory;

    // Dependency injection of factory
    public PaymentProcessor(PaymentGatewayFactory factory) {
        this.factory = factory;
    }

    public void processPayment(double amount) {
        factory.processOrder(amount);
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

        PaymentProcessor processor = new PaymentProcessor(factory);
        processor.processPayment(99.99);
    }
}
```

**When to Use vs. When NOT to Use:**
- **Use when:** A class can't anticipate which objects it needs to create
- **Use when:** You want to localize knowledge of which subclass to create
- **Use when:** Frameworks need to create objects without knowing specific types
- **NOT to use when:** The creation logic is simple and doesn't vary
- **NOT to use when:** You have a small, fixed set of objects (use simple factory instead)

**Interview Gotchas:**
- **Abstract Factory Confusion:** Candidates often confuse Factory Method with Abstract Factory. Factory Method uses inheritance, Abstract Factory uses composition.
- **Parameter Passing:** How to pass parameters to factory methods? Answer: Through method parameters or factory constructor.
- **Testing:** Factory methods can make unit testing difficult. Solution: Inject factories or use factory interfaces.
- **Common Mistake:** Making factory methods static, which prevents polymorphism.

## Structural Patterns

### Adapter

**Concept:** The Adapter pattern allows incompatible interfaces to work together by wrapping one interface to match another. It solves the problem of integrating existing code with new interfaces without modifying the original code.

**SDE2-Level Real-World Use Case:** Integrating third-party payment gateways in an e-commerce platform, where each gateway has different APIs but the system needs a unified interface for processing payments.

**Java Implementation (The Code):**
```java
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
public class Main {
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
```

**When to Use vs. When NOT to Use:**
- **Use when:** You need to integrate existing code with new interfaces
- **Use when:** Third-party libraries have incompatible APIs
- **Use when:** You want to create reusable adapters for common integrations
- **NOT to use when:** Both interfaces are under your control (refactor instead)
- **NOT to use when:** The adaptation logic is complex and error-prone

**Interview Gotchas:**
- **Object vs. Class Adapter:** Class adapters use inheritance, object adapters use composition. Object adapters are more flexible.
- **Two-Way Adapters:** Sometimes you need bidirectional adaptation. Solution: Implement both interfaces.
- **Performance Overhead:** Adapters add indirection. Consider if the overhead is acceptable.
- **Common Mistake:** Forgetting to handle all method signatures in the adapter.

### Decorator

**Concept:** The Decorator pattern dynamically adds responsibilities to objects without modifying their structure. It solves the problem of extending functionality through inheritance by using composition instead.

**SDE2-Level Real-World Use Case:** Adding cross-cutting concerns like logging, caching, and security to service methods in a microservices architecture, allowing features to be mixed and matched without modifying core business logic.

**Java Implementation (The Code):**
```java
// Component interface
public interface DataService {
    String fetchData(String key);
}

// Concrete component
public class DatabaseDataService implements DataService {
    @Override
    public String fetchData(String key) {
        System.out.println("Fetching data from database for key: " + key);
        return "Data for " + key; // Simulated database fetch
    }
}

// Base decorator class that implements the component interface
public abstract class DataServiceDecorator implements DataService {
    protected final DataService wrappedService; // Composition over inheritance

    // Constructor takes the service to decorate
    protected DataServiceDecorator(DataService service) {
        this.wrappedService = service;
    }

    // Default implementation delegates to wrapped service
    @Override
    public String fetchData(String key) {
        return wrappedService.fetchData(key);
    }
}

// Concrete decorators adding specific functionality

// Logging decorator
public class LoggingDataService extends DataServiceDecorator {
    public LoggingDataService(DataService service) {
        super(service);
    }

    @Override
    public String fetchData(String key) {
        System.out.println("[LOG] Starting data fetch for key: " + key);
        long startTime = System.currentTimeMillis();

        String result = super.fetchData(key); // Call wrapped service

        long endTime = System.currentTimeMillis();
        System.out.println("[LOG] Completed data fetch in " + (endTime - startTime) + "ms");

        return result;
    }
}

// Caching decorator
public class CachingDataService extends DataServiceDecorator {
    private final Map<String, String> cache = new ConcurrentHashMap<>(); // Thread-safe cache

    public CachingDataService(DataService service) {
        super(service);
    }

    @Override
    public String fetchData(String key) {
        // Check cache first
        String cachedResult = cache.get(key);
        if (cachedResult != null) {
            System.out.println("[CACHE] Cache hit for key: " + key);
            return cachedResult;
        }

        // Cache miss - fetch from wrapped service
        System.out.println("[CACHE] Cache miss for key: " + key);
        String result = super.fetchData(key);
        cache.put(key, result); // Store in cache

        return result;
    }
}

// Security decorator
public class SecureDataService extends DataServiceDecorator {
    private final String requiredRole;

    public SecureDataService(DataService service, String requiredRole) {
        super(service);
        this.requiredRole = requiredRole;
    }

    @Override
    public String fetchData(String key) {
        // Simplified security check
        String userRole = getCurrentUserRole(); // Would get from security context
        if (!requiredRole.equals(userRole)) {
            throw new SecurityException("Access denied. Required role: " + requiredRole);
        }

        System.out.println("[SECURITY] Access granted for role: " + userRole);
        return super.fetchData(key);
    }

    private String getCurrentUserRole() {
        // Simulated - in real app, get from SecurityContext
        return "ADMIN";
    }
}

// Usage example - decorators can be stacked
public class DataServiceClient {
    public static void main(String[] args) {
        // Start with base service
        DataService service = new DatabaseDataService();

        // Add logging
        service = new LoggingDataService(service);

        // Add caching
        service = new CachingDataService(service);

        // Add security
        service = new SecureDataService(service, "ADMIN");

        // Use the decorated service
        String result1 = service.fetchData("user123");
        System.out.println("Result: " + result1);

        // Second call should use cache
        String result2 = service.fetchData("user123");
        System.out.println("Result: " + result2);
    }
}
```

**When to Use vs. When NOT to Use:**
- **Use when:** You need to add responsibilities dynamically at runtime
- **Use when:** Inheritance would create too many subclasses
- **Use when:** You want to follow Open/Closed Principle
- **NOT to use when:** The component interface is complex (many methods to override)
- **NOT to use when:** Performance is critical and decoration overhead is unacceptable

**Interview Gotchas:**
- **Decorator vs. Inheritance:** Decorators use composition, allowing multiple decorations. Inheritance is static.
- **Transparent Decorators:** Decorators should be substitutable for the component they wrap.
- **Order Matters:** The order of decoration affects behavior (e.g., security before caching).
- **Common Mistake:** Forgetting to call super.method() in decorators, breaking the chain.

### Facade

**Concept:** The Facade pattern provides a simplified interface to a complex subsystem, hiding its complexity from clients. It solves the problem of tight coupling between clients and subsystem components by providing a unified entry point.

**SDE2-Level Real-World Use Case:** Simplifying access to a complex order processing system in an e-commerce platform, where clients need to place orders without understanding the intricacies of inventory management, payment processing, and shipping coordination.

**Java Implementation (The Code):**
```java
// Complex subsystem classes
public class InventoryService {
    public boolean checkStock(String productId, int quantity) {
        System.out.println("Checking stock for product " + productId + ", quantity: " + quantity);
        return true; // Simplified - would check actual inventory
    }

    public void reserveStock(String productId, int quantity) {
        System.out.println("Reserving " + quantity + " units of product " + productId);
    }
}

public class PaymentService {
    public boolean processPayment(String paymentMethod, double amount) {
        System.out.println("Processing " + paymentMethod + " payment for $" + amount);
        return true; // Simplified - would integrate with payment gateway
    }

    public void refundPayment(String transactionId) {
        System.out.println("Processing refund for transaction " + transactionId);
    }
}

public class ShippingService {
    public String arrangeShipping(String address, List<String> productIds) {
        System.out.println("Arranging shipping to " + address + " for products: " + productIds);
        return "SHIP123"; // Return tracking number
    }

    public void updateShippingStatus(String trackingNumber, String status) {
        System.out.println("Updating shipping status for " + trackingNumber + " to " + status);
    }
}

public class NotificationService {
    public void sendOrderConfirmation(String orderId, String customerEmail) {
        System.out.println("Sending order confirmation for " + orderId + " to " + customerEmail);
    }

    public void sendShippingUpdate(String trackingNumber, String customerEmail) {
        System.out.println("Sending shipping update for " + trackingNumber + " to " + customerEmail);
    }
}

// Facade class that provides simplified interface
public class OrderProcessingFacade {
    // Composition: Facade contains references to subsystem components
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final ShippingService shippingService;
    private final NotificationService notificationService;

    // Constructor injects all dependencies
    public OrderProcessingFacade() {
        this.inventoryService = new InventoryService();
        this.paymentService = new PaymentService();
        this.shippingService = new ShippingService();
        this.notificationService = new NotificationService();
    }

    // Simplified method that orchestrates complex subsystem interactions
    public OrderResult placeOrder(OrderRequest request) {
        // Step 1: Check and reserve inventory
        if (!inventoryService.checkStock(request.getProductId(), request.getQuantity())) {
            return new OrderResult(false, "Out of stock", null);
        }
        inventoryService.reserveStock(request.getProductId(), request.getQuantity());

        // Step 2: Process payment
        if (!paymentService.processPayment(request.getPaymentMethod(), request.getTotalAmount())) {
            return new OrderResult(false, "Payment failed", null);
        }

        // Step 3: Arrange shipping
        String trackingNumber = shippingService.arrangeShipping(
            request.getShippingAddress(),
            List.of(request.getProductId())
        );

        // Step 4: Send notifications
        String orderId = generateOrderId();
        notificationService.sendOrderConfirmation(orderId, request.getCustomerEmail());

        return new OrderResult(true, "Order placed successfully", orderId);
    }

    // Additional facade methods for other operations
    public void cancelOrder(String orderId) {
        // Complex cancellation logic hidden behind simple interface
        paymentService.refundPayment(orderId);
        shippingService.updateShippingStatus(orderId, "CANCELLED");
        notificationService.sendOrderConfirmation(orderId, "customer@example.com");
    }

    private String generateOrderId() {
        return "ORD" + System.currentTimeMillis();
    }
}

// Supporting classes
public class OrderRequest {
    private final String productId;
    private final int quantity;
    private final double totalAmount;
    private final String paymentMethod;
    private final String shippingAddress;
    private final String customerEmail;

    public OrderRequest(String productId, int quantity, double totalAmount,
                       String paymentMethod, String shippingAddress, String customerEmail) {
        this.productId = productId;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.customerEmail = customerEmail;
    }

    // Getters omitted for brevity
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public double getTotalAmount() { return totalAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getShippingAddress() { return shippingAddress; }
    public String getCustomerEmail() { return customerEmail; }
}

public class OrderResult {
    private final boolean success;
    private final String message;
    private final String orderId;

    public OrderResult(boolean success, String message, String orderId) {
        this.success = success;
        this.message = message;
        this.orderId = orderId;
    }

    // Getters omitted for brevity
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getOrderId() { return orderId; }
}

// Client code - simple to use
public class EcommerceClient {
    public static void main(String[] args) {
        OrderProcessingFacade orderFacade = new OrderProcessingFacade();

        OrderRequest request = new OrderRequest(
            "PROD123", 2, 199.98, "CREDIT_CARD",
            "123 Main St, Anytown, USA", "customer@example.com"
        );

        OrderResult result = orderFacade.placeOrder(request);

        if (result.isSuccess()) {
            System.out.println("Order successful: " + result.getOrderId());
        } else {
            System.out.println("Order failed: " + result.getMessage());
        }
    }
}
```

**When to Use vs. When NOT to Use:**
- **Use when:** A subsystem is complex and needs simplification
- **Use when:** You want to reduce coupling between clients and subsystems
- **Use when:** Layering your application architecture
- **NOT to use when:** The subsystem is already simple
- **NOT to use when:** Clients need fine-grained control over subsystem components

**Interview Gotchas:**
- **Facade vs. Adapter:** Facade simplifies interfaces, Adapter converts interfaces. Facade may not implement the same interface as subsystems.
- **God Object Anti-pattern:** Facades can become bloated. Solution: Split into multiple facades.
- **Testing:** Facades can hide testing complexity. Solution: Test subsystems separately.
- **Common Mistake:** Making facades stateful when they should be stateless.

## Behavioral Patterns

### Observer

**Concept:** The Observer pattern defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically. It solves the problem of maintaining consistency between related objects without tight coupling.

**SDE2-Level Real-World Use Case:** Real-time dashboard updates in a monitoring system, where multiple UI components need to react to changes in system metrics or alerts without polling the data source continuously.

**Java Implementation (The Code):**
```java
import java.util.concurrent.CopyOnWriteArrayList; // Thread-safe list for iterations
import java.util.List;

// Subject interface (Observable)
public interface Subject {
    void attach(Observer observer);   // Add observer
    void detach(Observer observer);   // Remove observer
    void notifyObservers();           // Notify all observers
}

// Observer interface
public interface Observer {
    void update(Subject subject, Object data); // Called when subject changes
}

// Concrete subject - System metrics monitor
public class SystemMetricsMonitor implements Subject {
    // Thread-safe list to handle concurrent modifications during iteration
    private final List<Observer> observers = new CopyOnWriteArrayList<>();

    private double cpuUsage;
    private double memoryUsage;
    private int activeConnections;

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
        System.out.println("Observer attached: " + observer.getClass().getSimpleName());
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
        System.out.println("Observer detached: " + observer.getClass().getSimpleName());
    }

    @Override
    public void notifyObservers() {
        // Iterate safely even if observers modify the list during notification
        for (Observer observer : observers) {
            // Pass 'this' as subject and current metrics as data
            observer.update(this, getCurrentMetrics());
        }
    }

    // Method to update metrics and notify observers
    public void updateMetrics(double cpu, double memory, int connections) {
        this.cpuUsage = cpu;
        this.memoryUsage = memory;
        this.activeConnections = connections;

        System.out.println("Metrics updated: CPU=" + cpu + "%, Memory=" + memory + "%, Connections=" + connections);
        notifyObservers(); // Notify all observers of the change
    }

    // Get current metrics as a data object
    private MetricsData getCurrentMetrics() {
        return new MetricsData(cpuUsage, memoryUsage, activeConnections);
    }

    // Getters for direct access
    public double getCpuUsage() { return cpuUsage; }
    public double getMemoryUsage() { return memoryUsage; }
    public int getActiveConnections() { return activeConnections; }
}

// Data class for metrics
public class MetricsData {
    public final double cpuUsage;
    public final double memoryUsage;
    public final int activeConnections;

    public MetricsData(double cpuUsage, double memoryUsage, int activeConnections) {
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.activeConnections = activeConnections;
    }
}

// Concrete observers

// Dashboard display observer
public class DashboardObserver implements Observer {
    @Override
    public void update(Subject subject, Object data) {
        if (data instanceof MetricsData metrics) {
            System.out.println("[DASHBOARD] Updating display:");
            System.out.println("  CPU Usage: " + metrics.cpuUsage + "%");
            System.out.println("  Memory Usage: " + metrics.memoryUsage + "%");
            System.out.println("  Active Connections: " + metrics.activeConnections);
        }
    }
}

// Alert system observer
public class AlertObserver implements Observer {
    private static final double CPU_THRESHOLD = 90.0;
    private static final double MEMORY_THRESHOLD = 85.0;

    @Override
    public void update(Subject subject, Object data) {
        if (data instanceof MetricsData metrics) {
            if (metrics.cpuUsage > CPU_THRESHOLD) {
                System.out.println("[ALERT] High CPU usage detected: " + metrics.cpuUsage + "%");
            }
            if (metrics.memoryUsage > MEMORY_THRESHOLD) {
                System.out.println("[ALERT] High memory usage detected: " + metrics.memoryUsage + "%");
            }
            if (metrics.activeConnections > 1000) {
                System.out.println("[ALERT] High connection count: " + metrics.activeConnections);
            }
        }
    }
}

// Logging observer
public class LoggingObserver implements Observer {
    @Override
    public void update(Subject subject, Object data) {
        if (data instanceof MetricsData metrics) {
            System.out.println("[LOG] Metrics snapshot - CPU: " + metrics.cpuUsage +
                             "%, Memory: " + metrics.memoryUsage + "%, Connections: " + metrics.activeConnections);
        }
    }
}

// Usage example
public class MonitoringSystem {
    public static void main(String[] args) {
        // Create subject
        SystemMetricsMonitor monitor = new SystemMetricsMonitor();

        // Attach observers
        Observer dashboard = new DashboardObserver();
        Observer alerts = new AlertObserver();
        Observer logger = new LoggingObserver();

        monitor.attach(dashboard);
        monitor.attach(alerts);
        monitor.attach(logger);

        // Simulate metrics updates
        monitor.updateMetrics(45.2, 67.8, 234);
        System.out.println();

        monitor.updateMetrics(95.1, 88.3, 1200); // Should trigger alerts
        System.out.println();

        // Detach an observer
        monitor.detach(logger);
        monitor.updateMetrics(50.0, 60.0, 150);
    }
}
```

**When to Use vs. When NOT to Use:**
- **Use when:** Changes in one object need to be reflected in others automatically
- **Use when:** You have a broadcast communication mechanism
- **Use when:** Loose coupling between subjects and observers is important
- **NOT to use when:** Updates are frequent and performance-critical
- **NOT to use when:** Observers need to query the subject for specific data

**Interview Gotchas:**
- **Memory Leaks:** Forgetting to detach observers can cause memory leaks. Solution: Use weak references.
- **Thread Safety:** Concurrent modifications during notification. Solution: Use thread-safe collections like CopyOnWriteArrayList.
- **Observer Ordering:** No guarantee of notification order. Don't depend on sequence.
- **Common Mistake:** Making observers query the subject instead of pushing data.

### Strategy

**Concept:** The Strategy pattern defines a family of algorithms, encapsulates each one, and makes them interchangeable at runtime. It solves the problem of having multiple ways to perform an operation without using conditional statements.

**SDE2-Level Real-World Use Case:** Payment processing in an e-commerce platform where different payment methods (credit card, PayPal, cryptocurrency) have different processing logic but need to be interchangeable based on user selection.

**Java Implementation (The Code):**
```java
import java.util.Map;
import java.util.HashMap;

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
public class EcommerceCheckout {
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
```

**When to Use vs. When NOT to Use:**
- **Use when:** Multiple algorithms exist for the same task
- **Use when:** Algorithm selection needs to happen at runtime
- **Use when:** You want to avoid conditional statements for algorithm selection
- **NOT to use when:** There's only one way to perform the operation
- **NOT to use when:** The algorithms are tightly coupled to specific data

**Interview Gotchas:**
- **Strategy vs. State:** Strategy is chosen by client, State changes based on internal state.
- **Composition over Inheritance:** Strategies use composition, not inheritance hierarchies.
- **Strategy Creation:** Who creates strategies? Client or factory? Depends on complexity.
- **Common Mistake:** Making strategies stateful when they should be stateless.

### Chain of Responsibility

**Concept:** The Chain of Responsibility pattern passes requests along a chain of handlers, where each handler decides whether to process the request or pass it to the next handler. It solves the problem of coupling senders and receivers by giving multiple objects a chance to handle requests.

**SDE2-Level Real-World Use Case:** Request processing pipeline in a web application, where HTTP requests go through multiple middleware layers (authentication, logging, rate limiting, caching) before reaching the business logic.

**Java Implementation (The Code):**
```java
// Handler interface
public interface RequestHandler {
    void setNext(RequestHandler nextHandler); // Set the next handler in chain
    void handle(Request request); // Process the request or pass it along
}

// Abstract base handler with common functionality
public abstract class AbstractRequestHandler implements RequestHandler {
    protected RequestHandler nextHandler; // Next handler in the chain

    @Override
    public void setNext(RequestHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    // Template method for handling requests
    @Override
    public void handle(Request request) {
        if (canHandle(request)) {
            processRequest(request);
        }
        if (nextHandler != null) {
            nextHandler.handle(request); // Pass to next handler regardless
        }
    }

    // Subclasses implement these
    protected abstract boolean canHandle(Request request);
    protected abstract void processRequest(Request request);
}

// Concrete handlers

// Authentication handler
public class AuthenticationHandler extends AbstractRequestHandler {
    @Override
    protected boolean canHandle(Request request) {
        return request.getHeaders().containsKey("Authorization");
    }

    @Override
    protected void processRequest(Request request) {
        String authHeader = request.getHeaders().get("Authorization");
        if (authHeader == null || !isValidToken(authHeader)) {
            throw new SecurityException("Invalid or missing authentication token");
        }
        System.out.println("[AUTH] User authenticated successfully");
        request.setAuthenticatedUser(extractUserFromToken(authHeader));
    }

    private boolean isValidToken(String token) {
        // Simplified token validation
        return token.startsWith("Bearer ") && token.length() > 10;
    }

    private String extractUserFromToken(String token) {
        // Simplified user extraction
        return "user123";
    }
}

// Logging handler
public class LoggingHandler extends AbstractRequestHandler {
    @Override
    protected boolean canHandle(Request request) {
        return true; // Always log requests
    }

    @Override
    protected void processRequest(Request request) {
        System.out.println("[LOG] Request: " + request.getMethod() + " " + request.getPath() +
                          " from " + request.getRemoteAddress());
    }
}

// Rate limiting handler
public class RateLimitingHandler extends AbstractRequestHandler {
    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();
    private final int maxRequestsPerMinute = 100;

    @Override
    protected boolean canHandle(Request request) {
        return true; // Check rate limit for all requests
    }

    @Override
    protected void processRequest(Request request) {
        String clientId = request.getRemoteAddress(); // Use IP as client identifier
        int currentCount = requestCounts.getOrDefault(clientId, 0);

        if (currentCount >= maxRequestsPerMinute) {
            throw new RuntimeException("Rate limit exceeded for client: " + clientId);
        }

        requestCounts.put(clientId, currentCount + 1);
        System.out.println("[RATE LIMIT] Request allowed for " + clientId + " (count: " + (currentCount + 1) + ")");
    }
}

// Caching handler
public class CachingHandler extends AbstractRequestHandler {
    private final Map<String, Response> cache = new ConcurrentHashMap<>();

    @Override
    protected boolean canHandle(Request request) {
        return "GET".equals(request.getMethod()); // Only cache GET requests
    }

    @Override
    protected void processRequest(Request request) {
        String cacheKey = request.getMethod() + ":" + request.getPath();

        if (cache.containsKey(cacheKey)) {
            System.out.println("[CACHE] Cache hit for " + cacheKey);
            request.setCachedResponse(cache.get(cacheKey));
            // Don't call next handler - return cached response
            return;
        }

        System.out.println("[CACHE] Cache miss for " + cacheKey);
        // Continue to next handler, response will be cached on the way back
    }

    // Method to cache responses (called by response handler)
    public void cacheResponse(String cacheKey, Response response) {
        cache.put(cacheKey, response);
        System.out.println("[CACHE] Response cached for " + cacheKey);
    }
}

// Business logic handler (end of chain)
public class BusinessLogicHandler extends AbstractRequestHandler {
    @Override
    protected boolean canHandle(Request request) {
        return true; // Handle all remaining requests
    }

    @Override
    protected void processRequest(Request request) {
        System.out.println("[BUSINESS] Processing business logic for " + request.getPath());

        // Simulate business logic
        String responseData = "Processed request for user: " + request.getAuthenticatedUser();

        Response response = new Response(200, responseData, Map.of("Content-Type", "application/json"));

        // Cache the response if it was a GET request
        if ("GET".equals(request.getMethod())) {
            String cacheKey = request.getMethod() + ":" + request.getPath();
            // In a real implementation, you'd get the caching handler from the chain
            // For simplicity, we'll assume it's available
        }

        request.setResponse(response);
    }
}

// Supporting classes
public class Request {
    private final String method;
    private final String path;
    private final String remoteAddress;
    private final Map<String, String> headers;
    private String authenticatedUser;
    private Response response;
    private Response cachedResponse;

    public Request(String method, String path, String remoteAddress, Map<String, String> headers) {
        this.method = method;
        this.path = path;
        this.remoteAddress = remoteAddress;
        this.headers = new HashMap<>(headers);
    }

    // Getters and setters
    public String getMethod() { return method; }
    public String getPath() { return path; }
    public String getRemoteAddress() { return remoteAddress; }
    public Map<String, String> getHeaders() { return new HashMap<>(headers); }
    public String getAuthenticatedUser() { return authenticatedUser; }
    public void setAuthenticatedUser(String user) { authenticatedUser = user; }
    public Response getResponse() { return response; }
    public void setResponse(Response response) { this.response = response; }
    public Response getCachedResponse() { return cachedResponse; }
    public void setCachedResponse(Response cachedResponse) { this.cachedResponse = cachedResponse; }
}

public class Response {
    private final int statusCode;
    private final String body;
    private final Map<String, String> headers;

    public Response(int statusCode, String body, Map<String, String> headers) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = new HashMap<>(headers);
    }

    public int getStatusCode() { return statusCode; }
    public String getBody() { return body; }
    public Map<String, String> getHeaders() { return new HashMap<>(headers); }
}

// Chain builder and executor
public class RequestProcessor {
    private final RequestHandler chainHead;

    public RequestProcessor() {
        // Build the chain: Logging -> Rate Limiting -> Authentication -> Caching -> Business Logic
        RequestHandler logging = new LoggingHandler();
        RequestHandler rateLimiting = new RateLimitingHandler();
        RequestHandler authentication = new AuthenticationHandler();
        RequestHandler caching = new CachingHandler();
        RequestHandler businessLogic = new BusinessLogicHandler();

        // Link the chain
        logging.setNext(rateLimiting);
        rateLimiting.setNext(authentication);
        authentication.setNext(caching);
        caching.setNext(businessLogic);

        this.chainHead = logging;
    }

    public Response processRequest(Request request) {
        try {
            chainHead.handle(request);

            // Return cached response if available, otherwise the processed response
            return request.getCachedResponse() != null ?
                   request.getCachedResponse() : request.getResponse();

        } catch (Exception e) {
            System.err.println("Request processing failed: " + e.getMessage());
            return new Response(500, "Internal Server Error: " + e.getMessage(), Map.of());
        }
    }
}

// Usage example
public class WebServer {
    public static void main(String[] args) {
        RequestProcessor processor = new RequestProcessor();

        // Simulate requests
        Map<String, String> headers1 = Map.of("Authorization", "Bearer valid-token-123");
        Request request1 = new Request("GET", "/api/users", "192.168.1.1", headers1);

        Response response1 = processor.processRequest(request1);
        System.out.println("Response 1: " + response1.getStatusCode() + " - " + response1.getBody());

        // Second request to same endpoint (should use cache)
        Request request2 = new Request("GET", "/api/users", "192.168.1.1", headers1);
        Response response2 = processor.processRequest(request2);
        System.out.println("Response 2: " + response2.getStatusCode() + " - " + response2.getBody());

        // Request without authentication (should fail)
        Request request3 = new Request("GET", "/api/users", "192.168.1.1", Map.of());
        Response response3 = processor.processRequest(request3);
        System.out.println("Response 3: " + response3.getStatusCode() + " - " + response3.getBody());
    }
}
```

**When to Use vs. When NOT to Use:**
- **Use when:** Multiple handlers might process a request in different ways
- **Use when:** Handler selection isn't known at compile time
- **Use when:** You want to decouple senders from receivers
- **NOT to use when:** There's only one handler for requests
- **NOT to use when:** Performance is critical and chain traversal is expensive

**Interview Gotchas:**
- **Chain Construction:** Who builds the chain? Client code or factory? Factory is usually better.
- **Handler Ordering:** Order matters. Authentication usually comes before business logic.
- **Breaking the Chain:** Some handlers might stop processing (return without calling next).
- **Common Mistake:** Making handlers stateful when they should be stateless.