// Facade Pattern Example
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

    // Getters
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

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getOrderId() { return orderId; }
}

// Client code - simple to use
public class FacadeDemo {
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