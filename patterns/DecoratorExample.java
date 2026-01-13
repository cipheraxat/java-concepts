// Decorator Pattern Example
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

// Base decorator class with common functionality
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

// Concrete decorators

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
public class DecoratorDemo {
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