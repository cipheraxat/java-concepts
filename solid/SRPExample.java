// SOLID Principles - Single Responsibility Principle Example
// Bad Example: Employee class violates SRP
public class EmployeeBad {
    private String name;
    private double salary;

    public EmployeeBad(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    // Responsibility 1: Data persistence
    public void saveToDatabase(Connection conn) throws SQLException {
        String sql = "INSERT INTO employees (name, salary) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, this.name);
            stmt.setDouble(2, this.salary);
            stmt.executeUpdate();
        }
    }

    // Responsibility 2: Business logic
    public double calculateBonus() {
        return this.salary * 0.1;
    }

    // Responsibility 3: Reporting
    public String generateReport() {
        return String.format("Employee: %s, Salary: %.2f, Bonus: %.2f",
                           this.name, this.salary, calculateBonus());
    }
}

// Good Example: Separated responsibilities
public record Employee(String name, double salary) {
}

// Responsibility 1: Data persistence
public interface EmployeeRepository {
    void save(Employee employee);
    Optional<Employee> findByName(String name);
    List<Employee> findAll();
}

public class DatabaseEmployeeRepository implements EmployeeRepository {
    private final DataSource dataSource;

    public DatabaseEmployeeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Employee employee) {
        String sql = "INSERT INTO employees (name, salary) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, employee.name());
            stmt.setDouble(2, employee.salary());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save employee", e);
        }
    }

    @Override
    public Optional<Employee> findByName(String name) {
        // Implementation omitted for brevity
        return Optional.empty();
    }

    @Override
    public List<Employee> findAll() {
        // Implementation omitted for brevity
        return List.of();
    }
}

// Responsibility 2: Business logic
public class PayrollService {
    public double calculateBonus(Employee employee) {
        return employee.salary() * 0.1;
    }

    public List<PayrollRecord> calculatePayroll(List<Employee> employees) {
        return employees.stream()
                .map(emp -> new PayrollRecord(emp, calculateBonus(emp)))
                .toList();
    }
}

// Responsibility 3: Reporting
public class EmployeeReportGenerator {
    public String generateReport(Employee employee, double bonus) {
        return String.format("Employee: %s, Salary: %.2f, Bonus: %.2f",
                           employee.name(), employee.salary(), bonus);
    }

    public String generateBulkReport(List<PayrollRecord> records) {
        return records.stream()
                .map(record -> generateReport(record.employee(), record.bonus()))
                .collect(Collectors.joining("\n"));
    }
}

// Supporting classes
public record PayrollRecord(Employee employee, double bonus) {
}

// Usage example
public class PayrollSystem {
    private final EmployeeRepository repository;
    private final PayrollService payrollService;
    private final EmployeeReportGenerator reportGenerator;

    public PayrollSystem(EmployeeRepository repository,
                        PayrollService payrollService,
                        EmployeeReportGenerator reportGenerator) {
        this.repository = repository;
        this.payrollService = payrollService;
        this.reportGenerator = reportGenerator;
    }

    public void processPayroll(List<Employee> employees) {
        employees.forEach(repository::save);
        var payrollRecords = payrollService.calculatePayroll(employees);
        var report = reportGenerator.generateBulkReport(payrollRecords);
        System.out.println(report);
    }
}