# Java Concepts Repository

This repository contains comprehensive Java interview preparation materials covering SOLID principles, multithreading & concurrency, and design patterns.

## Structure

```
java-concepts/
├── README.md                           # This file
├── SOLID_Principles.md                 # Basic SOLID principles guide
├── SOLID_Study_Guide_SDE2.md          # Advanced SDE2-level SOLID guide
├── Multithreading_Concurrency_Interview_Guide.md  # Deep-dive multithreading guide
├── Design_Patterns_Java_SDE2.md        # Design patterns guide
├── solid/                              # SOLID principles Java examples
│   ├── SRPExample.java                # Single Responsibility Principle
│   ├── OCPExample.java                # Open-Closed Principle
│   ├── LSPExample.java                # Liskov Substitution Principle
│   ├── ISPExample.java                # Interface Segregation Principle
│   └── DIPExample.java                # Dependency Inversion Principle
├── multithreading/                     # Multithreading examples
│   └── JMMExamples.java               # Java Memory Model examples
└── patterns/                           # Design patterns examples
    ├── SingletonExample.java          # Singleton pattern
    ├── BuilderExample.java            # Builder pattern
    ├── FactoryMethodExample.java      # Factory Method pattern
    ├── AdapterExample.java            # Adapter pattern
    ├── DecoratorExample.java          # Decorator pattern
    ├── FacadeExample.java             # Facade pattern
    ├── ObserverExample.java           # Observer pattern
    ├── StrategyExample.java           # Strategy pattern
    └── ChainOfResponsibilityExample.java  # Chain of Responsibility pattern
```

## How to Use

1. **Start with Documentation**: Read the `.md` files to understand concepts and interview scenarios
2. **Study Code Examples**: Examine the Java files for production-grade implementations
3. **Practice Implementation**: Try implementing the patterns yourself before looking at the code
4. **Interview Preparation**: Focus on the "Interview Gotchas" sections in each guide

## Topics Covered

### SOLID Principles
- **SRP**: Single Responsibility Principle with repository/service separation
- **OCP**: Open-Closed Principle with polymorphism and interfaces
- **LSP**: Liskov Substitution Principle with proper inheritance
- **ISP**: Interface Segregation Principle with role interfaces
- **DIP**: Dependency Inversion Principle with dependency injection

### Multithreading & Concurrency
- Java Memory Model (volatile, synchronized, happens-before)
- Thread safety patterns
- Concurrent collections usage

### Design Patterns
- **Creational**: Singleton (thread-safe), Builder, Factory Method
- **Structural**: Adapter, Decorator, Facade
- **Behavioral**: Observer, Strategy, Chain of Responsibility

## Prerequisites

- Java 17+ (some examples use modern features like records and sealed classes)
- Basic understanding of OOP concepts
- Familiarity with Java syntax

## Running the Examples

Each Java file contains a `main` method for demonstration. Compile and run individual files:

```bash
javac solid/SRPExample.java
java -cp . solid.SRPExample
```

## Interview Tips

- Be prepared to discuss trade-offs of each pattern
- Understand when NOT to use a pattern
- Know common implementation pitfalls
- Practice explaining patterns with real-world examples

## Contributing

This repository is for interview preparation. Feel free to suggest improvements or additional examples.

## License

This project is for educational purposes.