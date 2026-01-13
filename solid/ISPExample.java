// SOLID Principles - Interface Segregation Principle Example
// Bad Example: Fat interface
public interface WorkerBad {
    void work();
    void eat();
    void sleep();
}

public class HumanWorkerBad implements WorkerBad {
    @Override
    public void work() { System.out.println("Human working"); }

    @Override
    public void eat() { System.out.println("Human eating"); }

    @Override
    public void sleep() { System.out.println("Human sleeping"); }
}

public class RobotWorkerBad implements WorkerBad {
    @Override
    public void work() { System.out.println("Robot working"); }

    @Override
    public void eat() { throw new UnsupportedOperationException(); }

    @Override
    public void sleep() { throw new UnsupportedOperationException(); }
}

// Good Example: Segregated interfaces
public interface Workable {
    void work();
}

public interface Eatable {
    void eat();
}

public interface Sleepable {
    void sleep();
}

public sealed interface Worker permits HumanWorker, RobotWorker, AdvancedRobotWorker {
    String getId();
}

public final class HumanWorker implements Worker, Workable, Eatable, Sleepable {
    private final String id;

    public HumanWorker(String id) { this.id = id; }

    @Override
    public String getId() { return id; }

    @Override
    public void work() { System.out.println("Human working"); }

    @Override
    public void eat() { System.out.println("Human eating"); }

    @Override
    public void sleep() { System.out.println("Human sleeping"); }
}

public final class RobotWorker implements Worker, Workable {
    private final String id;

    public RobotWorker(String id) { this.id = id; }

    @Override
    public String getId() { return id; }

    @Override
    public void work() { System.out.println("Robot working"); }
}

public final class AdvancedRobotWorker implements Worker, Workable, Eatable {
    private final String id;

    public AdvancedRobotWorker(String id) { this.id = id; }

    @Override
    public String getId() { return id; }

    @Override
    public void work() { System.out.println("Advanced robot working"); }

    @Override
    public void eat() { System.out.println("Advanced robot recharging"); }
}

// Usage: Clients depend only on what they need
public class WorkManager {
    private final List<Workable> workers;

    public WorkManager(List<Workable> workers) {
        this.workers = workers;
    }

    public void manageWork() {
        workers.forEach(Workable::work);
    }
}

public class CafeteriaManager {
    private final List<Eatable> eaters;

    public CafeteriaManager(List<Eatable> eaters) {
        this.eaters = eaters;
    }

    public void serveLunch() {
        eaters.forEach(Eatable::eat);
    }
}