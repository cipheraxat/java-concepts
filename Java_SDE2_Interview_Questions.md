# Java SDE2 Interview Questions & Answers

*Comprehensive Guide for Senior Software Engineer (SDE2) Interviews*

## Table of Contents
1. [Core Java Fundamentals](#core-java-fundamentals)
2. [Object-Oriented Programming](#object-oriented-programming)
3. [Exception Handling](#exception-handling)
4. [SOLID Principles](#solid-principles)
5. [Collections Framework](#collections-framework)
6. [Multithreading & Concurrency](#multithreading--concurrency)
7. [JVM Internals](#jvm-internals)
8. [Java 8+ Features](#java-8-features)
9. [Design Patterns](#design-patterns)
10. [Microservices](#microservices)
11. [Spring Framework](#spring-framework)
12. [Database & ORM](#database--orm)
13. [System Design](#system-design)
14. [Coding Problems](#coding-problems)
15. [Behavioral Questions](#behavioral-questions)

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

### Q6: What is the difference between `static` and `instance` variables/methods?
**Answer:**
**Static Members:**
- Belong to class, not instances
- Shared among all instances
- Can be accessed without creating object
- Memory allocated once at class loading

**Instance Members:**
- Belong to specific object instance
- Each object has its own copy
- Require object creation to access

```java
class Example {
    static int staticVar = 10;     // Shared by all instances
    int instanceVar = 20;          // Each instance has its own

    static void staticMethod() {}  // Can access only static members
    void instanceMethod() {}       // Can access both static and instance members
}
```

### Q7: Explain the `this` and `super` keywords.
**Answer:**
- **this**: Refers to current instance of class
- **super**: Refers to parent class instance

**Common Uses:**
- `this()`: Call another constructor in same class
- `super()`: Call parent class constructor
- `this.field`: Access instance field when shadowed by parameter
- `super.method()`: Call overridden method in parent class

### Q8: What are wrapper classes? Explain autoboxing and unboxing.
**Answer:**
Wrapper classes provide object representation for primitive types.

**Primitive → Wrapper:**
- int → Integer
- char → Character
- boolean → Boolean
- etc.

**Autoboxing**: Automatic conversion from primitive to wrapper
**Unboxing**: Automatic conversion from wrapper to primitive

```java
// Autoboxing
Integer num = 10;  // int → Integer

// Unboxing
int value = num;   // Integer → int

// Before Java 5, manual conversion required
Integer manual = Integer.valueOf(10);
int manualValue = manual.intValue();
```

### Q9: Explain the `instanceof` operator and `isInstance()` method.
**Answer:**
Both check if an object is an instance of a class/interface.

**instanceof operator:**
- Compile-time check
- Returns boolean
- Can cause ClassCastException if used incorrectly

**isInstance() method:**
- Runtime check using reflection
- More flexible, can check against dynamically loaded classes
- Safer than instanceof

```java
Object obj = "Hello";

// Using instanceof
if (obj instanceof String) {
    String str = (String) obj;
}

// Using isInstance()
Class<?> stringClass = Class.forName("java.lang.String");
if (stringClass.isInstance(obj)) {
    String str = stringClass.cast(obj);
}
```

### Q10: What are generics in Java? Explain type erasure.
**Answer:**
Generics enable types to be parameters when defining classes, interfaces, and methods.

**Benefits:**
- Type safety at compile time
- No casting required
- Reusable code

**Type Erasure:**
- Generic information removed at runtime
- Replaced with Object or bounded type
- Ensures backward compatibility

```java
// Generic class
class Box<T> {
    private T item;

    public void setItem(T item) { this.item = item; }
    public T getItem() { return item; }
}

// Usage
Box<String> stringBox = new Box<>();
stringBox.setItem("Hello");  // No casting needed
String item = stringBox.getItem();
```

---

## Exception Handling

### Q1: Explain the exception hierarchy in Java.
**Answer:**
```
Throwable (superclass)
├── Exception (checked exceptions)
│   ├── IOException
│   ├── SQLException
│   └── RuntimeException (unchecked exceptions)
│       ├── NullPointerException
│       ├── IllegalArgumentException
│       ├── IndexOutOfBoundsException
│       └── ClassCastException
└── Error (unchecked, serious problems)
    ├── OutOfMemoryError
    ├── StackOverflowError
    └── VirtualMachineError
```

**Java Exception Hierarchy Examples:**
```java
public class ExceptionHierarchyDemo {
    public static void main(String[] args) {
        try {
            demonstrateExceptions();
        } catch (Exception e) {
            System.out.println("Caught Exception: " + e.getClass().getSimpleName());
            System.out.println("Message: " + e.getMessage());
        }
    }

    public static void demonstrateExceptions() throws Exception {
        // 1. Checked Exception - must be handled or declared
        try {
            throwCheckedException();
        } catch (IOException e) {
            System.out.println("Caught checked exception: " + e.getMessage());
        }

        // 2. Unchecked Exception - RuntimeException
        // throwRuntimeException(); // Would cause program to terminate if not caught

        // 3. Error - serious problems
        // throwError(); // Would cause program to terminate

        // 4. Custom exception
        throw new CustomBusinessException("Business rule violated");
    }

    // Checked exception
    public static void throwCheckedException() throws IOException {
        throw new IOException("File not found");
    }

    // Unchecked exception
    public static void throwRuntimeException() {
        throw new IllegalArgumentException("Invalid argument provided");
    }

    // Error
    public static void throwError() {
        throw new OutOfMemoryError("Not enough memory");
    }
}

// Custom checked exception
class CustomBusinessException extends Exception {
    public CustomBusinessException(String message) {
        super(message);
    }

    public CustomBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Custom unchecked exception
class CustomValidationException extends RuntimeException {
    public CustomValidationException(String message) {
        super(message);
    }

    public CustomValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Exception chaining
class ExceptionChainingDemo {
    public static void main(String[] args) {
        try {
            method1();
        } catch (CustomBusinessException e) {
            System.out.println("Root cause: " + e.getCause().getMessage());
            System.out.println("Business message: " + e.getMessage());
        }
    }

    public static void method1() throws CustomBusinessException {
        try {
            method2();
        } catch (IOException e) {
            // Chain exceptions
            throw new CustomBusinessException("Failed to process file", e);
        }
    }

    public static void method2() throws IOException {
        throw new IOException("File corrupted");
    }
}
```

### Q2: Difference between checked and unchecked exceptions?
**Answer:**
**Checked Exceptions:**
- Checked at compile time
- Must be handled with try-catch or declared in method signature
- Examples: IOException, SQLException

**Unchecked Exceptions:**
- Not checked at compile time
- Usually programming errors
- Examples: NullPointerException, ArrayIndexOutOfBoundsException

### Q3: Explain try-with-resources statement.
**Answer:**
Automatically closes resources that implement AutoCloseable interface.

**Java Try-With-Resources Examples:**
```java
import java.io.*;
import java.sql.*;
import java.util.zip.*;

public class TryWithResourcesDemo {

    // Basic try-with-resources
    public static void basicTryWithResources() {
        try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        // reader is automatically closed here
    }

    // Multiple resources
    public static void multipleResources() {
        try (FileInputStream fis = new FileInputStream("input.txt");
             FileOutputStream fos = new FileOutputStream("output.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line.toUpperCase());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error processing files: " + e.getMessage());
        }
        // All resources closed in reverse order: writer, reader, fos, fis
    }

    // Custom AutoCloseable resource
    public static void customResource() {
        try (CustomResource resource = new CustomResource("MyResource")) {
            resource.doWork();
            // Simulate exception
            if (Math.random() > 0.5) {
                throw new RuntimeException("Random exception");
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
        // resource.close() is called automatically
    }

    // Suppressed exceptions
    public static void suppressedExceptions() {
        try (CustomResource resource = new CustomResource("TestResource")) {
            resource.doWork();
            throw new RuntimeException("Primary exception");
        } catch (RuntimeException e) {
            System.out.println("Primary exception: " + e.getMessage());
            // Check for suppressed exceptions
            for (Throwable suppressed : e.getSuppressed()) {
                System.out.println("Suppressed exception: " + suppressed.getMessage());
            }
        }
    }

    // Database example
    public static void databaseExample() {
        String url = "jdbc:h2:mem:testdb";
        String sql = "CREATE TABLE users (id INT, name VARCHAR(50))";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Table created successfully");

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        // Connection and Statement closed automatically
    }

    public static void main(String[] args) {
        basicTryWithResources();
        multipleResources();
        customResource();
        suppressedExceptions();
        databaseExample();
    }
}

// Custom AutoCloseable implementation
class CustomResource implements AutoCloseable {
    private String name;
    private boolean closed = false;

    public CustomResource(String name) {
        this.name = name;
        System.out.println("Resource " + name + " opened");
    }

    public void doWork() {
        if (closed) {
            throw new IllegalStateException("Resource is closed");
        }
        System.out.println("Working with resource " + name);
    }

    @Override
    public void close() throws Exception {
        if (!closed) {
            closed = true;
            System.out.println("Resource " + name + " closed");
            // Simulate close exception
            if (name.equals("TestResource")) {
                throw new Exception("Close exception");
            }
        }
    }
}
```

### Q4: What is the difference between `throw` and `throws`?
**Answer:**
- **throw**: Used to explicitly throw an exception
- **throws**: Declares that a method can throw exceptions

**Java throw vs throws Examples:**
```java
import java.io.IOException;
import java.sql.SQLException;

public class ThrowThrowsDemo {

    // Method using 'throws' to declare checked exceptions
    public static void readFile() throws IOException {
        // This method can throw IOException
        throw new IOException("File not found");
    }

    // Method using 'throws' with multiple exceptions
    public static void processData() throws IOException, SQLException {
        if (Math.random() > 0.5) {
            throw new IOException("IO Error");
        } else {
            throw new SQLException("Database Error");
        }
    }

    // Method using 'throw' to throw runtime exception
    public static void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
        if (age > 150) {
            throw new IllegalArgumentException("Age cannot be greater than 150");
        }
    }

    // Method that throws custom exception
    public static void withdrawMoney(double amount) throws InsufficientFundsException {
        double balance = 1000.0;
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds. Available: $" + balance);
        }
        System.out.println("Withdrawal successful: $" + amount);
    }

    // Method that catches and re-throws with different exception
    public static void processFile(String filename) throws FileProcessingException {
        try {
            readFile(); // throws IOException
        } catch (IOException e) {
            // Wrap the original exception
            throw new FileProcessingException("Failed to process file: " + filename, e);
        }
    }

    // Method that throws exception conditionally
    public static void divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        System.out.println("Result: " + (a / b));
    }

    public static void main(String[] args) {
        try {
            // Example 1: throws with checked exception
            readFile();
        } catch (IOException e) {
            System.out.println("Caught IOException: " + e.getMessage());
        }

        try {
            // Example 2: throw with runtime exception
            validateAge(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught IllegalArgumentException: " + e.getMessage());
        }

        try {
            // Example 3: throws with custom exception
            withdrawMoney(1500);
        } catch (InsufficientFundsException e) {
            System.out.println("Caught InsufficientFundsException: " + e.getMessage());
        }

        try {
            // Example 4: throw in conditional logic
            divide(10, 0);
        } catch (ArithmeticException e) {
            System.out.println("Caught ArithmeticException: " + e.getMessage());
        }
    }
}

// Custom checked exception
class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }

    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Another custom exception for file processing
class FileProcessingException extends Exception {
    public FileProcessingException(String message) {
        super(message);
    }

    public FileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

### Q5: Explain custom exception creation.
**Answer:**
Create custom exceptions by extending Exception or RuntimeException.

**Java Custom Exception Examples:**
```java
import java.util.HashMap;
import java.util.Map;

// Base custom exception with additional fields
abstract class BusinessException extends Exception {
    private String errorCode;
    private Map<String, Object> context;

    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.context = new HashMap<>();
    }

    public BusinessException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.context = new HashMap<>();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public BusinessException addContext(String key, Object value) {
        this.context.put(key, value);
        return this;
    }
}

// Checked custom exception - must be handled or declared
class InsufficientFundsException extends BusinessException {
    private double requiredAmount;
    private double availableAmount;

    public InsufficientFundsException(double required, double available) {
        super("Insufficient funds for transaction",
              "INSUFFICIENT_FUNDS");
        this.requiredAmount = required;
        this.availableAmount = available;
        addContext("requiredAmount", required);
        addContext("availableAmount", available);
    }

    public double getRequiredAmount() {
        return requiredAmount;
    }

    public double getAvailableAmount() {
        return availableAmount;
    }

    public double getShortfall() {
        return requiredAmount - availableAmount;
    }
}

// Unchecked custom exception - doesn't need to be declared
class InvalidAccountException extends RuntimeException {
    private String accountId;
    private String reason;

    public InvalidAccountException(String accountId, String reason) {
        super("Invalid account: " + accountId + " - " + reason);
        this.accountId = accountId;
        this.reason = reason;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getReason() {
        return reason;
    }
}

// Custom exception with validation details
class ValidationException extends RuntimeException {
    private Map<String, String> fieldErrors;

    public ValidationException(String message) {
        super(message);
        this.fieldErrors = new HashMap<>();
    }

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = new HashMap<>(fieldErrors);
    }

    public void addFieldError(String field, String error) {
        this.fieldErrors.put(field, error);
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public boolean hasFieldErrors() {
        return !fieldErrors.isEmpty();
    }
}

// Service class demonstrating exception usage
class BankingService {
    private Map<String, Double> accounts = new HashMap<>();

    public BankingService() {
        accounts.put("ACC001", 1000.0);
        accounts.put("ACC002", 500.0);
    }

    public void transfer(String fromAccount, String toAccount, double amount)
            throws InsufficientFundsException {

        // Validate accounts exist
        if (!accounts.containsKey(fromAccount)) {
            throw new InvalidAccountException(fromAccount, "Account does not exist");
        }
        if (!accounts.containsKey(toAccount)) {
            throw new InvalidAccountException(toAccount, "Account does not exist");
        }

        // Check sufficient funds
        double fromBalance = accounts.get(fromAccount);
        if (fromBalance < amount) {
            throw new InsufficientFundsException(amount, fromBalance);
        }

        // Perform transfer
        accounts.put(fromAccount, fromBalance - amount);
        accounts.put(toAccount, accounts.get(toAccount) + amount);
    }

    public void validateUserData(String email, String phone, int age) {
        Map<String, String> errors = new HashMap<>();

        if (email == null || !email.contains("@")) {
            errors.put("email", "Invalid email format");
        }

        if (phone == null || phone.length() < 10) {
            errors.put("phone", "Phone number must be at least 10 digits");
        }

        if (age < 18 || age > 120) {
            errors.put("age", "Age must be between 18 and 120");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed", errors);
        }
    }

    public double getBalance(String accountId) {
        if (!accounts.containsKey(accountId)) {
            throw new InvalidAccountException(accountId, "Account does not exist");
        }
        return accounts.get(accountId);
    }
}

// Usage example
class CustomExceptionDemo {
    public static void main(String[] args) {
        BankingService service = new BankingService();

        try {
            // Test insufficient funds
            service.transfer("ACC001", "ACC002", 1500.0);
        } catch (InsufficientFundsException e) {
            System.out.println("Transfer failed: " + e.getMessage());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Required: $" + e.getRequiredAmount());
            System.out.println("Available: $" + e.getAvailableAmount());
            System.out.println("Shortfall: $" + e.getShortfall());
        }

        try {
            // Test invalid account
            service.getBalance("INVALID");
        } catch (InvalidAccountException e) {
            System.out.println("Account error: " + e.getMessage());
            System.out.println("Account ID: " + e.getAccountId());
            System.out.println("Reason: " + e.getReason());
        }

        try {
            // Test validation
            service.validateUserData("invalid-email", "123", 15);
        } catch (ValidationException e) {
            System.out.println("Validation failed: " + e.getMessage());
            e.getFieldErrors().forEach((field, error) ->
                System.out.println("  " + field + ": " + error));
        }
    }
}
```

---

## Object-Oriented Programming

### Q1: What are the four pillars of OOP?
**Answer:**
1. **Encapsulation**: Bundling data and methods, hiding internal state
2. **Inheritance**: Creating new classes from existing ones
3. **Polymorphism**: Same interface, different implementations
4. **Abstraction**: Hiding complex implementation details

**Java Implementation Example:**
```java
// 1. Encapsulation
public class BankAccount {
    private double balance; // Encapsulated data

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public double getBalance() {
        return balance; // Controlled access to data
    }
}

// 2. Inheritance
public class Animal {
    protected String name;

    public void eat() {
        System.out.println(name + " is eating");
    }
}

public class Dog extends Animal {
    public Dog(String name) {
        this.name = name;
    }

    public void bark() {
        System.out.println(name + " is barking");
    }
}

// 3. Polymorphism
public interface Shape {
    double calculateArea();
}

public class Circle implements Shape {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}

public class Rectangle implements Shape {
    private double width, height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double calculateArea() {
        return width * height;
    }
}

// Usage of polymorphism
public class AreaCalculator {
    public double calculateTotalArea(Shape[] shapes) {
        double total = 0;
        for (Shape shape : shapes) {
            total += shape.calculateArea(); // Same method, different behavior
        }
        return total;
    }
}

// 4. Abstraction
public abstract class Vehicle {
    protected String brand;

    public Vehicle(String brand) {
        this.brand = brand;
    }

    // Abstract method - implementation hidden
    public abstract void start();

    // Concrete method - some implementation provided
    public void stop() {
        System.out.println(brand + " vehicle stopped");
    }
}

public class Car extends Vehicle {
    public Car(String brand) {
        super(brand);
    }

    @Override
    public void start() {
        System.out.println(brand + " car started with key ignition");
    }
}

public class ElectricCar extends Vehicle {
    public ElectricCar(String brand) {
        super(brand);
    }

    @Override
    public void start() {
        System.out.println(brand + " electric car started with button");
    }
}
```

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
**Answer:**

**S - Single Responsibility Principle**
A class should have only one reason to change.

```java
// Bad example - violates SRP
class Employee {
    public void calculateSalary() { /* salary logic */ }
    public void saveToDatabase() { /* database logic */ }
    public void generateReport() { /* report logic */ }
}

// Good example - follows SRP
class SalaryCalculator {
    public double calculateSalary(Employee emp) { /* salary logic */ }
}

class EmployeeRepository {
    public void saveToDatabase(Employee emp) { /* database logic */ }
}

class ReportGenerator {
    public void generateReport(Employee emp) { /* report logic */ }
}
```

**O - Open-Closed Principle**
Open for extension, closed for modification.

```java
// Bad example - violates OCP
class NotificationService {
    public void sendNotification(String type, String message) {
        if (type.equals("email")) {
            // email logic
        } else if (type.equals("sms")) {
            // sms logic
        }
    }
}

// Good example - follows OCP
interface Notification {
    void send(String message);
}

class EmailNotification implements Notification {
    public void send(String message) { /* email logic */ }
}

class SMSNotification implements Notification {
    public void send(String message) { /* sms logic */ }
}

class NotificationService {
    public void sendNotification(Notification notification, String message) {
        notification.send(message);
    }
}
```

**L - Liskov Substitution Principle**
Subtypes should be substitutable for their base types.

```java
// Bad example - violates LSP
class Rectangle {
    protected int width, height;

    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public int getArea() { return width * height; }
}

class Square extends Rectangle {
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        super.setHeight(width); // Square constraint
    }

    @Override
    public void setHeight(int height) {
        super.setWidth(height);
        super.setHeight(height); // Square constraint
    }
}

// Good example - follows LSP
interface Shape {
    int getArea();
}

class Rectangle implements Shape {
    private int width, height;

    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getArea() { return width * height; }
}

class Square implements Shape {
    private int side;

    public Square(int side) {
        this.side = side;
    }

    public int getArea() { return side * side; }
}
```

**I - Interface Segregation Principle**
Clients should not be forced to depend on interfaces they don't use.

```java
// Bad example - violates ISP
interface Worker {
    void work();
    void eat();
    void sleep();
}

class Robot implements Worker {
    public void work() { /* robot work */ }
    public void eat() { /* not applicable */ }
    public void sleep() { /* not applicable */ }
}

// Good example - follows ISP
interface Workable {
    void work();
}

interface Eatable {
    void eat();
}

interface Sleepable {
    void sleep();
}

class Human implements Workable, Eatable, Sleepable {
    public void work() { /* human work */ }
    public void eat() { /* human eat */ }
    public void sleep() { /* human sleep */ }
}

class Robot implements Workable {
    public void work() { /* robot work */ }
}
```

**D - Dependency Inversion Principle**
Depend on abstractions, not concretions.

```java
// Bad example - violates DIP
class MySQLDatabase {
    public void connect() { /* MySQL connection */ }
    public void query(String sql) { /* MySQL query */ }
}

class UserService {
    private MySQLDatabase database = new MySQLDatabase();

    public void getUser(int id) {
        database.connect();
        // use database
    }
}

// Good example - follows DIP
interface Database {
    void connect();
    void query(String sql);
}

class MySQLDatabase implements Database {
    public void connect() { /* MySQL connection */ }
    public void query(String sql) { /* MySQL query */ }
}

class PostgreSQLDatabase implements Database {
    public void connect() { /* PostgreSQL connection */ }
    public void query(String sql) { /* PostgreSQL query */ }
}

class UserService {
    private Database database;

    public UserService(Database database) {
        this.database = database; // Dependency injection
    }

    public void getUser(int id) {
        database.connect();
        // use database
    }
}
```

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

**Java Collections Usage Examples:**
```java
public class CollectionsDemo {
    public static void main(String[] args) {
        // List implementations
        List<String> arrayList = new ArrayList<>(); // Fast random access
        List<String> linkedList = new LinkedList<>(); // Fast insertions/deletions
        List<String> vector = new Vector<>(); // Thread-safe

        // Set implementations
        Set<String> hashSet = new HashSet<>(); // No duplicates, fast lookup
        Set<String> linkedHashSet = new LinkedHashSet<>(); // Maintains insertion order
        Set<String> treeSet = new TreeSet<>(); // Sorted order

        // Queue implementations
        Queue<String> priorityQueue = new PriorityQueue<>(); // Priority-based ordering

        // Map implementations
        Map<String, Integer> hashMap = new HashMap<>(); // Fast lookup
        Map<String, Integer> linkedHashMap = new LinkedHashMap<>(); // Maintains insertion order
        Map<String, Integer> treeMap = new TreeMap<>(); // Sorted keys
        Map<String, Integer> hashtable = new Hashtable<>(); // Thread-safe

        // Common operations
        demonstrateListOperations();
        demonstrateSetOperations();
        demonstrateMapOperations();
    }

    public static void demonstrateListOperations() {
        List<String> fruits = new ArrayList<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add(1, "Orange"); // Insert at index
        fruits.remove("Banana");
        fruits.set(0, "Grape"); // Replace element

        // Iteration
        for (String fruit : fruits) {
            System.out.println(fruit);
        }

        // Stream operations
        List<String> upperCaseFruits = fruits.stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());
    }

    public static void demonstrateSetOperations() {
        Set<String> colors = new HashSet<>();
        colors.add("Red");
        colors.add("Blue");
        colors.add("Red"); // Duplicate, won't be added

        // Set operations
        Set<String> primaryColors = new HashSet<>(Arrays.asList("Red", "Blue", "Yellow"));
        Set<String> warmColors = new HashSet<>(Arrays.asList("Red", "Orange", "Yellow"));

        // Union
        Set<String> allColors = new HashSet<>(primaryColors);
        allColors.addAll(warmColors);

        // Intersection
        Set<String> commonColors = new HashSet<>(primaryColors);
        commonColors.retainAll(warmColors);

        // Difference
        Set<String> uniqueToPrimary = new HashSet<>(primaryColors);
        uniqueToPrimary.removeAll(warmColors);
    }

    public static void demonstrateMapOperations() {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 95);
        scores.put("Bob", 87);
        scores.put("Charlie", 92);

        // Access operations
        int aliceScore = scores.get("Alice");
        boolean hasBob = scores.containsKey("Bob");
        int bobScore = scores.getOrDefault("Bob", 0);

        // Iteration
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Functional operations
        scores.replaceAll((name, score) -> score + 5); // Add 5 to all scores
        Map<String, Integer> filteredScores = scores.entrySet().stream()
            .filter(entry -> entry.getValue() > 90)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
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

**Java HashMap Internal Implementation Example:**
```java
public class CustomHashMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Entry<K, V>[] table;
    private int size;
    private int threshold;

    public CustomHashMap() {
        table = new Entry[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    public void put(K key, V value) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);

        // Check if key already exists
        for (Entry<K, V> entry = table[index]; entry != null; entry = entry.next) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        // Add new entry
        addEntry(hash, key, value, index);
    }

    public V get(K key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);

        for (Entry<K, V> entry = table[index]; entry != null; entry = entry.next) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    private void addEntry(int hash, K key, V value, int index) {
        Entry<K, V> newEntry = new Entry<>(hash, key, value, table[index]);
        table[index] = newEntry;

        if (++size >= threshold) {
            resize(2 * table.length);
        }
    }

    private void resize(int newCapacity) {
        Entry<K, V>[] oldTable = table;
        table = new Entry[newCapacity];
        threshold = (int) (newCapacity * LOAD_FACTOR);

        for (Entry<K, V> entry : oldTable) {
            while (entry != null) {
                Entry<K, V> next = entry.next;
                int index = indexFor(entry.hash, newCapacity);
                entry.next = table[index];
                table[index] = entry;
                entry = next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1); // Equivalent to hash % length for power of 2
    }

    static class Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Entry<K, V> next;

        Entry(int hash, K key, V value, Entry<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}

// Usage example showing hash collisions
public class HashMapCollisionsDemo {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();

        // These keys might collide depending on hash function
        map.put("Aa", 1);
        map.put("BB", 2);

        // In Java 8+, if collisions exceed threshold, converts to balanced tree
        for (int i = 0; i < 20; i++) {
            map.put("key" + i, i);
        }

        System.out.println("Map size: " + map.size());
        System.out.println("Bucket count would be: " + (int) Math.ceil(map.size() / 0.75));
    }
}
```

### Q4: What is the difference between HashMap and Hashtable?
**Answer:**
- **HashMap**: Not synchronized, allows null keys/values, better performance
- **Hashtable**: Synchronized, doesn't allow null keys/values, legacy class
- **ConcurrentHashMap**: Better than Hashtable for concurrent operations

### Q5: Explain fail-fast vs fail-safe iterators.
**Answer:**
- **Fail-fast**: Throws ConcurrentModificationException if collection modified during iteration (ArrayList, HashMap)
- **Fail-safe**: Creates copy, doesn't throw exception (ConcurrentHashMap, CopyOnWriteArrayList)

### Q6: What is the difference between `Comparable` and `Comparator`?
**Answer:**
**Comparable:**
- Single sorting sequence
- `compareTo()` method in the class itself
- Natural ordering

**Comparator:**
- Multiple sorting sequences
- Separate class implementing `Comparator`
- Custom ordering

```java
class Employee implements Comparable<Employee> {
    private String name;
    private int salary;

    @Override
    public int compareTo(Employee other) {
        return this.salary - other.salary; // Natural ordering by salary
    }
}

// Custom comparator for sorting by name
class NameComparator implements Comparator<Employee> {
    @Override
    public int compare(Employee e1, Employee e2) {
        return e1.getName().compareTo(e2.getName());
    }
}
```

### Q7: Explain the `hashCode()` and `equals()` contract.
**Answer:**
**Contract Rules:**
1. If two objects are equal (equals() returns true), they must have same hashCode()
2. If two objects have same hashCode(), they may or may not be equal
3. If equals() is overridden, hashCode() must also be overridden

**Why important:**
- HashMap, HashSet rely on this contract
- Ensures correct behavior in hash-based collections

```java
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Person person = (Person) obj;
    return age == person.age && Objects.equals(name, person.name);
}

@Override
public int hashCode() {
    return Objects.hash(name, age);
}
```

### Q8: What is the difference between `ArrayList` and `Vector`?
**Answer:**
- **Synchronization**: Vector is synchronized, ArrayList is not
- **Performance**: ArrayList faster for single-threaded operations
- **Growth**: Both double size when full, but Vector has constructor to specify increment
- **Legacy**: Vector is legacy class, ArrayList preferred

### Q9: Explain `WeakHashMap` and its use cases.
**Answer:**
- Keys are weak references
- Entries automatically removed when key is no longer strongly referenced
- Useful for implementing caches where entries should be garbage collected when key is no longer used

```java
WeakHashMap<Key, Value> map = new WeakHashMap<>();
Key key = new Key();
map.put(key, "value");

// When key goes out of scope and no strong references exist,
// the entry may be removed by garbage collector
key = null; // Entry may be removed during next GC cycle
```

### Q10: What are concurrent collections in Java?
**Answer:**
Thread-safe collections for concurrent programming.

**Key Classes:**
- **ConcurrentHashMap**: Thread-safe HashMap with better performance than Hashtable
- **CopyOnWriteArrayList**: Thread-safe ArrayList, creates copy for modifications
- **BlockingQueue**: Interface for producer-consumer patterns
- **ConcurrentSkipListMap/Set**: Concurrent sorted collections

**Benefits over synchronized collections:**
- Better performance
- No need for external synchronization
- Support for concurrent reads/writes

---

## Multithreading & Concurrency

### Q1: What is the difference between `Thread` and `Runnable`?
**Answer:**
- **Thread**: Class that represents a thread of execution
- **Runnable**: Interface with `run()` method
- **Best Practice**: Implement Runnable for better composition

**Java Threading Examples:**
```java
// 1. Extending Thread (not recommended)
public class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread is running: " + Thread.currentThread().getName());
    }
}

// 2. Implementing Runnable (recommended)
public class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable is running: " + Thread.currentThread().getName());
    }
}

// 3. Using lambda (Java 8+)
Runnable lambdaRunnable = () -> {
    System.out.println("Lambda runnable: " + Thread.currentThread().getName());
};

// Usage
public class ThreadCreationDemo {
    public static void main(String[] args) {
        // Thread subclass
        Thread thread1 = new MyThread();
        thread1.start();

        // Runnable implementation
        Thread thread2 = new Thread(new MyRunnable());
        thread2.start();

        // Lambda runnable
        Thread thread3 = new Thread(lambdaRunnable);
        thread3.start();

        // Anonymous Runnable
        Thread thread4 = new Thread(() -> {
            System.out.println("Anonymous runnable: " + Thread.currentThread().getName());
        });
        thread4.start();

        // Using ExecutorService (preferred for thread management)
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(new MyRunnable());
        executor.submit(lambdaRunnable);
        executor.shutdown();
    }
}
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

**Java Synchronization Examples:**
```java
public class SynchronizationDemo {
    private int counter = 0;

    // 1. Synchronized method
    public synchronized void increment() {
        counter++;
    }

    // 2. Synchronized block
    public void incrementWithBlock() {
        synchronized (this) {
            counter++;
        }
    }

    // 3. Static synchronized method
    private static int staticCounter = 0;

    public static synchronized void incrementStatic() {
        staticCounter++;
    }

    // 4. Synchronized block on class object
    public void incrementStaticWithBlock() {
        synchronized (SynchronizationDemo.class) {
            staticCounter++;
        }
    }
}

// Race condition example
public class RaceConditionDemo {
    private static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        Runnable incrementTask = () -> {
            for (int i = 0; i < 1000; i++) {
                counter++; // Race condition here
            }
        };

        Thread thread1 = new Thread(incrementTask);
        Thread thread2 = new Thread(incrementTask);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Final counter: " + counter); // Might not be 2000
    }
}

// Fixed version with synchronization
public class SynchronizedCounter {
    private static int counter = 0;
    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Runnable incrementTask = () -> {
            for (int i = 0; i < 1000; i++) {
                synchronized (lock) {
                    counter++;
                }
            }
        };

        Thread thread1 = new Thread(incrementTask);
        Thread thread2 = new Thread(incrementTask);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Final counter: " + counter); // Will be 2000
    }
}
```

### Q3: What is the volatile keyword?
**Answer:**
- Ensures visibility of changes across threads
- Prevents compiler optimizations that might cache variables
- Doesn't provide atomicity for compound operations

**Java Volatile Examples:**
```java
public class VolatileDemo {
    private volatile boolean flag = false;
    private int counter = 0;

    // Thread 1: Writer
    public void writer() {
        counter = 42;      // 1. Write to non-volatile variable
        flag = true;       // 2. Write to volatile variable (creates memory barrier)
    }

    // Thread 2: Reader
    public void reader() {
        if (flag) {        // 3. Read volatile variable
            // Due to volatile, counter will be 42 here (visibility guarantee)
            System.out.println("Counter: " + counter);
        }
    }
}

// Volatile vs synchronized
public class VolatileVsSynchronized {
    private volatile int volatileCounter = 0;
    private int synchronizedCounter = 0;

    // Volatile doesn't guarantee atomicity for compound operations
    public void incrementVolatile() {
        volatileCounter++; // Not atomic - race condition possible
    }

    // Synchronized guarantees both visibility and atomicity
    public synchronized void incrementSynchronized() {
        synchronizedCounter++; // Atomic and visible to all threads
    }
}

// Double-checked locking with volatile (Singleton pattern)
public class Singleton {
    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) { // First check (no locking)
            synchronized (Singleton.class) {
                if (instance == null) { // Second check (with locking)
                    instance = new Singleton(); // Volatile ensures visibility
                }
            }
        }
        return instance;
    }
}

// Volatile with long/double (for 32-bit systems)
public class VolatileLongDemo {
    private volatile long sharedLong = 0;

    public void setSharedLong(long value) {
        sharedLong = value; // Volatile ensures 64-bit write is atomic
    }

    public long getSharedLong() {
        return sharedLong; // Volatile ensures 64-bit read is atomic
    }
}
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

### Q6: Explain the `Executor` framework.
**Answer:**
Framework for managing thread pools and asynchronous task execution.

**Key Components:**
- **Executor**: Interface for executing tasks
- **ExecutorService**: Extended interface with lifecycle management
- **ThreadPoolExecutor**: Configurable thread pool implementation
- **Executors**: Factory methods for creating executors

```java
// Creating a fixed thread pool
ExecutorService executor = Executors.newFixedThreadPool(10);

// Submitting tasks
Future<String> future = executor.submit(() -> {
    // Task logic
    return "Result";
});

// Getting result (blocking)
String result = future.get();

// Shutting down executor
executor.shutdown();
```

### Q7: What is the difference between `CyclicBarrier` and `CountDownLatch`?
**Answer:**
**CountDownLatch:**
- One-time use
- Threads wait until count reaches zero
- Cannot be reset

**CyclicBarrier:**
- Reusable
- Threads wait for each other at a barrier point
- Can be reset after all threads reach the barrier

```java
// CountDownLatch example
CountDownLatch latch = new CountDownLatch(3);
for (int i = 0; i < 3; i++) {
    new Thread(() -> {
        // Do work
        latch.countDown();
    }).start();
}
latch.await(); // Wait for all threads to finish

// CyclicBarrier example
CyclicBarrier barrier = new CyclicBarrier(3, () -> System.out.println("All threads reached barrier"));
for (int i = 0; i < 3; i++) {
    new Thread(() -> {
        // Do work
        barrier.await(); // Wait for others
    }).start();
}
```

### Q8: Explain thread safety and how to achieve it.
**Answer:**
**Thread Safety:** Class behaves correctly when accessed from multiple threads.

**Ways to achieve:**
1. **Synchronization**: Use synchronized blocks/methods
2. **Atomic Classes**: Use AtomicInteger, AtomicReference, etc.
3. **Immutable Objects**: Cannot be modified after creation
4. **ThreadLocal**: Thread-specific variables
5. **Concurrent Collections**: CopyOnWriteArrayList, ConcurrentHashMap

```java
// Thread-safe counter using AtomicInteger
public class SafeCounter {
    private AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();
    }

    public int getCount() {
        return count.get();
    }
}
```

### Q9: What is a deadlock? How to prevent it?
**Answer:**
**Deadlock:** Situation where two or more threads are blocked forever, waiting for each other.

**Conditions for deadlock (Coffman conditions):**
1. **Mutual Exclusion**: Resource can only be used by one thread
2. **Hold and Wait**: Thread holds resource while waiting for another
3. **No Preemption**: Resources cannot be forcibly taken
4. **Circular Wait**: Circular chain of threads waiting for resources

**Prevention:**
- Acquire locks in consistent order
- Use timeout for lock acquisition
- Avoid nested locks
- Use tryLock() instead of lock()

### Q10: Explain the `Fork/Join` framework.
**Answer:**
Framework for parallel processing of tasks that can be divided into subtasks.

**Key Classes:**
- **ForkJoinPool**: Thread pool for fork/join tasks
- **RecursiveTask**: For tasks that return results
- **RecursiveAction**: For tasks that don't return results

```java
class SumTask extends RecursiveTask<Long> {
    private final long[] array;
    private final int start, end;

    SumTask(long[] array, int start, int end) {
        this.array = array;
        this.start = start; this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            // Compute directly
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        } else {
            // Split and fork
            int mid = (start + end) / 2;
            SumTask left = new SumTask(array, start, mid);
            SumTask right = new SumTask(array, mid, end);
            left.fork();
            long rightResult = right.compute();
            long leftResult = left.join();
            return leftResult + rightResult;
        }
    }
}
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

**Java Memory Areas Example:**
```java
public class MemoryAreasDemo {
    // Method Area: Class metadata, static variables
    private static final String CONSTANT = "Stored in Method Area";
    private static int staticVar = 42; // Stored in Method Area

    // Heap: Objects and instance variables
    private String instanceVar = "Stored in Heap";

    public void demonstrateMemoryAreas() {
        // Stack: Local variables and method calls
        int localVar = 100; // Stored in Stack
        Object localObject = new Object(); // Reference in Stack, object in Heap

        // PC Register: Current instruction pointer (managed by JVM)

        // Native Method Stack: For native method calls
        System.out.println("This calls native methods internally");
    }

    public static void main(String[] args) { // args reference in Stack
        MemoryAreasDemo demo = new MemoryAreasDemo(); // demo reference in Stack, object in Heap
        demo.demonstrateMemoryAreas(); // Method call frame in Stack
    }
}

// Memory monitoring
public class MemoryMonitor {
    public static void printMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();

        long totalMemory = runtime.totalMemory(); // Heap size
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        System.out.println("Total Memory: " + totalMemory / 1024 / 1024 + " MB");
        System.out.println("Used Memory: " + usedMemory / 1024 / 1024 + " MB");
        System.out.println("Free Memory: " + freeMemory / 1024 / 1024 + " MB");
    }
}
```

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

**Java GC Monitoring and Tuning Example:**
```java
public class GCMonitoringDemo {
    private static final List<Object> objects = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Starting GC monitoring demo");

        // Print initial memory
        printMemoryUsage();

        // Create objects to trigger GC
        for (int i = 0; i < 100000; i++) {
            objects.add(new byte[1024]); // 1KB objects

            if (i % 10000 == 0) {
                System.out.println("Created " + i + " objects");
                printMemoryUsage();

                // Manually trigger GC (not recommended in production)
                System.gc();
                printMemoryUsage();
            }
        }

        // Clear references to make objects eligible for GC
        objects.clear();
        System.gc();
        System.out.println("After clearing references:");
        printMemoryUsage();
    }

    private static void printMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory();

        System.out.printf("Memory - Used: %dMB, Free: %dMB, Total: %dMB, Max: %dMB%n",
            usedMemory / 1024 / 1024,
            freeMemory / 1024 / 1024,
            totalMemory / 1024 / 1024,
            maxMemory / 1024 / 1024);
    }
}

// JVM GC tuning parameters (in application startup)
// -Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200
// -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log

// Programmatic GC monitoring
public class GCStats {
    public static void monitorGC() {
        // Using ManagementFactory to get GC information
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();

        for (GarbageCollectorMXBean gcBean : gcBeans) {
            System.out.println("GC Name: " + gcBean.getName());
            System.out.println("Collection Count: " + gcBean.getCollectionCount());
            System.out.println("Collection Time: " + gcBean.getCollectionTime() + " ms");
        }
    }
}
```

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

### Q4: What is the Java Memory Model (JMM)?
**Answer:**
Specification that describes how threads interact through memory and what behaviors are legal in concurrent programs.

**Key Concepts:**
- **Happens-before relationship**: Defines ordering of operations
- **Visibility**: When changes made by one thread are visible to others
- **Atomicity**: Operations that appear indivisible to other threads

**Happens-before rules:**
- Program order rule
- Monitor lock rule
- Volatile variable rule
- Thread start rule
- Thread termination rule

### Q5: Explain JIT compilation.
**Answer:**
**Just-In-Time (JIT) Compilation:** Converts bytecode to native machine code at runtime.

**Benefits:**
- Better performance than interpretation
- Platform-specific optimizations
- Dynamic recompilation based on runtime behavior

**Process:**
1. **Interpretation**: Initially interpret bytecode
2. **Profiling**: Collect statistics about method execution
3. **Compilation**: Compile hot methods to native code
4. **Optimization**: Apply various optimizations

### Q6: What are JVM tuning parameters?
**Answer:**
Common JVM parameters for performance tuning:

**Memory Settings:**
- `-Xms`: Initial heap size
- `-Xmx`: Maximum heap size
- `-Xss`: Thread stack size

**GC Settings:**
- `-XX:+UseG1GC`: Use G1 garbage collector
- `-XX:MaxGCPauseMillis`: Target pause time
- `-XX:G1HeapRegionSize`: G1 region size

**Other:**
- `-XX:+PrintGCDetails`: Print GC details
- `-XX:+HeapDumpOnOutOfMemoryError`: Dump heap on OOM

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

**Java Functional Interface Examples:**
```java
import java.util.function.*;
import java.util.Arrays;
import java.util.List;

public class FunctionalInterfacesDemo {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

        // 1. Predicate - takes input, returns boolean
        Predicate<String> startsWithA = name -> name.startsWith("A");
        names.stream().filter(startsWithA).forEach(System.out::println);

        // 2. Function - takes input, returns output
        Function<String, Integer> nameLength = String::length;
        names.stream().map(nameLength).forEach(System.out::println);

        // 3. Consumer - takes input, returns nothing
        Consumer<String> printUpperCase = name -> System.out.println(name.toUpperCase());
        names.forEach(printUpperCase);

        // 4. Supplier - takes nothing, returns output
        Supplier<Double> randomSupplier = Math::random;
        System.out.println("Random: " + randomSupplier.get());

        // 5. Comparator - compares two inputs
        Comparator<String> byLength = Comparator.comparingInt(String::length);
        names.stream().sorted(byLength).forEach(System.out::println);

        // 6. Runnable - takes nothing, returns nothing
        Runnable task = () -> System.out.println("Task executed");
        new Thread(task).start();

        // 7. Callable - takes nothing, returns result (can throw exception)
        Callable<String> callableTask = () -> {
            Thread.sleep(1000);
            return "Result from callable";
        };
    }
}

// Custom functional interface
@FunctionalInterface
interface Calculator {
    double calculate(double a, double b);

    // Default method
    default double add(double a, double b) {
        return a + b;
    }

    // Static method
    static double multiply(double a, double b) {
        return a * b;
    }
}

public class CustomFunctionalInterfaceDemo {
    public static void main(String[] args) {
        // Using lambda
        Calculator addition = (a, b) -> a + b;
        Calculator subtraction = (a, b) -> a - b;

        System.out.println("Addition: " + addition.calculate(10, 5));
        System.out.println("Subtraction: " + subtraction.calculate(10, 5));

        // Using default method
        System.out.println("Default add: " + addition.add(10, 5));

        // Using static method
        System.out.println("Static multiply: " + Calculator.multiply(10, 5));
    }
}
```

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

**Java Streams Examples:**
```java
import java.util.*;
import java.util.stream.*;

public class StreamsDemo {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", "Engineering", 75000),
            new Employee("Bob", "HR", 50000),
            new Employee("Charlie", "Engineering", 80000),
            new Employee("David", "Sales", 60000),
            new Employee("Eve", "Engineering", 90000)
        );

        // 1. Filter and collect
        List<Employee> engineers = employees.stream()
            .filter(emp -> "Engineering".equals(emp.getDepartment()))
            .collect(Collectors.toList());

        // 2. Map and reduce
        double averageSalary = employees.stream()
            .mapToDouble(Employee::getSalary)
            .average()
            .orElse(0.0);

        // 3. Group by department
        Map<String, List<Employee>> byDepartment = employees.stream()
            .collect(Collectors.groupingBy(Employee::getDepartment));

        // 4. Find highest paid employee
        Optional<Employee> highestPaid = employees.stream()
            .max(Comparator.comparingDouble(Employee::getSalary));

        // 5. Partition by salary threshold
        Map<Boolean, List<Employee>> partitionedBySalary = employees.stream()
            .collect(Collectors.partitioningBy(emp -> emp.getSalary() > 70000));

        // 6. Complex pipeline
        Map<String, Double> avgSalaryByDept = employees.stream()
            .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.averagingDouble(Employee::getSalary)
            ));

        // 7. Parallel streams
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        int sum = numbers.parallelStream()
            .filter(n -> n % 2 == 0)
            .mapToInt(n -> n * n)
            .sum();

        // 8. Stream from other sources
        Stream<String> stringStream = Stream.of("A", "B", "C");
        IntStream intStream = IntStream.range(1, 10);
        Stream<Double> randomStream = Stream.generate(Math::random).limit(5);

        // 9. Custom collectors
        Collector<Employee, ?, Map<String, List<Employee>>> customCollector =
            Collector.of(
                HashMap::new,
                (map, emp) -> map.computeIfAbsent(emp.getDepartment(), k -> new ArrayList<>()).add(emp),
                (map1, map2) -> {
                    map2.forEach((dept, emps) ->
                        map1.computeIfAbsent(dept, k -> new ArrayList<>()).addAll(emps));
                    return map1;
                }
            );

        Map<String, List<Employee>> customGrouping = employees.stream()
            .collect(customCollector);
    }
}

class Employee {
    private String name;
    private String department;
    private double salary;

    public Employee(String name, String department, double salary) {
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    // Getters
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getSalary() { return salary; }
}
```

### Q4: Explain Optional class.
**Answer:**
Container that may or may not contain a value. Helps avoid NullPointerException.

**Java Optional Examples:**
```java
import java.util.Optional;
import java.util.List;
import java.util.Arrays;

public class OptionalDemo {
    public static void main(String[] args) {
        // 1. Creating Optional instances
        Optional<String> empty = Optional.empty();
        Optional<String> withValue = Optional.of("Hello");
        Optional<String> nullable = Optional.ofNullable(null); // Empty optional
        Optional<String> nullableWithValue = Optional.ofNullable("World");

        // 2. Checking presence
        System.out.println("Is present: " + withValue.isPresent());
        System.out.println("Is empty: " + empty.isEmpty());

        // 3. Getting values safely
        String result1 = withValue.orElse("Default");
        String result2 = empty.orElse("Default");

        String result3 = withValue.orElseGet(() -> "Computed default");
        String result4 = empty.orElseGet(() -> "Computed default");

        // 4. Throwing exceptions
        try {
            String result5 = empty.orElseThrow(() -> new RuntimeException("Value not present"));
        } catch (RuntimeException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        // 5. Conditional execution
        withValue.ifPresent(value -> System.out.println("Value: " + value));
        empty.ifPresentOrElse(
            value -> System.out.println("Value: " + value),
            () -> System.out.println("No value present")
        );

        // 6. Filtering and mapping
        Optional<String> filtered = withValue.filter(s -> s.length() > 3);
        Optional<Integer> mapped = withValue.map(String::length);

        // 7. Chaining operations
        Optional<String> chained = withValue
            .filter(s -> s.startsWith("H"))
            .map(String::toUpperCase)
            .or(() -> Optional.of("DEFAULT"));

        // 8. Real-world example
        User user = findUserById(123);
        String email = Optional.ofNullable(user)
            .map(User::getProfile)
            .map(Profile::getEmail)
            .orElse("default@email.com");

        // 9. Stream operations with Optional
        List<Optional<String>> optionals = Arrays.asList(
            Optional.of("A"), Optional.empty(), Optional.of("B")
        );

        List<String> presentValues = optionals.stream()
            .flatMap(Optional::stream) // Java 9+
            .collect(Collectors.toList());

        // Alternative for Java 8
        List<String> presentValuesJava8 = optionals.stream()
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    private static User findUserById(int id) {
        // Simulate database lookup that might return null
        return id == 123 ? new User(new Profile("user@example.com")) : null;
    }
}

class User {
    private Profile profile;

    public User(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }
}

class Profile {
    private String email;

    public Profile(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
```

### Q5: What are method references?
**Answer:**
Shorthand for lambda expressions that call existing methods.

**Types:**
- **Static method**: `String::valueOf`
- **Instance method**: `String::toUpperCase`
- **Constructor**: `ArrayList::new`

### Q6: Explain the `CompletableFuture` class.
**Answer:**
Provides a way to write asynchronous, non-blocking code in a more functional style.

**Key Features:**
- **Composability**: Chain multiple asynchronous operations
- **Exception handling**: Handle errors in async operations
- **Combining results**: Combine results from multiple futures

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> "Hello")
    .thenApply(s -> s + " World")
    .thenApply(String::toUpperCase);

// Blocking get (not recommended in production)
String result = future.get();
```

### Q7: What are default and static methods in interfaces?
**Answer:**
**Default Methods:**
- Provide implementation in interface
- Can be overridden by implementing classes
- Enable interface evolution without breaking existing code

**Static Methods:**
- Belong to interface, not instances
- Cannot be overridden
- Called using InterfaceName.methodName()

```java
interface Calculator {
    default int add(int a, int b) {
        return a + b;
    }

    static int multiply(int a, int b) {
        return a * b;
    }
}

class SimpleCalculator implements Calculator {
    // Can override default method
    @Override
    public int add(int a, int b) {
        return Math.addExact(a, b); // Safer addition
    }
}

// Usage
Calculator calc = new SimpleCalculator();
int sum = calc.add(5, 3);           // Uses overridden method
int product = Calculator.multiply(5, 3); // Static method
```

### Q8: Explain the `LocalDate`, `LocalTime`, and `LocalDateTime` classes.
**Answer:**
New date/time API introduced in Java 8 to replace problematic `Date` and `Calendar`.

**Key Classes:**
- **LocalDate**: Date without time (2023-12-25)
- **LocalTime**: Time without date (14:30:00)
- **LocalDateTime**: Date and time (2023-12-25T14:30:00)

```java
// Creating instances
LocalDate date = LocalDate.of(2023, 12, 25);
LocalTime time = LocalTime.of(14, 30);
LocalDateTime dateTime = LocalDateTime.of(date, time);

// Current date/time
LocalDate today = LocalDate.now();
LocalDateTime now = LocalDateTime.now();

// Formatting
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
String formatted = dateTime.format(formatter);
```

### Q9: What is the `Collector` interface in Streams?
**Answer:**
Interface for mutable reduction operations on streams.

**Common Collectors:**
- `toList()`, `toSet()`: Collect to collections
- `joining()`: Concatenate strings
- `groupingBy()`: Group elements
- `partitioningBy()`: Partition into two groups

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

// Grouping by length
Map<Integer, List<String>> groupedByLength = names.stream()
    .collect(Collectors.groupingBy(String::length));

// Partitioning by condition
Map<Boolean, List<String>> partitioned = names.stream()
    .collect(Collectors.partitioningBy(name -> name.length() > 4));
```

### Q10: Explain modules in Java 9+.
**Answer:**
Modules provide better encapsulation and dependency management.

**Key Concepts:**
- **Module**: Named, self-describing collection of code
- **module-info.java**: Module descriptor file
- **Requires**: Dependencies on other modules
- **Exports**: Packages available to other modules

```java
// module-info.java
module com.example.myapp {
    requires java.sql;
    requires com.example.utils;

    exports com.example.myapp.api;
    exports com.example.myapp.model;
}
```

---

## Design Patterns

### Q1: Explain Singleton pattern with thread-safe implementation.
**Answer:**
Ensures only one instance of a class exists.

**Thread-Safe Implementations:**
```java
// 1. Eager initialization (thread-safe, but creates instance even if not used)
public class EagerSingleton {
    private static final EagerSingleton instance = new EagerSingleton();

    private EagerSingleton() {}

    public static EagerSingleton getInstance() {
        return instance;
    }
}

// 2. Lazy initialization with double-checked locking
public class LazySingleton {
    private static volatile LazySingleton instance;

    private LazySingleton() {}

    public static LazySingleton getInstance() {
        if (instance == null) {
            synchronized (LazySingleton.class) {
                if (instance == null) {
                    instance = new LazySingleton();
                }
            }
        }
        return instance;
    }
}

// 3. Bill Pugh Singleton (recommended)
public class BillPughSingleton {
    private BillPughSingleton() {}

    private static class SingletonHelper {
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }

    public static BillPughSingleton getInstance() {
        return SingletonHelper.INSTANCE;
    }
}

// 4. Enum Singleton (most robust)
public enum EnumSingleton {
    INSTANCE;

    public void doSomething() {
        System.out.println("Singleton method called");
    }
}

// Usage
public class SingletonDemo {
    public static void main(String[] args) {
        // Enum singleton
        EnumSingleton singleton = EnumSingleton.INSTANCE;
        singleton.doSomething();

        // Bill Pugh singleton
        BillPughSingleton bpSingleton = BillPughSingleton.getInstance();

        // Thread safety test
        Runnable task = () -> {
            LazySingleton lazy = LazySingleton.getInstance();
            System.out.println("Instance: " + lazy.hashCode());
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();
    }
}
```

### Q2: Explain Factory pattern.
**Answer:**
Creates objects without specifying exact classes.

**Java Factory Pattern Examples:**
```java
// 1. Simple Factory Pattern
interface Shape {
    void draw();
}

class Circle implements Shape {
    @Override
    public void draw() {
        System.out.println("Drawing Circle");
    }
}

class Rectangle implements Shape {
    @Override
    public void draw() {
        System.out.println("Drawing Rectangle");
    }
}

class Triangle implements Shape {
    @Override
    public void draw() {
        System.out.println("Drawing Triangle");
    }
}

class ShapeFactory {
    public static Shape createShape(String type) {
        switch (type.toLowerCase()) {
            case "circle":
                return new Circle();
            case "rectangle":
                return new Rectangle();
            case "triangle":
                return new Triangle();
            default:
                throw new IllegalArgumentException("Unknown shape type: " + type);
        }
    }
}

// 2. Factory Method Pattern
abstract class DocumentCreator {
    public abstract Document createDocument();

    public void processDocument() {
        Document doc = createDocument();
        doc.open();
        doc.save();
        doc.close();
    }
}

class PDFDocumentCreator extends DocumentCreator {
    @Override
    public Document createDocument() {
        return new PDFDocument();
    }
}

class WordDocumentCreator extends DocumentCreator {
    @Override
    public Document createDocument() {
        return new WordDocument();
    }
}

interface Document {
    void open();
    void save();
    void close();
}

class PDFDocument implements Document {
    @Override
    public void open() { System.out.println("Opening PDF document"); }
    @Override
    public void save() { System.out.println("Saving PDF document"); }
    @Override
    public void close() { System.out.println("Closing PDF document"); }
}

class WordDocument implements Document {
    @Override
    public void open() { System.out.println("Opening Word document"); }
    @Override
    public void save() { System.out.println("Saving Word document"); }
    @Override
    public void close() { System.out.println("Closing Word document"); }
}

// 3. Abstract Factory Pattern
interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}

class WindowsFactory implements GUIFactory {
    @Override
    public Button createButton() { return new WindowsButton(); }
    @Override
    public Checkbox createCheckbox() { return new WindowsCheckbox(); }
}

class MacFactory implements GUIFactory {
    @Override
    public Button createButton() { return new MacButton(); }
    @Override
    public Checkbox createCheckbox() { return new MacCheckbox(); }
}

interface Button { void paint(); }
interface Checkbox { void paint(); }

class WindowsButton implements Button {
    @Override
    public void paint() { System.out.println("Rendering Windows button"); }
}

class MacButton implements Button {
    @Override
    public void paint() { System.out.println("Rendering Mac button"); }
}

class WindowsCheckbox implements Checkbox {
    @Override
    public void paint() { System.out.println("Rendering Windows checkbox"); }
}

class MacCheckbox implements Checkbox {
    @Override
    public void paint() { System.out.println("Rendering Mac checkbox"); }
}

// Usage
public class FactoryPatternDemo {
    public static void main(String[] args) {
        // Simple Factory
        Shape circle = ShapeFactory.createShape("circle");
        circle.draw();

        // Factory Method
        DocumentCreator pdfCreator = new PDFDocumentCreator();
        pdfCreator.processDocument();

        // Abstract Factory
        GUIFactory factory = new WindowsFactory();
        Button button = factory.createButton();
        Checkbox checkbox = factory.createCheckbox();
        button.paint();
        checkbox.paint();
    }
}
```

### Q3: Explain Observer pattern.
**Answer:**
One-to-many dependency where changes in one object notify others.

**Java Observer Pattern Examples:**
```java
import java.util.*;

// 1. Traditional Observer Pattern
interface Observer {
    void update(String message);
}

interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers();
}

class NewsAgency implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private String news;

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(news);
        }
    }

    public void setNews(String news) {
        this.news = news;
        notifyObservers();
    }
}

class NewsChannel implements Observer {
    private String name;

    public NewsChannel(String name) {
        this.name = name;
    }

    @Override
    public void update(String news) {
        System.out.println(name + " received news: " + news);
    }
}

// 2. Using Java's built-in Observer (deprecated)
@Deprecated
class WeatherData extends Observable {
    private float temperature;
    private float humidity;
    private float pressure;

    public void measurementsChanged() {
        setChanged();
        notifyObservers();
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementsChanged();
    }

    public float getTemperature() { return temperature; }
    public float getHumidity() { return humidity; }
    public float getPressure() { return pressure; }
}

@Deprecated
class CurrentConditionsDisplay implements java.util.Observer {
    private float temperature;
    private float humidity;

    @Override
    public void update(Observable obs, Object arg) {
        if (obs instanceof WeatherData) {
            WeatherData weatherData = (WeatherData) obs;
            this.temperature = weatherData.getTemperature();
            this.humidity = weatherData.getHumidity();
            display();
        }
    }

    public void display() {
        System.out.println("Current conditions: " + temperature + "F degrees and " +
                          humidity + "% humidity");
    }
}

// 3. Property Change Listener (modern approach)
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

class Person {
    private String name;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        support.firePropertyChange("name", oldName, name);
    }

    public String getName() {
        return name;
    }
}

class NameChangeListener implements PropertyChangeListener {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("Name changed from " + evt.getOldValue() +
                          " to " + evt.getNewValue());
    }
}

// 4. Reactive Streams (Java 9+)
import java.util.concurrent.Flow.*;
import java.util.concurrent.SubmissionPublisher;

class NewsPublisher extends SubmissionPublisher<String> implements Publisher<String> {
    public void publishNews(String news) {
        submit(news);
    }
}

class NewsSubscriber implements Subscriber<String> {
    private Subscription subscription;
    private String name;

    public NewsSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(String item) {
        System.out.println(name + " received: " + item);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(name + " error: " + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println(name + " completed");
    }
}

// Usage
public class ObserverPatternDemo {
    public static void main(String[] args) {
        // Custom Observer Pattern
        NewsAgency agency = new NewsAgency();
        NewsChannel channel1 = new NewsChannel("CNN");
        NewsChannel channel2 = new NewsChannel("BBC");

        agency.attach(channel1);
        agency.attach(channel2);
        agency.setNews("Breaking news!");

        // Property Change Listener
        Person person = new Person();
        person.addPropertyChangeListener(new NameChangeListener());
        person.setName("John");
        person.setName("Jane");

        // Reactive Streams
        NewsPublisher publisher = new NewsPublisher();
        NewsSubscriber subscriber1 = new NewsSubscriber("Subscriber1");
        NewsSubscriber subscriber2 = new NewsSubscriber("Subscriber2");

        publisher.subscribe(subscriber1);
        publisher.subscribe(subscriber2);
        publisher.publishNews("Reactive news!");
        publisher.close();
    }
}
```

### Q4: Explain Strategy pattern.
**Answer:**
Defines a family of algorithms, encapsulates each one, and makes them interchangeable.

```java
interface PaymentStrategy {
    void pay(double amount);
}

class CreditCardPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using credit card");
    }
}

class PayPalPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using PayPal");
    }
}

class ShoppingCart {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void checkout(double amount) {
        paymentStrategy.pay(amount);
    }
}
```

### Q5: Explain Decorator pattern.
**Answer:**
Attaches additional responsibilities to an object dynamically, providing flexible alternative to subclassing.

```java
interface Coffee {
    double cost();
    String description();
}

class SimpleCoffee implements Coffee {
    @Override
    public double cost() { return 2.0; }

    @Override
    public String description() { return "Simple coffee"; }
}

abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;

    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
}

class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public double cost() {
        return coffee.cost() + 0.5;
    }

    @Override
    public String description() {
        return coffee.description() + " with milk";
    }
}
```

### Q6: Explain Builder pattern.
**Answer:**
Separates the construction of a complex object from its representation, allowing the same construction process to create different representations.

```java
class Computer {
    private String cpu;
    private String ram;
    private String storage;
    // getters...

    private Computer(Builder builder) {
        this.cpu = builder.cpu;
        this.ram = builder.ram;
        this.storage = builder.storage;
    }

    static class Builder {
        private String cpu;
        private String ram;
        private String storage;

        public Builder cpu(String cpu) {
            this.cpu = cpu;
            return this;
        }

        public Builder ram(String ram) {
            this.ram = ram;
            return this;
        }

        public Builder storage(String storage) {
            this.storage = storage;
            return this;
        }

        public Computer build() {
            return new Computer(this);
        }
    }
}

// Usage
Computer computer = new Computer.Builder()
    .cpu("Intel i7")
    .ram("16GB")
    .storage("512GB SSD")
    .build();
```

---

## Microservices

### Q1: What are microservices? How do they differ from monolithic applications?
**Answer:**
**Microservices:** Architectural style where application is built as collection of small, independent services.

**vs Monolithic:**
- **Deployment**: Microservices deploy independently, monolithic as single unit
- **Technology**: Different services can use different technologies
- **Scaling**: Scale individual services based on demand
- **Development**: Teams work on separate services
- **Fault Isolation**: Failure in one service doesn't bring down entire application

**Java Spring Boot Microservice Example:**
```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final InventoryClient inventoryClient;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        // Check inventory via Feign client
        InventoryResponse inventory = inventoryClient.checkInventory(request.getProductId());

        if (inventory.getQuantity() < request.getQuantity()) {
            throw new InsufficientInventoryException("Not enough inventory");
        }

        Order order = orderService.createOrder(request);
        return ResponseEntity.ok(order);
    }
}

@FeignClient(name = "inventory-service")
public interface InventoryClient {
    @GetMapping("/inventory/{productId}")
    InventoryResponse checkInventory(@PathVariable String productId);
}
```

### Q2: Explain service discovery in microservices.
**Answer:**
Mechanism for services to find and communicate with each other dynamically.

**Types:**
- **Client-side**: Client queries registry and selects service instance
- **Server-side**: Load balancer/proxy routes requests

**Popular Tools:**
- **Eureka**: Netflix service registry
- **Consul**: HashiCorp service discovery
- **Kubernetes**: Built-in service discovery

**Java Implementation Example (Eureka Client):**
```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class PaymentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }
}

// Service registration with Eureka
@RestController
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/payments")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.processPayment(request);
        return ResponseEntity.ok(response);
    }
}

// Client-side service discovery with Feign
@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/users/{userId}")
    User getUser(@PathVariable String userId);
}

@Service
public class PaymentService {
    private final UserServiceClient userServiceClient;

    public PaymentResponse processPayment(PaymentRequest request) {
        // Discover and call user service
        User user = userServiceClient.getUser(request.getUserId());

        // Process payment logic
        return new PaymentResponse("SUCCESS", "Payment processed for " + user.getName());
    }
}

// application.yml
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
  instance:
    preferIpAddress: true

spring:
  application:
    name: payment-service
```

### Q3: What is circuit breaker pattern?
**Answer:**
Prevents cascading failures by stopping calls to failing services.

**States:**
- **Closed**: Normal operation, requests pass through
- **Open**: Service failing, requests fail fast
- **Half-Open**: Testing if service recovered

```java
@CircuitBreaker(name = "inventoryService", fallbackMethod = "fallbackInventory")
public String getInventory() {
    return restTemplate.getForObject("http://inventory-service/inventory", String.class);
}

public String fallbackInventory(Throwable t) {
    return "Inventory service unavailable";
}
```

### Q4: Explain API Gateway pattern.
**Answer:**
Single entry point for all client requests to microservices.

**Responsibilities:**
- **Routing**: Route requests to appropriate services
- **Authentication**: Handle authentication/authorization
- **Rate Limiting**: Control request rates
- **Logging**: Centralize request logging
- **Transformation**: Transform requests/responses

**Benefits:**
- Simplifies client code
- Centralizes cross-cutting concerns
- Provides single point for monitoring/security

### Q5: What are the challenges of microservices?
**Answer:**
- **Distributed Systems Complexity**: Network latency, partial failures
- **Data Consistency**: Maintaining consistency across services
- **Service Coordination**: Managing inter-service communication
- **Testing**: More complex testing scenarios
- **Deployment**: Managing multiple deployments
- **Monitoring**: Tracking issues across services
- **Team Coordination**: Aligning multiple teams

### Q6: Explain Saga pattern for distributed transactions.
**Answer:**
Manages distributed transactions in microservices by coordinating local transactions.

**Types:**
- **Choreography**: Services communicate directly, no central coordinator
- **Orchestration**: Central orchestrator coordinates the saga

**Example (Choreography):**
1. Order service creates order
2. Publishes "OrderCreated" event
3. Payment service processes payment
4. Publishes "PaymentProcessed" event
5. Inventory service reserves items
6. If any step fails, compensating actions executed

**Java Implementation Example (Orchestration with Spring State Machine):**
```java
@Configuration
@EnableStateMachine
public class OrderSagaConfig extends StateMachineConfigurerAdapter<OrderStates, OrderEvents> {

    @Override
    public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states) throws Exception {
        states
            .withStates()
                .initial(OrderStates.ORDER_CREATED)
                .state(OrderStates.PAYMENT_PROCESSED)
                .state(OrderStates.INVENTORY_RESERVED)
                .state(OrderStates.ORDER_COMPLETED)
                .state(OrderStates.ORDER_FAILED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions) throws Exception {
        transitions
            .withExternal()
                .source(OrderStates.ORDER_CREATED).target(OrderStates.PAYMENT_PROCESSED)
                .event(OrderEvents.PAYMENT_SUCCESS)
                .action(paymentProcessedAction())
            .and()
            .withExternal()
                .source(OrderStates.PAYMENT_PROCESSED).target(OrderStates.INVENTORY_RESERVED)
                .event(OrderEvents.INVENTORY_RESERVED)
                .action(inventoryReservedAction())
            .and()
            .withExternal()
                .source(OrderStates.INVENTORY_RESERVED).target(OrderStates.ORDER_COMPLETED)
                .event(OrderEvents.ORDER_CONFIRMED)
            .and()
            .withExternal()
                .source(OrderStates.ORDER_CREATED).target(OrderStates.ORDER_FAILED)
                .event(OrderEvents.PAYMENT_FAILED)
                .action(compensateOrderAction());
    }

    @Bean
    public Action<OrderStates, OrderEvents> paymentProcessedAction() {
        return context -> {
            // Process payment
            String orderId = context.getMessage().getHeaders().get("orderId", String.class);
            paymentService.processPayment(orderId);
        };
    }

    @Bean
    public Action<OrderStates, OrderEvents> inventoryReservedAction() {
        return context -> {
            // Reserve inventory
            String orderId = context.getMessage().getHeaders().get("orderId", String.class);
            inventoryService.reserveInventory(orderId);
        };
    }

    @Bean
    public Action<OrderStates, OrderEvents> compensateOrderAction() {
        return context -> {
            // Compensating action: cancel order
            String orderId = context.getMessage().getHeaders().get("orderId", String.class);
            orderService.cancelOrder(orderId);
        };
    }
}

@Service
public class OrderSagaService {
    private final StateMachine<OrderStates, OrderEvents> stateMachine;

    public void processOrder(String orderId) {
        Message<OrderEvents> message = MessageBuilder
            .withPayload(OrderEvents.PAYMENT_SUCCESS)
            .setHeader("orderId", orderId)
            .build();

        stateMachine.sendEvent(message);
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

**Java Implementation Examples:**
```java
// Constructor Injection (Recommended)
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

    // Dependencies injected through constructor
    public OrderService(OrderRepository orderRepository, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
    }

    public Order createOrder(OrderRequest request) {
        // Use injected dependencies
        Order order = orderRepository.save(new Order(request));
        paymentService.processPayment(order);
        return order;
    }
}

// Setter Injection
@Service
public class EmailService {
    private EmailProvider emailProvider;

    @Autowired
    public void setEmailProvider(EmailProvider emailProvider) {
        this.emailProvider = emailProvider;
    }
}

// Field Injection (Not recommended for testability)
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
}

// Configuration class
@Configuration
public class AppConfig {
    @Bean
    public OrderService orderService(OrderRepository orderRepository, PaymentService paymentService) {
        return new OrderService(orderRepository, paymentService);
    }
}
```

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

**Java Implementation Example:**
```java
@Service
public class DatabaseConnectionService implements InitializingBean, DisposableBean {

    private Connection connection;

    // 1. Constructor called during instantiation
    public DatabaseConnectionService() {
        System.out.println("1. Constructor called");
    }

    // 2. Dependencies injected via setter
    @Autowired
    public void setDataSource(DataSource dataSource) {
        System.out.println("2. Dependencies injected");
        this.dataSource = dataSource;
    }

    // 3. BeanNameAware implementation
    @Override
    public void setBeanName(String name) {
        System.out.println("3. Bean name set: " + name);
    }

    // 4. BeanFactoryAware implementation
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("4. BeanFactory reference set");
    }

    // 5. @PostConstruct - Pre-initialization
    @PostConstruct
    public void initialize() {
        System.out.println("5. @PostConstruct - Pre-initialization");
        // Initialize connection pool, validate connections, etc.
    }

    // 6. InitializingBean - After properties set
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("6. InitializingBean.afterPropertiesSet()");
        // Additional initialization logic
        this.connection = dataSource.getConnection();
    }

    // 7. Custom init-method (configured in XML or @Bean)
    public void customInit() {
        System.out.println("7. Custom init-method called");
    }

    // 8. Bean ready for use
    public void performDatabaseOperation() {
        System.out.println("8. Bean ready for use - performing operation");
        // Use the connection
    }

    // 9. @PreDestroy - Pre-destruction
    @PreDestroy
    public void cleanup() {
        System.out.println("9. @PreDestroy - Pre-destruction");
        // Close connections, release resources
    }

    // 10. DisposableBean - Destroy
    @Override
    public void destroy() throws Exception {
        System.out.println("10. DisposableBean.destroy()");
        if (connection != null) {
            connection.close();
        }
    }

    // 11. Custom destroy-method
    public void customDestroy() {
        System.out.println("11. Custom destroy-method called");
    }
}

// Configuration
@Configuration
public class AppConfig {
    @Bean(initMethod = "customInit", destroyMethod = "customDestroy")
    public DatabaseConnectionService databaseConnectionService() {
        return new DatabaseConnectionService();
    }
}
```

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

### Q4: Explain Spring Boot auto-configuration.
**Answer:**
Automatically configures Spring application based on dependencies on classpath.

**How it works:**
- Scans classpath for JAR files and classes
- Applies configuration based on found libraries
- Uses `@Conditional` annotations to determine when to apply configuration

**Benefits:**
- Reduces boilerplate configuration
- Convention over configuration
- Faster development

### Q5: What is the difference between `@Component`, `@Service`, `@Repository`, and `@Controller`?
**Answer:**
All are stereotypes for dependency injection, but serve different purposes:

- **@Component**: Generic stereotype for any Spring-managed component
- **@Service**: Indicates service layer component, business logic
- **@Repository**: Indicates data access layer, provides exception translation
- **@Controller**: Indicates web layer component, handles HTTP requests

**Exception Translation (@Repository):**
Translates technology-specific exceptions (SQLException) to Spring's DataAccessException hierarchy.

### Q6: Explain Spring Data JPA.
**Answer:**
Simplifies data access layer implementation using JPA.

**Key Features:**
- **Repository interfaces**: No need to implement basic CRUD operations
- **Query methods**: Generate queries from method names
- **Custom queries**: @Query annotation for complex queries
- **Pagination**: Built-in pagination support

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Basic CRUD operations provided automatically

    // Query method - generates query from method name
    List<User> findByEmail(String email);

    // Custom query
    @Query("SELECT u FROM User u WHERE u.status = :status")
    List<User> findActiveUsers(@Param("status") String status);
}
```

### Q7: What are Spring profiles?
**Answer:**
Way to segregate parts of application configuration and make it available only in certain environments.

```java
@Component
@Profile("dev")
public class DevDataSourceConfig {
    // Development database configuration
}

@Component
@Profile("prod")
public class ProdDataSourceConfig {
    // Production database configuration
}

// Usage in application.properties
spring.profiles.active=dev
```

### Q8: Explain Spring Security.
**Answer:**
Framework for authentication, authorization, and protection against common attacks.

**Key Concepts:**
- **Authentication**: Verifying user identity
- **Authorization**: Determining user permissions
- **Principal**: Currently authenticated user
- **SecurityContext**: Holds authentication information

**Common Features:**
- Basic authentication
- Form-based login
- OAuth2 support
- Method-level security
- CSRF protection

---

## Database & ORM

### Q1: Explain ACID properties.
**Answer:**
- **Atomicity**: All or nothing
- **Consistency**: Database remains in consistent state
- **Isolation**: Concurrent transactions don't interfere
- **Durability**: Committed changes persist

**Java Implementation Example (Spring Transaction):**
```java
@Service
public class BankService {
    private final AccountRepository accountRepository;

    @Transactional
    public void transferMoney(String fromAccountId, String toAccountId, BigDecimal amount) {
        // Atomicity: Either both operations succeed or both fail
        Account fromAccount = accountRepository.findById(fromAccountId)
            .orElseThrow(() -> new AccountNotFoundException("From account not found"));

        Account toAccount = accountRepository.findById(toAccountId)
            .orElseThrow(() -> new AccountNotFoundException("To account not found"));

        // Consistency: Account balances remain valid
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        // Isolation: Concurrent transfers don't interfere
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Durability: Changes persist even if system crashes
    }
}
```

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

**Java Implementation Example (JPA Indexing):**
```java
@Entity
@Table(indexes = {
    @Index(name = "idx_user_email", columnList = "email"),
    @Index(name = "idx_user_status_created", columnList = "status, createdDate")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    // Composite index for queries filtering by status and ordering by creation date
    // @Query("SELECT u FROM User u WHERE u.status = ?1 ORDER BY u.createdDate DESC")
}

// Repository with indexed queries
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Uses idx_user_email index
    Optional<User> findByEmail(String email);

    // Uses idx_user_status_created index
    @Query("SELECT u FROM User u WHERE u.status = :status ORDER BY u.createdDate DESC")
    List<User> findActiveUsersOrderByCreatedDate(@Param("status") UserStatus status);

    // Full table scan (no index)
    List<User> findByFirstName(String firstName);
}
```

### Q3: Explain JPA/Hibernate.
**Answer:**
- **JPA**: Java Persistence API - specification for ORM
- **Hibernate**: Implementation of JPA

**Key Concepts:**
- **Entity**: POJO mapped to database table
- **EntityManager**: Interface for CRUD operations
- **JPQL**: Java Persistence Query Language
- **Caching**: First-level (session) and second-level (application) cache

### Q4: What are database normalization forms?
**Answer:**
**1NF (First Normal Form):**
- Eliminate repeating groups
- Each column contains atomic values
- Each record is unique

**2NF (Second Normal Form):**
- Satisfies 1NF
- No partial dependencies (non-key attributes depend on entire primary key)

**3NF (Third Normal Form):**
- Satisfies 2NF
- No transitive dependencies (non-key attributes don't depend on other non-key attributes)

**BCNF (Boyce-Codd Normal Form):**
- Stronger than 3NF
- Every determinant is a candidate key

### Q5: Explain database indexing strategies.
**Answer:**
**Types of Indexes:**
- **B-Tree**: Most common, good for range queries
- **Hash**: Fast exact matches, not good for ranges
- **Bitmap**: Efficient for low-cardinality columns
- **Full-text**: For text search operations

**Composite Indexes:**
- Index on multiple columns
- Order matters for query optimization
- Can satisfy queries with different column combinations

**Index Maintenance:**
- Updates slow down INSERT/UPDATE/DELETE
- Rebuild periodically for performance
- Monitor index usage and remove unused indexes

### Q6: What are database transactions and isolation levels?
**Answer:**
**Isolation Levels (from lowest to highest isolation):**
- **READ UNCOMMITTED**: Can read uncommitted changes (dirty reads possible)
- **READ COMMITTED**: Only read committed changes (no dirty reads)
- **REPEATABLE READ**: Same query returns same results within transaction
- **SERIALIZABLE**: Transactions execute serially (highest isolation)

**Trade-offs:**
- Higher isolation = Better consistency, worse performance
- Lower isolation = Better performance, potential consistency issues

### Q7: Explain N+1 query problem and solutions.
**Answer:**
**Problem:** ORM executes N additional queries for N related entities.

**Example:**
```java
// N+1 problem
List<Order> orders = entityManager.createQuery("SELECT o FROM Order o").getResultList();
for (Order order : orders) {
    // Each iteration executes a query to load order items
    List<OrderItem> items = order.getItems();
}
```

**Solutions:**
- **Eager fetching**: `@OneToMany(fetch = FetchType.EAGER)`
- **Join fetch**: `SELECT o FROM Order o JOIN FETCH o.items`
- **Batch fetching**: `@BatchSize(size = 10)`
- **Entity graphs**: Define fetch plans

**Java Implementation Examples:**
```java
@Entity
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @BatchSize(size = 10) // Solution 1: Batch fetching
    private List<OrderItem> items;
}

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Solution 2: Join fetch in query
    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.items WHERE o.customer.id = :customerId")
    List<Order> findOrdersWithItemsByCustomerId(@Param("customerId") Long customerId);

    // Solution 3: Entity graph
    @EntityGraph(attributePaths = {"items", "items.product"})
    List<Order> findAll();
}

// Service layer
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    // Bad: N+1 queries
    public List<OrderDTO> getOrdersWithItemsNPlusOne() {
        List<Order> orders = orderRepository.findAll(); // 1 query
        return orders.stream()
            .map(order -> {
                // N queries here (one per order)
                List<OrderItem> items = order.getItems();
                return convertToDTO(order, items);
            })
            .collect(Collectors.toList());
    }

    // Good: Single query with join fetch
    public List<OrderDTO> getOrdersWithItemsOptimized() {
        List<Order> orders = orderRepository.findOrdersWithItemsByCustomerId(customerId); // 1 query
        return orders.stream()
            .map(order -> convertToDTO(order, order.getItems()))
            .collect(Collectors.toList());
    }
}
```

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

**Java Implementation Example:**
```java
@Service
public class UrlShortenerService {
    private final UrlRepository urlRepository;
    private final Cache<String, String> cache;

    public String shortenUrl(String longUrl) {
        String shortUrl = generateShortUrl();
        UrlMapping mapping = new UrlMapping(shortUrl, longUrl, LocalDateTime.now());
        urlRepository.save(mapping);
        cache.put(shortUrl, longUrl);
        return shortUrl;
    }

    public String getOriginalUrl(String shortUrl) {
        // Check cache first
        String cachedUrl = cache.get(shortUrl);
        if (cachedUrl != null) {
            return cachedUrl;
        }

        // Check database
        UrlMapping mapping = urlRepository.findByShortUrl(shortUrl);
        if (mapping != null) {
            cache.put(shortUrl, mapping.getLongUrl());
            return mapping.getLongUrl();
        }

        throw new UrlNotFoundException("Short URL not found: " + shortUrl);
    }

    private String generateShortUrl() {
        // Base62 encoding of timestamp + random
        long timestamp = System.currentTimeMillis();
        int random = ThreadLocalRandom.current().nextInt(1000000);
        long combined = timestamp * 1000000 + random;
        return encodeBase62(combined);
    }

    private String encodeBase62(long num) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(chars.charAt((int) (num % 62)));
            num /= 62;
        }
        return sb.reverse().toString();
    }
}

@RestController
public class UrlController {
    private final UrlShortenerService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shortenUrl(@RequestBody ShortenRequest request) {
        String shortUrl = urlService.shortenUrl(request.getLongUrl());
        return ResponseEntity.ok(new ShortenResponse(shortUrl));
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirect(@PathVariable String shortUrl) {
        String longUrl = urlService.getOriginalUrl(shortUrl);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, longUrl)
                .build();
    }
}
```

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

### Q3: Design a rate limiter.
**Answer:**
**Algorithms:**
- **Token Bucket**: Tokens added at fixed rate, requests consume tokens
- **Leaky Bucket**: Requests processed at fixed rate
- **Fixed Window**: Limit requests per time window
- **Sliding Window**: More accurate than fixed window

**Implementation Considerations:**
- **Storage**: Redis for distributed systems
- **Scalability**: Handle high throughput
- **Accuracy vs Performance**: Trade-offs between precision and speed

**Java Implementation Example (Token Bucket):**
```java
public class TokenBucketRateLimiter {
    private final long capacity; // Maximum tokens
    private final double refillRate; // Tokens per second
    private double tokens;
    private long lastRefillTime;

    public TokenBucketRateLimiter(long capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefillTime = System.nanoTime();
    }

    public synchronized boolean allowRequest() {
        refillTokens();
        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }
        return false;
    }

    private void refillTokens() {
        long now = System.nanoTime();
        double elapsedSeconds = (now - lastRefillTime) / 1_000_000_000.0;
        long tokensToAdd = (long) (elapsedSeconds * refillRate);

        if (tokensToAdd > 0) {
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTime = now;
        }
    }
}

// Usage with Redis for distributed rate limiting
@Service
public class DistributedRateLimiter {
    private final RedisTemplate<String, String> redisTemplate;

    public boolean isAllowed(String userId, int requestsPerMinute) {
        String key = "rate_limit:" + userId;
        long currentTime = System.currentTimeMillis() / 1000; // Current minute

        // Use Redis sorted set to track requests in current window
        redisTemplate.opsForZSet().add(key, String.valueOf(currentTime), currentTime);

        // Remove requests outside the current minute window
        redisTemplate.opsForZSet().removeRangeByScore(key, 0, currentTime - 60);

        Long requestCount = redisTemplate.opsForZSet().size(key);

        // Set expiration for the key
        redisTemplate.expire(key, 60, TimeUnit.SECONDS);

        return requestCount <= requestsPerMinute;
    }
}
```

### Q4: Design a distributed cache.
**Answer:**
**Key Components:**
- **Cache Store**: Redis, Memcached, or in-memory
- **Cache Policies**: LRU, LFU, TTL
- **Consistency**: Cache invalidation strategies
- **Scalability**: Sharding, replication

**Cache Strategies:**
- **Cache-Aside**: Application manages cache
- **Write-Through**: Write to cache and DB together
- **Write-Behind**: Write to cache first, then DB asynchronously

**Challenges:**
- **Cache Miss**: Cold start, cache stampede
- **Data Consistency**: Keeping cache and DB in sync
- **Memory Management**: Eviction policies

**Java Implementation Example (Cache-Aside Pattern):**
```java
@Service
public class UserService {
    private final UserRepository userRepository;
    private final Cache<String, User> cache;

    public User getUserById(String userId) {
        // Cache-Aside: Check cache first
        User cachedUser = cache.get(userId);
        if (cachedUser != null) {
            return cachedUser;
        }

        // Cache miss: Fetch from database
        User user = userRepository.findById(userId);
        if (user != null) {
            // Store in cache with TTL
            cache.put(userId, user, Duration.ofMinutes(10));
        }

        return user;
    }

    public void updateUser(User user) {
        // Update database first
        userRepository.save(user);

        // Invalidate cache to ensure consistency
        cache.invalidate(user.getId());

        // Or update cache if using Write-Through
        // cache.put(user.getId(), user, Duration.ofMinutes(10));
    }
}

// Custom cache implementation with LRU eviction
public class LRUCache<K, V> {
    private final int capacity;
    private final Map<K, V> cache;
    private final LinkedHashSet<K> accessOrder;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.accessOrder = new LinkedHashSet<>();
    }

    public synchronized V get(K key) {
        if (!cache.containsKey(key)) {
            return null;
        }
        // Move to end (most recently used)
        accessOrder.remove(key);
        accessOrder.add(key);
        return cache.get(key);
    }

    public synchronized void put(K key, V value) {
        if (cache.containsKey(key)) {
            accessOrder.remove(key);
        } else if (cache.size() >= capacity) {
            // Remove least recently used
            K lru = accessOrder.iterator().next();
            accessOrder.remove(lru);
            cache.remove(lru);
        }

        cache.put(key, value);
        accessOrder.add(key);
    }

    public synchronized void invalidate(K key) {
        cache.remove(key);
        accessOrder.remove(key);
    }
}
```

### Q5: Design a search engine.
**Answer:**
**Components:**
- **Crawler**: Discovers and downloads web pages
- **Indexer**: Creates inverted index of words to documents
- **Query Processor**: Processes search queries
- **Ranking Algorithm**: Determines result relevance

**Scalability:**
- **Distributed Indexing**: Sharding index across multiple servers
- **Load Balancing**: Distribute queries across servers
- **Caching**: Cache frequent queries and results

**Advanced Features:**
- **PageRank**: Link-based ranking
- **Personalization**: User-specific results
- **Real-time Indexing**: Handle dynamic content

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

### Q4: Implement a thread-safe LRU Cache.
```java
class LRUCache<K, V> {
    private final int capacity;
    private final Map<K, V> cache;
    private final Deque<K> accessOrder;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>(capacity);
        this.accessOrder = new LinkedList<>();
    }

    public synchronized V get(K key) {
        if (!cache.containsKey(key)) {
            return null;
        }
        // Move to front (most recently used)
        accessOrder.remove(key);
        accessOrder.addFirst(key);
        return cache.get(key);
    }

    public synchronized void put(K key, V value) {
        if (cache.containsKey(key)) {
            // Update existing
            accessOrder.remove(key);
        } else if (cache.size() >= capacity) {
            // Remove least recently used
            K lru = accessOrder.removeLast();
            cache.remove(lru);
        }

        cache.put(key, value);
        accessOrder.addFirst(key);
    }
}
```

### Q5: Find the median in a stream of numbers.
```java
class MedianFinder {
    private PriorityQueue<Integer> maxHeap; // Lower half
    private PriorityQueue<Integer> minHeap; // Upper half

    public MedianFinder() {
        maxHeap = new PriorityQueue<>((a, b) -> b - a); // Max heap
        minHeap = new PriorityQueue<>(); // Min heap
    }

    public void addNum(int num) {
        if (maxHeap.isEmpty() || num <= maxHeap.peek()) {
            maxHeap.offer(num);
        } else {
            minHeap.offer(num);
        }

        // Balance heaps
        if (maxHeap.size() > minHeap.size() + 1) {
            minHeap.offer(maxHeap.poll());
        } else if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    public double findMedian() {
        if (maxHeap.size() > minHeap.size()) {
            return maxHeap.peek();
        } else {
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        }
    }
}
```

### Q6: Implement a simple calculator with parentheses.
```java
public int calculate(String s) {
    Stack<Integer> stack = new Stack<>();
    int result = 0;
    int sign = 1;
    int num = 0;

    for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);

        if (Character.isDigit(c)) {
            num = num * 10 + (c - '0');
        } else if (c == '+') {
            result += sign * num;
            num = 0;
            sign = 1;
        } else if (c == '-') {
            result += sign * num;
            num = 0;
            sign = -1;
        } else if (c == '(') {
            stack.push(result);
            stack.push(sign);
            result = 0;
            sign = 1;
        } else if (c == ')') {
            result += sign * num;
            num = 0;
            result *= stack.pop(); // sign
            result += stack.pop(); // previous result
        }
    }

    if (num != 0) result += sign * num;
    return result;
}
```

### Q7: Find all permutations of a string.
```java
public List<String> findPermutations(String str) {
    List<String> result = new ArrayList<>();
    backtrack(str.toCharArray(), 0, result);
    return result;
}

private void backtrack(char[] chars, int start, List<String> result) {
    if (start == chars.length) {
        result.add(new String(chars));
        return;
    }

    Set<Character> used = new HashSet<>();
    for (int i = start; i < chars.length; i++) {
        if (used.contains(chars[i])) continue;

        used.add(chars[i]);
        swap(chars, start, i);
        backtrack(chars, start + 1, result);
        swap(chars, start, i); // backtrack
    }
}

private void swap(char[] chars, int i, int j) {
    char temp = chars[i];
    chars[i] = chars[j];
    chars[j] = temp;
}
```

### Q8: Implement a trie (prefix tree).
```java
class TrieNode {
    Map<Character, TrieNode> children;
    boolean isEndOfWord;

    public TrieNode() {
        children = new HashMap<>();
        isEndOfWord = false;
    }
}

class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }
        node.isEndOfWord = true;
    }

    public boolean search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return false;
            }
            node = node.children.get(c);
        }
        return node.isEndOfWord;
    }

    public boolean startsWith(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return false;
            }
            node = node.children.get(c);
        }
        return true;
    }
}
```

### Q9: Find the longest substring without repeating characters.
```java
public int lengthOfLongestSubstring(String s) {
    Map<Character, Integer> charIndex = new HashMap<>();
    int maxLength = 0;
    int start = 0;

    for (int end = 0; end < s.length(); end++) {
        char c = s.charAt(end);
        if (charIndex.containsKey(c)) {
            start = Math.max(start, charIndex.get(c) + 1);
        }
        charIndex.put(c, end);
        maxLength = Math.max(maxLength, end - start + 1);
    }

    return maxLength;
}
```

### Q10: Implement binary search.
```java
public int binarySearch(int[] nums, int target) {
    int left = 0;
    int right = nums.length - 1;

    while (left <= right) {
        int mid = left + (right - left) / 2;

        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }

    return -1; // Not found
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

### Q6: How do you approach debugging complex issues?
**Answer:**
- **Reproduce the issue**: Create minimal test case
- **Gather information**: Logs, stack traces, system state
- **Divide and conquer**: Isolate components
- **Use debugging tools**: IDE debugger, profilers
- **Check assumptions**: Verify your understanding
- **Ask for help**: Pair debugging, code reviews
- **Document findings**: For future reference

### Q7: Describe a time when you had to refactor legacy code.
**Answer:**
- **Assessment**: Understand current codebase and issues
- **Planning**: Create refactoring plan with small steps
- **Testing**: Ensure comprehensive test coverage before refactoring
- **Incremental changes**: Make small, safe changes
- **Validation**: Run tests after each change
- **Documentation**: Update documentation as needed
- **Team communication**: Keep team informed of changes

### Q8: How do you stay updated with technology trends?
**Answer:**
- **Reading**: Technical blogs, books, documentation
- **Communities**: Stack Overflow, Reddit, GitHub
- **Conferences**: Attend meetups, webinars, conferences
- **Practice**: Personal projects, open source contributions
- **Networking**: Connect with other developers
- **Teaching**: Blog, mentor, or speak about what you learn

### Q9: How do you handle technical disagreements with colleagues?
**Answer:**
- **Listen actively**: Understand their perspective
- **Present facts**: Use data, benchmarks, best practices
- **Find common ground**: Identify shared goals
- **Propose alternatives**: Suggest compromises or alternatives
- **Escalate if needed**: Involve technical lead or manager
- **Learn from discussion**: Even if you disagree, you might learn something

### Q10: What motivates you as a software engineer?
**Answer:**
- **Problem solving**: Enjoy tackling complex challenges
- **Continuous learning**: Technology evolves rapidly
- **Impact**: Building products that help users
- **Collaboration**: Working with talented teams
- **Innovation**: Creating new solutions and improving existing ones
- **Quality**: Pride in writing clean, maintainable code

---

*Remember: In interviews, explain your thought process, ask clarifying questions, and demonstrate problem-solving skills. Practice coding problems on LeetCode/HackerRank and review system design concepts regularly.*