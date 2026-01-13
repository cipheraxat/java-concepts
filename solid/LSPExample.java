// SOLID Principles - Liskov Substitution Principle Example
// Bad Example: Violates LSP
public class RectangleBad {
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

public class SquareBad extends RectangleBad {
    @Override
    public void setWidth(double width) {
        super.setWidth(width);
        super.setHeight(width); // Violates LSP
    }

    @Override
    public void setHeight(double height) {
        super.setWidth(height);
        super.setHeight(height); // Violates LSP
    }
}

// Good Example: Proper inheritance hierarchy
public interface Shape {
    double getArea();
}

public interface RectangleShape extends Shape {
    double getWidth();
    double getHeight();
    void setWidth(double width);
    void setHeight(double height);
}

public interface SquareShape extends Shape {
    double getSide();
    void setSide(double side);
}

public class SimpleRectangle implements RectangleShape {
    private double width;
    private double height;

    public SimpleRectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double getWidth() { return width; }

    @Override
    public double getHeight() { return height; }

    @Override
    public void setWidth(double width) { this.width = width; }

    @Override
    public void setHeight(double height) { this.height = height; }

    @Override
    public double getArea() { return width * height; }
}

public class SimpleSquare implements SquareShape {
    private double side;

    public SimpleSquare(double side) {
        this.side = side;
    }

    @Override
    public double getSide() { return side; }

    @Override
    public void setSide(double side) { this.side = side; }

    @Override
    public double getArea() { return side * side; }
}

// Generic shape processor
public class ShapeProcessor {
    public void processShape(Shape shape) {
        System.out.println("Area: " + shape.getArea());
    }
}