# SOLID Design Principles in Java

## Introduction

SOLID is an acronym for five design principles in object-oriented programming that help developers create more maintainable, understandable, and flexible software. These principles were introduced by Robert C. Martin (Uncle Bob) and are fundamental to clean code practices.

- **S** - Single Responsibility Principle
- **O** - Open-Closed Principle  
- **L** - Liskov Substitution Principle
- **I** - Interface Segregation Principle
- **D** - Dependency Inversion Principle

Each principle addresses specific issues in software design and promotes better code organization, testability, and extensibility.

## 1. Single Responsibility Principle (SRP)

### Definition
A class should have only one reason to change, meaning it should have only one responsibility or job.

### Explanation
This principle prevents classes from becoming "god objects" that do too many things. When a class has multiple responsibilities, changes in one area can affect others, leading to fragile code. SRP encourages separation of concerns.

### Java Example

```java
// Bad Example: Violates SRP - handles both data persistence and reporting
public class Employee {
    private String name;
    private double salary;
    
    // Constructor and getters/setters omitted for brevity
    
    // Responsibility 1: Data persistence
    public void saveToDatabase() {
        // Database save logic
        System.out.println("Saving employee to database");
    }
    
    // Responsibility 2: Reporting
    public void generateReport() {
        // Report generation logic
        System.out.println("Generating employee report");
    }
}

// Good Example: Separated responsibilities
public class Employee {
    private String name;
    private double salary;
    
    // Constructor and getters/setters omitted for brevity
}

// Responsibility 1: Data persistence
public class EmployeeRepository {
    public void save(Employee employee) {
        // Database save logic
        System.out.println("Saving employee to database");
    }
}

// Responsibility 2: Reporting
public class EmployeeReportGenerator {
    public void generateReport(Employee employee) {
        // Report generation logic
        System.out.println("Generating employee report");
    }
}
```

### Benefits
- Easier to understand and maintain
- Better testability
- Reduced coupling between different concerns
- Changes are localized to specific classes

### Interview Questions
- Why is SRP important for maintainability?
- How would you refactor a class that violates SRP?
- Can you give an example from your experience where applying SRP improved code quality?

## 2. Open-Closed Principle (OCP)

### Definition
Software entities (classes, modules, functions) should be open for extension but closed for modification.

### Explanation
This means you should be able to add new functionality without changing existing code. OCP promotes the use of abstraction and polymorphism to allow extension through inheritance or composition.

### Java Example

```java
// Bad Example: Violates OCP - requires modification for new shapes
public class AreaCalculator {
    public double calculateArea(Object shape) {
        if (shape instanceof Rectangle) {
            Rectangle rect = (Rectangle) shape;
            return rect.getWidth() * rect.getHeight();
        } else if (shape instanceof Circle) {
            Circle circle = (Circle) shape;
            return Math.PI * circle.getRadius() * circle.getRadius();
        }
        // Adding a new shape requires modifying this method
        return 0;
    }
}

// Good Example: Follows OCP - extensible without modification
public interface Shape {
    double calculateArea();
}

public class Rectangle implements Shape {
    private double width;
    private double height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double calculateArea() {
        return width * height;
    }
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

// New shape can be added without modifying existing code
public class Triangle implements Shape {
    private double base;
    private double height;
    
    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }
    
    @Override
    public double calculateArea() {
        return 0.5 * base * height;
    }
}

public class AreaCalculator {
    public double calculateArea(Shape shape) {
        return shape.calculateArea();
    }
    
    // Can calculate area for any shape that implements Shape interface
    public double calculateTotalArea(List<Shape> shapes) {
        return shapes.stream()
                .mapToDouble(Shape::calculateArea)
                .sum();
    }
}
```

### Benefits
- Reduces risk of introducing bugs in existing code
- Promotes code reuse
- Easier to add new features
- Better maintainability

### Interview Questions
- How does OCP relate to polymorphism?
- What's the difference between inheritance and composition in the context of OCP?
- How would you design a system that needs to support multiple payment methods?

## 3. Liskov Substitution Principle (LSP)

### Definition
Objects of a superclass should be replaceable with objects of its subclasses without affecting the correctness of the program.

### Explanation
This principle ensures that inheritance hierarchies are designed correctly. A subclass should behave in a way that doesn't break the expectations set by the superclass. LSP is about behavioral subtyping, not just structural subtyping.

### Java Example

```java
// Bad Example: Violates LSP - Square breaks Rectangle's behavior
public class Rectangle {
    protected double width;
    protected double height;
    
    public void setWidth(double width) {
        this.width = width;
    }
    
    public void setHeight(double height) {
        this.height = height;
    }
    
    public double getArea() {
        return width * height;
    }
}

public class Square extends Rectangle {
    @Override
    public void setWidth(double width) {
        super.setWidth(width);
        super.setHeight(width); // Violates LSP - changes height when setting width
    }
    
    @Override
    public void setHeight(double height) {
        super.setWidth(height); // Violates LSP - changes width when setting height
        super.setHeight(height);
    }
}

// Client code that expects Rectangle behavior
public class AreaCalculator {
    public void printArea(Rectangle rect) {
        rect.setWidth(5);
        rect.setHeight(10);
        System.out.println("Area: " + rect.getArea()); // Expected: 50, but Square gives 25
    }
}

// Good Example: Proper inheritance hierarchy
public interface Shape {
    double getArea();
}

public class Rectangle implements Shape {
    protected double width;
    protected double height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    public void setWidth(double width) {
        this.width = width;
    }
    
    public void setHeight(double height) {
        this.height = height;
    }
    
    @Override
    public double getArea() {
        return width * height;
    }
}

public class Square implements Shape {
    private double side;
    
    public Square(double side) {
        this.side = side;
    }
    
    public void setSide(double side) {
        this.side = side;
    }
    
    @Override
    public double getArea() {
        return side * side;
    }
}

// Both can be used interchangeably where Shape is expected
public class ShapeProcessor {
    public void processShape(Shape shape) {
        System.out.println("Area: " + shape.getArea());
    }
}
```

### Benefits
- Ensures proper inheritance hierarchies
- Prevents unexpected behavior in polymorphic code
- Improves code reliability
- Makes testing easier

### Interview Questions
- How can you identify LSP violations in code?
- What's the relationship between LSP and design by contract?
- Can you explain the Rectangle-Square problem?

## 4. Interface Segregation Principle (ISP)

### Definition
Clients should not be forced to depend on interfaces they do not use.

### Explanation
This principle advises against "fat" interfaces that contain methods that not all implementing classes need. Instead, create smaller, more specific interfaces that are relevant to specific clients.

### Java Example

```java
// Bad Example: Violates ISP - fat interface
public interface Worker {
    void work();
    void eat();
    void sleep();
}

// Human worker needs all methods
public class HumanWorker implements Worker {
    @Override
    public void work() {
        System.out.println("Human is working");
    }
    
    @Override
    public void eat() {
        System.out.println("Human is eating");
    }
    
    @Override
    public void sleep() {
        System.out.println("Human is sleeping");
    }
}

// Robot worker doesn't need eat() and sleep()
public class RobotWorker implements Worker {
    @Override
    public void work() {
        System.out.println("Robot is working");
    }
    
    @Override
    public void eat() {
        // Robots don't eat - forced to implement unnecessary method
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void sleep() {
        // Robots don't sleep - forced to implement unnecessary method
        throw new UnsupportedOperationException();
    }
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

public class HumanWorker implements Workable, Eatable, Sleepable {
    @Override
    public void work() {
        System.out.println("Human is working");
    }
    
    @Override
    public void eat() {
        System.out.println("Human is eating");
    }
    
    @Override
    public void sleep() {
        System.out.println("Human is sleeping");
    }
}

public class RobotWorker implements Workable {
    @Override
    public void work() {
        System.out.println("Robot is working");
    }
}

// Specialized robot that can be upgraded
public class AdvancedRobotWorker implements Workable, Eatable {
    @Override
    public void work() {
        System.out.println("Advanced robot is working");
    }
    
    @Override
    public void eat() {
        System.out.println("Advanced robot is recharging");
    }
}
```

### Benefits
- Reduces coupling between classes
- Makes interfaces more focused and cohesive
- Easier to implement and test
- Promotes better code organization

### Interview Questions
- How does ISP relate to the Single Responsibility Principle?
- When should you consider splitting an interface?
- Can you give examples of ISP violations in popular Java APIs?

## 5. Dependency Inversion Principle (DIP)

### Definition
High-level modules should not depend on low-level modules. Both should depend on abstractions. Abstractions should not depend on details. Details should depend on abstractions.

### Explanation
This principle promotes loose coupling by ensuring that classes depend on interfaces or abstract classes rather than concrete implementations. It enables easier testing and flexibility in changing implementations.

### Java Example

```java
// Bad Example: Violates DIP - high-level module depends on low-level module
public class EmailService {
    private GmailMailer mailer;
    
    public EmailService() {
        this.mailer = new GmailMailer(); // Direct dependency on concrete class
    }
    
    public void sendEmail(String message) {
        mailer.send(message);
    }
}

public class GmailMailer {
    public void send(String message) {
        System.out.println("Sending via Gmail: " + message);
    }
}

// High-level module (NotificationService) depends on low-level module (EmailService)
public class NotificationService {
    private EmailService emailService;
    
    public NotificationService() {
        this.emailService = new EmailService();
    }
    
    public void notify(String message) {
        emailService.sendEmail(message);
    }
}

// Good Example: Follows DIP - depends on abstractions
public interface MessageService {
    void sendMessage(String message);
}

public class EmailService implements MessageService {
    private Mailer mailer;
    
    public EmailService(Mailer mailer) {
        this.mailer = mailer; // Depends on abstraction
    }
    
    @Override
    public void sendMessage(String message) {
        mailer.send(message);
    }
}

public interface Mailer {
    void send(String message);
}

public class GmailMailer implements Mailer {
    @Override
    public void send(String message) {
        System.out.println("Sending via Gmail: " + message);
    }
}

public class OutlookMailer implements Mailer {
    @Override
    public void send(String message) {
        System.out.println("Sending via Outlook: " + message);
    }
}

// High-level module depends on abstraction
public class NotificationService {
    private MessageService messageService;
    
    public NotificationService(MessageService messageService) {
        this.messageService = messageService; // Dependency injection
    }
    
    public void notify(String message) {
        messageService.sendMessage(message);
    }
}

// Usage with dependency injection
public class Main {
    public static void main(String[] args) {
        // Can easily switch implementations
        Mailer gmailMailer = new GmailMailer();
        MessageService emailService = new EmailService(gmailMailer);
        NotificationService notificationService = new NotificationService(emailService);
        
        notificationService.notify("Hello World!");
        
        // Easy to change to Outlook
        Mailer outlookMailer = new OutlookMailer();
        MessageService outlookService = new EmailService(outlookMailer);
        NotificationService outlookNotification = new NotificationService(outlookService);
        
        outlookNotification.notify("Hello from Outlook!");
    }
}
```

### Benefits
- Reduces coupling between modules
- Enables easier testing with mocks/stubs
- Allows for runtime flexibility in implementations
- Promotes better separation of concerns

### Interview Questions
- How does DIP enable better testing?
- What's the difference between DIP and Dependency Injection?
- How would you refactor legacy code to follow DIP?

## Conclusion

SOLID principles provide a foundation for writing clean, maintainable, and extensible code. While they may seem abstract at first, applying them consistently leads to:

- More modular and testable code
- Easier maintenance and refactoring
- Better team collaboration
- Reduced technical debt

Remember that SOLID principles should be applied judiciously - over-engineering can be as harmful as not following them. Use them as guidelines to make informed design decisions.

## Common Interview Scenarios

1. **Refactoring Exercise**: Given a piece of code that violates multiple SOLID principles, explain how you'd refactor it.

2. **Design Discussion**: Design a system (e.g., e-commerce platform, logging system) and explain how SOLID principles guide your design decisions.

3. **Trade-off Analysis**: Discuss when it might be acceptable to violate a SOLID principle and what the consequences would be.

4. **Real-world Application**: Share experiences where applying SOLID principles solved real problems in your projects.

Practice explaining these principles with concrete examples and be prepared to discuss their benefits and potential drawbacks in different contexts.