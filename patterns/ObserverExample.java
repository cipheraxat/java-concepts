// Observer Pattern Example
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

    // Getters
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
public class ObserverDemo {
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