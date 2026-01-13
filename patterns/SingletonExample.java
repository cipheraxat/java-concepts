// Singleton Pattern Example
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
public class SingletonDemo {
    public static void main(String[] args) {
        // Multiple calls return the same instance
        DatabaseConnectionPool pool1 = DatabaseConnectionPool.getInstance();
        DatabaseConnectionPool pool2 = DatabaseConnectionPool.getInstance();

        System.out.println("Same instance: " + (pool1 == pool2)); // true

        pool1.getConnection();
    }
}