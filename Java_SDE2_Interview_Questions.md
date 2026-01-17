# Java SDE2 Interview Questions & Answers

*Comprehensive Guide for Senior Software Engineer (SDE2) Interviews*

## Table of Contents
1. [Core Java Fundamentals](#core-java-fundamentals)
2. [Object-Oriented Programming](#object-oriented-programming)
3. [SOLID Principles](#solid-principles)
4. [Collections Framework](#collections-framework)
5. [Multithreading & Concurrency](#multithreading--concurrency)
6. [JVM Internals](#jvm-internals)
7. [Java 8+ Features](#java-8-features)
8. [Design Patterns](#design-patterns)
9. [Spring Framework](#spring-framework)
10. [Database & ORM](#database--orm)
11. [System Design](#system-design)
12. [Coding Problems](#coding-problems)
13. [Behavioral Questions](#behavioral-questions)

---

## Core Java Fundamentals

### Q1: Explain the difference between `==` and `.equals()` in Java.
**Answer:**
- `==` compares object references (memory addresses)
- `.equals()` compares object content/values
- For primitives, `==` compares values
- For objects, always override `.equals()` for content comparison

```java
String s1 = new String("hello");
String s2 = new String("hello");
System.out.println(s1 == s2); // false (different objects)
System.out.println(s1.equals(s2)); // true (same content)
```

### Q2: What is the difference between `String`, `StringBuilder`, and `StringBuffer`?
**Answer:**
- **String**: Immutable, thread-safe, created in String pool
- **StringBuilder**: Mutable, not thread-safe, better performance for single-threaded operations
- **StringBuffer**: Mutable, thread-safe (synchronized), slower than StringBuilder

**When to use:**
- String: For constants or few modifications
- StringBuilder: For string manipulation in single-threaded environment
- StringBuffer: For string manipulation in multi-threaded environment

### Q3: Explain method overloading vs method overriding.
**Answer:**
**Method Overloading:**
- Same method name, different parameters
- Occurs within same class
- Compile-time polymorphism
- Return type can be different

**Method Overriding:**
- Same method signature in subclass
- Runtime polymorphism
- Must have same return type (or covariant)
- Access modifier can be more permissive

### Q4: What are the access modifiers in Java?
**Answer:**
- **private**: Accessible only within the same class
- **default (package-private)**: Accessible within the same package
- **protected**: Accessible within package and subclasses
- **public**: Accessible from anywhere

### Q5: Explain the `final`, `finally`, and `finalize()` keywords.
**Answer:**
- **final**: Variable (constant), method (cannot override), class (cannot inherit)
- **finally**: Block that always executes after try-catch, used for cleanup
- **finalize()**: Method called by garbage collector before object destruction (deprecated in Java 9+)

---

## Object-Oriented Programming

### Q1: What are the four pillars of OOP?
**Answer:**
1. **Encapsulation**: Bundling data and methods, hiding internal state
2. **Inheritance**: Creating new classes from existing ones
3. **Polymorphism**: Same interface, different implementations
4. **Abstraction**: Hiding complex implementation details

### Q2: Explain encapsulation with an example.
**Answer:**
Encapsulation is about data hiding and bundling data with methods that operate on it.

```java
public class BankAccount {
    private double balance; // Encapsulated data

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public double getBalance() {
        return balance;
    }
}
```

### Q3: What is polymorphism? Explain with examples.
**Answer:**
Polymorphism allows objects to be treated as instances of their parent class.

**Types:**
- **Compile-time (Static)**: Method overloading
- **Runtime (Dynamic)**: Method overriding

```java
// Runtime Polymorphism
class Animal {
    void makeSound() {
        System.out.println("Animal sound");
    }
}

class Dog extends Animal {
    void makeSound() {
        System.out.println("Woof!");
    }
}

// Usage
Animal animal = new Dog();
animal.makeSound(); // Prints "Woof!" (runtime binding)
```

### Q4: What is abstraction? How is it different from encapsulation?
**Answer:**
**Abstraction**: Hiding implementation details and showing only essential features.

**Encapsulation**: Hiding data using access modifiers.

**Key Difference**: Abstraction is about "what" a class does, encapsulation is about "how" it does it.

**Example:**
```java
// Abstract class showing abstraction
abstract class Vehicle {
    abstract void start(); // Abstract method - what it does

    void stop() { // Concrete method - implementation detail
        System.out.println("Vehicle stopped");
    }
}
```

---

## SOLID Principles

### Q1: Explain all five SOLID principles with examples.
**Answer:** (Reference the SOLID_Study_Guide_SDE2.md for detailed examples)

**S - Single Responsibility Principle**
- A class should have only one reason to change
- Example: Separate classes for BankService, NotificationService, etc.

**O - Open-Closed Principle**
- Open for extension, closed for modification
- Example: NotificationService interface with multiple implementations

**L - Liskov Substitution Principle**
- Subtypes should be substitutable for their base types
- Example: Proper interface segregation instead of inheritance violation

**I - Interface Segregation Principle**
- Clients should not be forced to depend on interfaces they don't use
- Example: UPIPayments and CashBackManager separate interfaces

**D - Dependency Inversion Principle**
- Depend on abstractions, not concretions
- Example: ShoppingMall depends on BankCard interface

### Q2: When would you NOT apply SOLID principles?
**Answer:**
- Simple scripts or prototypes
- Performance-critical code where abstraction adds overhead
- Legacy code maintenance
- Small teams or tight deadlines
- When the complexity outweighs the benefits

---

## Collections Framework

### Q1: Explain the Collection hierarchy in Java.
**Answer:**
```
Collection (Interface)
├── List (Interface)
│   ├── ArrayList
│   ├── LinkedList
│   └── Vector
├── Set (Interface)
│   ├── HashSet
│   ├── LinkedHashSet
│   └── TreeSet
├── Queue (Interface)
│   ├── PriorityQueue
│   └── LinkedList
└── Map (Interface) - Not extending Collection
    ├── HashMap
    ├── LinkedHashMap
    ├── TreeMap
    └── Hashtable
```

### Q2: Difference between ArrayList and LinkedList?
**Answer:**
- **ArrayList**: Backed by dynamic array, fast random access (O(1)), slow insertions/deletions (O(n))
- **LinkedList**: Backed by doubly linked list, slow random access (O(n)), fast insertions/deletions (O(1))

**When to use:**
- ArrayList: Frequent reads, infrequent modifications
- LinkedList: Frequent insertions/deletions, sequential access

### Q3: How does HashMap work internally?
**Answer:**
- Uses array of buckets (default 16)
- Key's hashCode() determines bucket
- If collision, stores as linked list (Java 7) or balanced tree (Java 8+ when > 8 elements)
- Load factor (0.75) triggers rehashing

### Q4: What is the difference between HashMap and Hashtable?
**Answer:**
- **HashMap**: Not synchronized, allows null keys/values, better performance
- **Hashtable**: Synchronized, doesn't allow null keys/values, legacy class
- **ConcurrentHashMap**: Better than Hashtable for concurrent operations

### Q5: Explain fail-fast vs fail-safe iterators.
**Answer:**
- **Fail-fast**: Throws ConcurrentModificationException if collection modified during iteration (ArrayList, HashMap)
- **Fail-safe**: Creates copy, doesn't throw exception (ConcurrentHashMap, CopyOnWriteArrayList)

---

## Multithreading & Concurrency

### Q1: What is the difference between `Thread` and `Runnable`?
**Answer:**
- **Thread**: Class that represents a thread of execution
- **Runnable**: Interface with `run()` method
- **Best Practice**: Implement Runnable for better composition

```java
// Preferred approach
class MyTask implements Runnable {
    public void run() {
        // task logic
    }
}
Thread thread = new Thread(new MyTask());
```

### Q2: Explain synchronization in Java.
**Answer:**
Synchronization ensures only one thread can access a resource at a time.

**Types:**
- **Method level**: `synchronized void method()`
- **Block level**: `synchronized(this) { }`
- **Static method**: Locks on class object

**Problems:**
- **Race condition**: Multiple threads accessing shared data
- **Deadlock**: Two threads waiting for each other
- **Starvation**: Thread never gets CPU time

### Q3: What is the volatile keyword?
**Answer:**
- Ensures visibility of changes across threads
- Prevents compiler optimizations that might cache variables
- Doesn't provide atomicity for compound operations

```java
private volatile boolean flag = false; // All threads see updates immediately
```

### Q4: Explain the producer-consumer problem.
**Answer:**
Classic synchronization problem using wait() and notify().

```java
class BlockingQueue<T> {
    private Queue<T> queue = new LinkedList<>();
    private int capacity;

    public synchronized void put(T item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }
        queue.add(item);
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        T item = queue.remove();
        notifyAll();
        return item;
    }
}
```

### Q5: What are atomic classes in Java?
**Answer:**
Classes in `java.util.concurrent.atomic` package for lock-free thread-safe operations.

```java
AtomicInteger counter = new AtomicInteger(0);
counter.incrementAndGet(); // Thread-safe increment
```

---

## JVM Internals

### Q1: Explain JVM memory areas.
**Answer:**
- **Method Area**: Class metadata, static variables, method information
- **Heap**: Objects and instance variables
- **Stack**: Method calls, local variables, reference variables
- **PC Register**: Current instruction pointer
- **Native Method Stack**: Native method calls

### Q2: What is garbage collection in Java?
**Answer:**
Automatic memory management that reclaims heap memory occupied by unreachable objects.

**GC Types:**
- **Minor GC**: Young generation (Eden, Survivor spaces)
- **Major GC**: Old generation
- **Full GC**: Entire heap

**Algorithms:**
- **Serial GC**: Single thread
- **Parallel GC**: Multiple threads for minor GC
- **CMS (Concurrent Mark Sweep)**: Concurrent with application threads
- **G1**: Generational, divides heap into regions

### Q3: Explain class loading in Java.
**Answer:**
Process of loading .class files into JVM memory.

**Class Loaders:**
1. **Bootstrap ClassLoader**: Loads core Java classes (rt.jar)
2. **Extension ClassLoader**: Loads extension classes (jre/lib/ext)
3. **Application ClassLoader**: Loads application classes

**Process:**
1. **Loading**: Find and load binary data
2. **Linking**: Verification, Preparation, Resolution
3. **Initialization**: Execute static blocks, initialize static variables

---

## Java 8+ Features

### Q1: What are functional interfaces?
**Answer:**
Interfaces with exactly one abstract method. Can have default/static methods.

**Common Examples:**
- `Runnable`: `run()`
- `Callable`: `call()`
- `Comparator`: `compare()`
- `Predicate`: `test()`
- `Function`: `apply()`
- `Consumer`: `accept()`
- `Supplier`: `get()`

### Q2: Explain Lambda expressions.
**Answer:**
Anonymous functions that can be passed as arguments or stored in variables.

```java
// Before Java 8
button.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        System.out.println("Button clicked");
    }
});

// With Lambda
button.addActionListener(e -> System.out.println("Button clicked"));
```

### Q3: What are Streams in Java 8?
**Answer:**
API for processing collections declaratively.

**Operations:**
- **Intermediate**: `filter()`, `map()`, `sorted()` - return Stream
- **Terminal**: `collect()`, `forEach()`, `count()` - return result

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
List<String> filtered = names.stream()
    .filter(name -> name.length() > 3)
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

### Q4: Explain Optional class.
**Answer:**
Container that may or may not contain a value. Helps avoid NullPointerException.

```java
Optional<String> optional = Optional.ofNullable(getName());

String result = optional
    .filter(name -> name.length() > 0)
    .map(String::toUpperCase)
    .orElse("DEFAULT");
```

### Q5: What are method references?
**Answer:**
Shorthand for lambda expressions that call existing methods.

**Types:**
- **Static method**: `String::valueOf`
- **Instance method**: `String::toUpperCase`
- **Constructor**: `ArrayList::new`

---

## Design Patterns

### Q1: Explain Singleton pattern with thread-safe implementation.
**Answer:**
Ensures only one instance of a class exists.

**Thread-Safe Implementations:**
```java
// Double-checked locking
public class Singleton {
    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

### Q2: Explain Factory pattern.
**Answer:**
Creates objects without specifying exact classes.

```java
interface Notification {
    void send(String message);
}

class EmailNotification implements Notification {
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

class NotificationFactory {
    public static Notification createNotification(String type) {
        switch (type) {
            case "email": return new EmailNotification();
            default: throw new IllegalArgumentException();
        }
    }
}
```

### Q3: Explain Observer pattern.
**Answer:**
One-to-many dependency where changes in one object notify others.

```java
interface Observer {
    void update(String message);
}

class Subject {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void notifyObservers(String message) {
        observers.forEach(obs -> obs.update(message));
    }
}
```

---

## Spring Framework

### Q1: What is dependency injection?
**Answer:**
Process where Spring provides dependencies to a class instead of the class creating them.

**Types:**
- **Constructor Injection**: Dependencies provided through constructor
- **Setter Injection**: Dependencies provided through setter methods
- **Field Injection**: Dependencies injected directly into fields (@Autowired)

### Q2: Explain Spring Bean lifecycle.
**Answer:**
1. **Instantiation**: Bean created
2. **Populate Properties**: Dependencies injected
3. **BeanNameAware.setBeanName()**: Bean name set
4. **BeanFactoryAware.setBeanFactory()**: BeanFactory reference set
5. **Pre-initialization**: @PostConstruct methods called
6. **InitializingBean.afterPropertiesSet()**: Called after properties set
7. **Custom init-method**: User-defined initialization
8. **Bean ready for use**
9. **Pre-destruction**: @PreDestroy methods called
10. **DisposableBean.destroy()**: Cleanup
11. **Custom destroy-method**: User-defined cleanup

### Q3: What is AOP in Spring?
**Answer:**
Aspect-Oriented Programming allows separation of cross-cutting concerns.

**Key Concepts:**
- **Aspect**: Module with cross-cutting logic
- **Join Point**: Point in program execution
- **Advice**: Action taken at join point
- **Pointcut**: Expression matching join points
- **Weaving**: Linking aspects with objects

**Common Use Cases:**
- Logging
- Transaction management
- Security
- Caching

---

## Database & ORM

### Q1: Explain ACID properties.
**Answer:**
- **Atomicity**: All or nothing
- **Consistency**: Database remains in consistent state
- **Isolation**: Concurrent transactions don't interfere
- **Durability**: Committed changes persist

### Q2: What are database indexes? When to use them?
**Answer:**
Data structures that improve query performance by allowing fast lookups.

**When to use:**
- Columns frequently used in WHERE clauses
- Foreign keys
- Columns used for sorting/grouping

**When NOT to use:**
- Small tables
- Columns with low selectivity
- Frequently updated columns (slows INSERT/UPDATE)

### Q3: Explain JPA/Hibernate.
**Answer:**
- **JPA**: Java Persistence API - specification for ORM
- **Hibernate**: Implementation of JPA

**Key Concepts:**
- **Entity**: POJO mapped to database table
- **EntityManager**: Interface for CRUD operations
- **JPQL**: Java Persistence Query Language
- **Caching**: First-level (session) and second-level (application) cache

---

## System Design

### Q1: Design a URL shortening service (like bit.ly).
**Answer:**
**Requirements:**
- Shorten long URLs to short ones
- Redirect short URLs to original
- Handle high traffic
- Analytics (optional)

**Components:**
- **Web Server**: Handle requests
- **Database**: Store URL mappings
- **Cache**: Redis for fast lookups
- **Load Balancer**: Distribute traffic

**Algorithm:**
- Generate unique hash (Base62 encoding)
- Store mapping: short_url -> long_url

**Scalability:**
- Database sharding
- Multiple cache instances
- CDN for static content

### Q2: Design a notification system.
**Answer:**
**Components:**
- **Producer**: Generate notifications
- **Queue**: Message queue (Kafka/RabbitMQ)
- **Workers**: Process notifications
- **Templates**: Notification templates
- **Channels**: Email, SMS, Push notifications

**Patterns:**
- Observer pattern for subscribers
- Strategy pattern for different channels
- Factory pattern for notification creation

---

## Coding Problems

### Q1: Find the first non-repeating character in a string.
```java
public char firstNonRepeatingChar(String str) {
    Map<Character, Integer> count = new LinkedHashMap<>();
    for (char c : str.toCharArray()) {
        count.put(c, count.getOrDefault(c, 0) + 1);
    }
    for (Map.Entry<Character, Integer> entry : count.entrySet()) {
        if (entry.getValue() == 1) {
            return entry.getKey();
        }
    }
    return '\0';
}
```

### Q2: Implement LRU Cache.
```java
class LRUCache {
    private LinkedHashMap<Integer, Integer> cache;
    private int capacity;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<Integer, Integer>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > capacity;
            }
        };
    }

    public int get(int key) {
        return cache.getOrDefault(key, -1);
    }

    public void put(int key, int value) {
        cache.put(key, value);
    }
}
```

### Q3: Check if a binary tree is balanced.
```java
class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int x) { val = x; }
}

public boolean isBalanced(TreeNode root) {
    return checkHeight(root) != -1;
}

private int checkHeight(TreeNode node) {
    if (node == null) return 0;

    int leftHeight = checkHeight(node.left);
    if (leftHeight == -1) return -1;

    int rightHeight = checkHeight(node.right);
    if (rightHeight == -1) return -1;

    if (Math.abs(leftHeight - rightHeight) > 1) return -1;

    return Math.max(leftHeight, rightHeight) + 1;
}
```

---

## Behavioral Questions

### Q1: Tell me about a challenging technical problem you solved.
**Answer Structure:**
- **Context**: Set the scene
- **Problem**: What was the issue?
- **Approach**: How did you tackle it?
- **Solution**: What did you implement?
- **Results**: What was the impact?

### Q2: How do you handle conflicting priorities?
**Answer:**
- Communicate with stakeholders
- Prioritize based on business impact
- Break down tasks and set realistic deadlines
- Escalate when necessary
- Focus on high-impact, low-effort tasks first

### Q3: Describe a time when you had to learn a new technology quickly.
**Answer:**
- **Preparation**: Research and planning
- **Learning**: Hands-on practice, documentation
- **Application**: Build small projects
- **Collaboration**: Pair programming, code reviews
- **Continuous Learning**: Stay updated

### Q4: How do you approach code reviews?
**Answer:**
- **Functionality**: Does it work correctly?
- **Design**: Follows SOLID principles?
- **Performance**: Any bottlenecks?
- **Security**: Input validation, SQL injection, etc.
- **Readability**: Clear variable names, comments?
- **Testing**: Adequate test coverage?

### Q5: How do you handle production issues?
**Answer:**
- **Assessment**: Gather information, check logs
- **Impact Analysis**: How many users affected?
- **Communication**: Notify stakeholders
- **Root Cause**: Identify the problem
- **Fix**: Implement solution
- **Prevention**: Add monitoring, improve tests
- **Post-mortem**: Document lessons learned

---

*Remember: In interviews, explain your thought process, ask clarifying questions, and demonstrate problem-solving skills. Practice coding problems on LeetCode/HackerRank and review system design concepts regularly.*