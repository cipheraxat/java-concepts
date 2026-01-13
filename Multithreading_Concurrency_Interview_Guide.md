# Multithreading & Concurrency in Java: SDE-2 Interview Preparation Guide

*Prepared by a Senior Principal Engineer at FAANG-level company*

This guide is designed for Senior Software Engineer (SDE-2) candidates preparing for technical interviews at top tech companies. It assumes familiarity with basic multithreading concepts and dives deep into advanced topics, internal architectures, performance trade-offs, and real-world application. Focus on understanding *why* mechanisms work rather than just *how* to use them.

## Java Memory Model (JMM)

The Java Memory Model defines how threads interact through memory, ensuring visibility and ordering of operations. It's crucial for writing correct concurrent programs.

### Volatile Keyword
`volatile` ensures visibility of changes across threads but does not guarantee atomicity for compound operations.

```java
public class VolatileExample {
    private volatile boolean flag = false; // Ensures visibility across threads

    public void writer() {
        flag = true; // Write to volatile variable - immediately visible to other threads
    }

    public void reader() {
        if (flag) { // Read from volatile - sees the latest value
            System.out.println("Flag is true");
        }
    }
}

// Why it works: volatile creates a memory barrier that prevents reordering and ensures
// that writes are flushed to main memory and reads are loaded from main memory.
// However, flag = !flag is not atomic - use AtomicBoolean for that.
```

### Synchronized Keyword
`synchronized` provides both mutual exclusion (atomicity) and visibility through monitor locks.

```java
public class SynchronizedExample {
    private int counter = 0;

    public synchronized void increment() { // Intrinsic lock on 'this'
        counter++; // Atomic operation within the synchronized block
    }

    public synchronized int getCounter() {
        return counter; // Visibility guaranteed - sees latest value
    }
}

// Why it works: Entering synchronized block acquires monitor lock, exiting releases it.
// All operations within the block are atomic and visible to other threads that synchronize
// on the same lock. However, synchronized can cause contention and performance issues.
```

### Visibility vs. Atomicity
- **Visibility**: Ensures one thread's changes are seen by others (handled by volatile/synchronized)
- **Atomicity**: Ensures operations complete without interleaving (handled by synchronized/Atomic classes)

```java
public class VisibilityVsAtomicity {
    private volatile int visibleCounter = 0; // Visible but not atomic for ++
    private AtomicInteger atomicCounter = new AtomicInteger(0); // Both visible and atomic

    public void incrementVisible() {
        visibleCounter++; // Not atomic - race condition possible
    }

    public void incrementAtomic() {
        atomicCounter.incrementAndGet(); // Atomic using CAS
    }
}
```

### Happens-Before Relationship
Defines the ordering guarantees between operations in different threads.

```java
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

// Key happens-before rules:
// 1. Volatile variable rule: write to volatile happens-before read
// 2. Monitor lock rule: unlock happens-before subsequent lock
// 3. Thread start rule: Thread.start() happens-before thread actions
// 4. Thread join rule: Thread.join() happens-before join returns
```

## Locking Mechanisms

Java provides multiple locking mechanisms with different trade-offs in performance, flexibility, and features.

### Intrinsic Locks (synchronized)
- **Pros**: Simple, built-in, automatic unlock on exceptions
- **Cons**: No timeout, no interruptibility, single condition per lock
- **Use when**: Simple synchronization, no advanced features needed

### ReentrantLock
- **Pros**: Interruptible, timeout support, multiple conditions, fairness option
- **Cons**: Manual lock management, potential for forgotten unlocks
- **Use when**: Need timeout, interruptibility, or multiple wait conditions

```java
public class LockComparison {
    private final ReentrantLock reentrantLock = new ReentrantLock();
    private final ReadWriteLock rwLock = new ReadWriteLock();
    private final StampedLock stampedLock = new StampedLock();

    // ReentrantLock with timeout
    public boolean tryIncrement(long timeout, TimeUnit unit) throws InterruptedException {
        if (reentrantLock.tryLock(timeout, unit)) { // Interruptible with timeout
            try {
                counter++;
                return true;
            } finally {
                reentrantLock.unlock(); // Manual unlock required
            }
        }
        return false;
    }

    // ReadWriteLock for read-heavy workloads
    public int getCounter() {
        rwLock.readLock().lock(); // Multiple readers allowed
        try {
            return counter;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public void increment() {
        rwLock.writeLock().lock(); // Exclusive write access
        try {
            counter++;
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    // StampedLock for optimistic reading
    public int optimisticRead() {
        long stamp = stampedLock.tryOptimisticRead(); // No lock acquisition
        int value = counter;
        if (!stampedLock.validate(stamp)) { // Check if write occurred
            // Fallback to pessimistic read
            stamp = stampedLock.readLock();
            try {
                value = counter;
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        return value;
    }
}
```

### ReadWriteLock vs. StampedLock
- **ReadWriteLock**: Good for read-heavy workloads with some writes
- **StampedLock**: Better for read-mostly workloads; supports optimistic locking
- **Trade-off**: StampedLock is more complex but can provide better performance for optimistic scenarios

**When to use which:**
- **Intrinsic locks**: Simple cases, existing synchronized code
- **ReentrantLock**: Need timeouts, interruptibility, or multiple conditions
- **ReadWriteLock**: Read-heavy with occasional writes
- **StampedLock**: Very read-heavy, can tolerate occasional retries on optimistic failures

## The Executor Framework

The Executor framework provides a higher-level abstraction for managing thread pools and asynchronous task execution.

### ThreadPoolExecutor Internals
ThreadPoolExecutor manages a pool of worker threads and a queue of tasks.

```java
public class ThreadPoolExecutorDeepDive {
    // Core components:
    // - corePoolSize: Minimum threads to keep alive
    // - maximumPoolSize: Maximum threads allowed
    // - keepAliveTime: How long idle threads wait before terminating
    // - workQueue: BlockingQueue for pending tasks
    // - threadFactory: Factory for creating new threads
    // - rejectedExecutionHandler: What to do when queue is full

    public static ExecutorService createCustomThreadPool() {
        return new ThreadPoolExecutor(
            4,                          // corePoolSize
            8,                          // maximumPoolSize
            60L, TimeUnit.SECONDS,      // keepAliveTime
            new LinkedBlockingQueue<>(100), // bounded queue prevents unbounded growth
            new ThreadFactory() {
                private int count = 0;
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "CustomPool-" + count++);
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy() // Rejection policy
        );
    }
}

// Internal working:
// 1. Submit task to execute()
// 2. If fewer than core threads, create new thread
// 3. If core threads busy, queue task
// 4. If queue full and fewer than max threads, create new thread
// 5. If all threads busy and queue full, reject task
```

### Meaningful Thread Pool Sizing
Thread pool size depends on workload characteristics and system resources.

```java
public class ThreadPoolSizing {
    public static void main(String[] args) {
        int cores = Runtime.getRuntime().availableProcessors();

        // CPU-bound tasks: Pool size = number of cores
        ExecutorService cpuBoundPool = Executors.newFixedThreadPool(cores);

        // I/O-bound tasks: Pool size = cores * (1 + wait_time/compute_time)
        // For tasks with 50% I/O wait: cores * 2
        ExecutorService ioBoundPool = Executors.newFixedThreadPool(cores * 2);

        // Cached thread pool for short-lived tasks
        ExecutorService cachedPool = Executors.newCachedThreadPool();

        // Work-stealing pool for CPU-intensive parallel processing
        ExecutorService workStealingPool = Executors.newWorkStealingPool();
    }
}

// Sizing guidelines:
// - CPU-bound: cores to cores+1
// - I/O-bound: cores * (1 + I/O_time/CPU_time)
// - Mixed: Start with cores*2, monitor and adjust
// - Consider memory: Each thread ~1MB stack space
```

### ForkJoinPool
Designed for divide-and-conquer algorithms using work-stealing.

```java
public class ForkJoinExample {
    static class FibonacciTask extends RecursiveTask<Integer> {
        private final int n;

        FibonacciTask(int n) { this.n = n; }

        @Override
        protected Integer compute() {
            if (n <= 1) return n;

            FibonacciTask f1 = new FibonacciTask(n - 1);
            f1.fork(); // Asynchronous execution

            FibonacciTask f2 = new FibonacciTask(n - 2);
            int result2 = f2.compute(); // Synchronous execution

            return result2 + f1.join(); // Wait for f1 result
        }
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        FibonacciTask task = new FibonacciTask(10);
        int result = pool.invoke(task);
        System.out.println("Fibonacci(10) = " + result);
    }
}

// Why it works: Work-stealing balances load across threads
// When a thread completes its tasks, it "steals" work from other threads' deques
// Efficient for recursive, divide-and-conquer algorithms
```

## Modern Async Programming: CompletableFuture

CompletableFuture provides a powerful API for composing asynchronous operations.

### Chaining Operations
```java
public class CompletableFutureChaining {
    public static void main(String[] args) {
        CompletableFuture<String> future = CompletableFuture
            .supplyAsync(() -> fetchUserData())           // Start async operation
            .thenApply(userData -> parseUserData(userData)) // Transform result
            .thenApply(parsedData -> enrichUserData(parsedData)) // Chain another transformation
            .thenAccept(enrichedData -> saveToCache(enrichedData)) // Consume result
            .exceptionally(throwable -> {                 // Handle exceptions
                System.err.println("Error: " + throwable.getMessage());
                return "default"; // Provide fallback
            });

        // Wait for completion (in real code, avoid blocking)
        future.join();
    }

    private static String fetchUserData() { /* Simulate network call */ return "user:123"; }
    private static User parseUserData(String data) { /* Parse logic */ return new User(); }
    private static User enrichUserData(User user) { /* Enrichment logic */ return user; }
    private static void saveToCache(User user) { /* Cache logic */ }
}
```

### Exception Handling
```java
public class CompletableFutureExceptions {
    public static CompletableFuture<String> processWithRetry(String input) {
        return CompletableFuture
            .supplyAsync(() -> riskyOperation(input))
            .thenApply(result -> validateResult(result))
            .exceptionally(throwable -> {
                // Log and retry logic
                System.err.println("Operation failed: " + throwable.getMessage());
                return retryOperation(input);
            })
            .thenApply(result -> "Processed: " + result)
            .whenComplete((result, throwable) -> {
                if (throwable != null) {
                    System.err.println("Final failure: " + throwable.getMessage());
                } else {
                    System.out.println("Success: " + result);
                }
            });
    }

    // Handle specific exception types
    public static CompletableFuture<String> handleSpecificExceptions() {
        return CompletableFuture
            .supplyAsync(() -> networkCall())
            .handle((result, throwable) -> {
                if (throwable instanceof IOException) {
                    return "Network fallback";
                } else if (throwable instanceof TimeoutException) {
                    return "Timeout fallback";
                } else if (throwable != null) {
                    throw new CompletionException(throwable);
                } else {
                    return result;
                }
            });
    }
}
```

### Combining Futures
```java
public class CompletableFutureCombining {
    public static void main(String[] args) {
        CompletableFuture<String> userFuture = fetchUser();
        CompletableFuture<String> orderFuture = fetchOrder();

        // Combine two futures
        CompletableFuture<String> combined = userFuture
            .thenCombine(orderFuture, (user, order) -> user + " ordered " + order);

        // Wait for either to complete
        CompletableFuture<Object> either = userFuture
            .applyToEither(orderFuture, result -> "First result: " + result);

        // Wait for all to complete
        CompletableFuture<Void> all = CompletableFuture.allOf(userFuture, orderFuture);

        // Transform after all complete
        CompletableFuture<String> finalResult = all
            .thenApply(v -> "All operations completed");

        finalResult.join();
    }

    // Compose dependent futures
    public static CompletableFuture<String> dependentOperations() {
        return fetchUser()
            .thenCompose(user -> fetchUserOrders(user)) // Use result of first for second
            .thenCompose(orders -> processOrders(orders)); // Chain another dependent operation
    }
}
```

## Concurrent Collections

### ConcurrentHashMap Internals
ConcurrentHashMap uses lock striping and CAS for thread-safe operations.

```java
public class ConcurrentHashMapInternals {
    // Java 7: Segment-based locking
    // - Map divided into segments (16 by default)
    // - Each segment has its own lock
    // - Read operations can proceed concurrently across segments

    // Java 8+: CAS-based with Node locking
    // - No segments, but similar lock striping concept
    // - Uses CAS for updates, locks only when necessary
    // - Better performance for most operations

    public static void demonstrateConcurrency() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        // Concurrent reads and writes are safe
        map.put("key1", 1); // May use CAS or lock internally

        // Atomic operations
        map.compute("key1", (k, v) -> (v == null) ? 1 : v + 1); // Atomic increment

        // Bulk operations
        map.forEach(1, (k, v) -> System.out.println(k + "=" + v)); // Parallel iteration
    }
}

// Why it works:
// - Lock striping reduces contention compared to single global lock
// - CAS operations are lock-free for uncontended updates
// - Size operations are approximate but fast (not requiring full locks)
```

### BlockingQueue
Thread-safe queues that block when full/empty.

```java
public class BlockingQueueExample {
    private final BlockingQueue<Task> queue = new LinkedBlockingQueue<>(100);
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public void producer() {
        executor.submit(() -> {
            while (true) {
                Task task = createTask();
                try {
                    queue.put(task); // Blocks if queue is full
                    System.out.println("Produced: " + task);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    public void consumer() {
        executor.submit(() -> {
            while (true) {
                try {
                    Task task = queue.take(); // Blocks if queue is empty
                    processTask(task);
                    System.out.println("Consumed: " + task);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    // Different queue implementations:
    // - ArrayBlockingQueue: Bounded, uses single lock
    // - LinkedBlockingQueue: Optionally bounded, uses two locks (put/take)
    // - PriorityBlockingQueue: Unbounded, priority-ordered
    // - SynchronousQueue: Zero capacity, direct handoff
}
```

## Advanced Synchronization

### CountDownLatch
Allows threads to wait until a set of operations complete.

```java
public class CountDownLatchExample {
    public static void main(String[] args) throws InterruptedException {
        int numWorkers = 3;
        CountDownLatch latch = new CountDownLatch(numWorkers);

        // Start worker threads
        for (int i = 0; i < numWorkers; i++) {
            new Thread(() -> {
                try {
                    doWork();
                    latch.countDown(); // Decrement counter
                } catch (Exception e) {
                    // Handle exception - may need to count down anyway
                    latch.countDown();
                }
            }).start();
        }

        // Main thread waits for all workers to complete
        latch.await(); // Blocks until count reaches zero
        System.out.println("All workers completed");
    }
}

// Use cases:
// - Starting multiple services before accepting requests
// - Waiting for initialization to complete
// - Coordinating phases of computation
```

### CyclicBarrier
Allows threads to wait at a barrier until all reach it, then proceed together.

```java
public class CyclicBarrierExample {
    private static final int NUM_THREADS = 3;
    private static final CyclicBarrier barrier = new CyclicBarrier(NUM_THREADS, () -> {
        System.out.println("All threads reached barrier, proceeding to next phase");
    });

    public static void main(String[] args) {
        for (int i = 0; i < NUM_THREADS; i++) {
            new Thread(new Worker(i)).start();
        }
    }

    static class Worker implements Runnable {
        private final int id;

        Worker(int id) { this.id = id; }

        @Override
        public void run() {
            try {
                System.out.println("Worker " + id + " doing phase 1");
                Thread.sleep((long) (Math.random() * 1000));
                barrier.await(); // Wait for all to reach phase 1

                System.out.println("Worker " + id + " doing phase 2");
                Thread.sleep((long) (Math.random() * 1000));
                barrier.await(); // Wait for all to reach phase 2

                System.out.println("Worker " + id + " completed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

// Key differences from CountDownLatch:
// - CyclicBarrier can be reused (cyclic)
// - All threads must reach barrier before any can proceed
// - Can execute a barrier action when all arrive
```

### Semaphore
Controls access to a shared resource with a specified number of permits.

```java
public class SemaphoreExample {
    private static final Semaphore semaphore = new Semaphore(3); // 3 permits

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(new ResourceUser(i)).start();
        }
    }

    static class ResourceUser implements Runnable {
        private final int id;

        ResourceUser(int id) { this.id = id; }

        @Override
        public void run() {
            try {
                semaphore.acquire(); // Acquire permit, blocks if none available
                System.out.println("Thread " + id + " acquired resource");

                // Use the shared resource
                useResource();

                System.out.println("Thread " + id + " releasing resource");
                semaphore.release(); // Release permit
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Fair vs unfair semaphores:
    // - Fair: FIFO ordering of waiting threads
    // - Unfair: No ordering guarantees, potentially better performance
}
```

### CAS (Compare-And-Swap) Algorithms
Atomic operations using hardware-level compare-and-swap.

```java
public class CASExample {
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        // Basic CAS operation
        int current = counter.get();
        if (counter.compareAndSet(current, current + 1)) {
            System.out.println("CAS succeeded");
        }

        // Custom CAS-based implementation
        class CustomSpinLock {
            private final AtomicBoolean locked = new AtomicBoolean(false);

            public void lock() {
                while (!locked.compareAndSet(false, true)) {
                    // Spin until we acquire the lock
                    Thread.yield(); // Be nice to other threads
                }
            }

            public void unlock() {
                locked.set(false);
            }
        }

        // ABA problem demonstration
        AtomicStampedReference<String> stampedRef = new AtomicStampedReference<>("value", 0);
        // StampedReference prevents ABA by tracking version numbers
    }
}

// Why CAS works:
// - Hardware-supported atomic operation
// - Lock-free for uncontended cases
// - Can lead to starvation in high contention
// - ABA problem: Value changes A->B->A without detection
```

## Modern Java: Virtual Threads (Project Loom)

Virtual threads (fibers) are lightweight threads managed by the JVM, not the OS.

```java
public class VirtualThreadsExample {
    public static void main(String[] args) throws InterruptedException {
        // Traditional thread (OS thread)
        Thread.ofPlatform().start(() -> {
            System.out.println("Platform thread: " + Thread.currentThread());
        });

        // Virtual thread (JVM-managed)
        Thread.ofVirtual().start(() -> {
            System.out.println("Virtual thread: " + Thread.currentThread());
        });

        // Executor with virtual threads
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 1000; i++) {
                executor.submit(() -> {
                    // Each task runs on its own virtual thread
                    // Much cheaper than platform threads
                    return doWork();
                });
            }
        }
    }
}

// Key benefits:
// - Millions of virtual threads possible (vs. thousands of platform threads)
// - Blocking operations don't block OS threads
// - Simplified async programming
// - Backward compatible with existing APIs

// Trade-offs:
// - Not yet production-ready (as of Java 19, preview feature)
// - Debugging can be more complex
// - May require rethinking thread-local variables
```

## Common SDE-2 Interview Questions

### Scenario-Based Questions

1. **Deadlock Prevention**: Design a system that transfers money between accounts without deadlocks. How would you order lock acquisition?

2. **Producer-Consumer at Scale**: Implement a high-throughput producer-consumer system. How do you size thread pools and queues? What blocking queue implementation would you choose and why?

3. **Concurrent Cache**: Design a thread-safe LRU cache with O(1) operations. How do you handle concurrent reads/writes without blocking all operations?

4. **Rate Limiter**: Implement a distributed rate limiter using concurrent data structures. How do you handle race conditions across multiple threads?

5. **Thread-Safe Singleton**: Write a thread-safe singleton that performs well under high concurrency. Compare double-checked locking vs. enum vs. static holder patterns.

6. **Concurrent Counter**: Implement a high-performance counter that can be incremented by multiple threads. Compare AtomicLong vs. LongAdder vs. synchronized approaches.

7. **Task Scheduling**: Design a task scheduler that executes tasks at specific times with thread pool management. How do you handle task cancellation and thread pool sizing?

8. **Concurrent Data Processing**: Process a large dataset concurrently using ForkJoinPool. How do you choose the right granularity for task splitting?

9. **Async API Design**: Design an async API using CompletableFuture that handles timeouts, retries, and circuit breakers. How do you compose multiple async operations?

10. **Memory Model Edge Cases**: Explain scenarios where volatile is insufficient and you need synchronized or Atomic classes. Provide code examples.

### Design Discussion Questions

- How would you design a concurrent web crawler that respects robots.txt and rate limits?
- Design a thread-safe observer pattern implementation.
- How do you handle thread interruption gracefully in long-running operations?
- Design a concurrent connection pool for database connections.

### Performance Tuning Questions

- How do you diagnose and fix thread contention issues?
- When should you use ThreadLocal vs. passing parameters?
- How do you choose between different concurrent collection implementations?
- What are the performance implications of different locking strategies?

Remember: In interviews, focus on trade-offs, scalability considerations, and real-world constraints. Be prepared to discuss performance benchmarks, memory usage, and failure scenarios.