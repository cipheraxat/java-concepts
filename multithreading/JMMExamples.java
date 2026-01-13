// Java Memory Model Examples
public class VolatileExample {
    private volatile boolean flag = false;

    public void writer() {
        flag = true; // Write to volatile variable - immediately visible to other threads
    }

    public void reader() {
        if (flag) { // Read from volatile - sees the latest value
            System.out.println("Flag is true");
        }
    }
}

public class SynchronizedExample {
    private int counter = 0;

    public synchronized void increment() { // Intrinsic lock on 'this'
        counter++; // Atomic operation within the synchronized block
    }

    public synchronized int getCounter() {
        return counter; // Visibility guaranteed - sees latest value
    }
}

public class VisibilityVsAtomicity {
    private volatile int visibleCounter = 0; // Visible but not atomic for ++
    private final AtomicInteger atomicCounter = new AtomicInteger(0); // Both visible and atomic

    public void incrementVisible() {
        visibleCounter++; // Not atomic - race condition possible
    }

    public void incrementAtomic() {
        atomicCounter.incrementAndGet(); // Atomic using CAS
    }
}

public class HappensBeforeExample {
    private int x = 0;
    private volatile boolean flag = false;

    public void thread1() {
        x = 42;        // Operation A
        flag = true;   // Operation B - volatile write
    }

    public void thread2() {
        if (flag) {    // Operation C - volatile read
            // Happens-before guarantees: if we see flag=true, we also see x=42
            System.out.println(x); // Will print 42, not 0
        }
    }
}