// SOLID Principles - Open-Closed Principle Example
// Bad Example: Violates OCP
public class AreaCalculatorBad {
    public double calculateArea(Object shape) {
        if (shape instanceof Rectangle) {
            Rectangle rect = (Rectangle) shape;
            return rect.getWidth() * rect.getHeight();
        } else if (shape instanceof Circle) {
            Circle circle = (Circle) shape;
            return Math.PI * Math.pow(circle.getRadius(), 2);
        } else if (shape instanceof Triangle) {
            Triangle triangle = (Triangle) shape;
            return 0.5 * triangle.getBase() * triangle.getHeight();
        }
        throw new IllegalArgumentException("Unsupported shape");
    }
}

// Supporting classes for bad example
public class Rectangle {
    private double width;
    private double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double getWidth() { return width; }
    public double getHeight() { return height; }
}

public class Circle {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    public double getRadius() { return radius; }
}

public class Triangle {
    private double base;
    private double height;

    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    public double getBase() { return base; }
    public double getHeight() { return height; }
}

// Good Example: Follows OCP
public interface Shape {
    double calculateArea();
}

public record RectangleRecord(double width, double height) implements Shape {
    @Override
    public double calculateArea() {
        return width * height;
    }
}

public record CircleRecord(double radius) implements Shape {
    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}

public record TriangleRecord(double base, double height) implements Shape {
    @Override
    public double calculateArea() {
        return 0.5 * base * height;
    }
}

// New shape: Easy extension without modification
public record Square(double side) implements Shape {
    @Override
    public double calculateArea() {
        return side * side;
    }
}

public class AreaCalculator {
    public double calculateArea(Shape shape) {
        return shape.calculateArea();
    }

    public double calculateTotalArea(List<Shape> shapes) {
        return shapes.stream()
                .mapToDouble(Shape::calculateArea)
                .sum();
    }

    public double calculateWithFilter(List<Shape> shapes,
                                    Predicate<Shape> filter,
                                    Function<Shape, Double> calculator) {
        return shapes.stream()
                .filter(filter)
                .mapToDouble(calculator::apply)
                .sum();
    }
}