package com.library.dao;

import com.library.database.DatabaseConnection;
import com.library.model.Employee;
import com.library.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDAO implements DAO<Employee> {
    private final Connection connection;

    public EmployeeDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public Optional<Employee> get(String id) {
        try {
            // First try to find by employee_id
            String sql = "SELECT e.*, u.* FROM employees e " +
                         "JOIN users u ON e.user_id = u.user_id " +
                         "WHERE e.employee_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Employee employee = mapResultSetToEmployee(rs);
                return Optional.of(employee);
            }
            
            // Then try to find by user_id
            sql = "SELECT e.*, u.* FROM employees e " +
                  "JOIN users u ON e.user_id = u.user_id " +
                  "WHERE e.user_id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                Employee employee = mapResultSetToEmployee(rs);
                return Optional.of(employee);
            }
        } catch (SQLException e) {
            System.err.println("Error getting employee: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();
        try {
            String sql = "SELECT e.*, u.* FROM employees e " +
                         "JOIN users u ON e.user_id = u.user_id";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            
            while (rs.next()) {
                Employee employee = mapResultSetToEmployee(rs);
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all employees: " + e.getMessage());
        }
        return employees;
    }

    @Override
    public void save(Employee employee) {
        try {
            // First insert into users table if it doesn't exist
            String checkUserSql = "SELECT COUNT(*) FROM users WHERE user_id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkUserSql);
            checkStmt.setString(1, employee.getUserId());
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) == 0) {
                String userSql = "INSERT INTO users (user_id, name, email, phone_number, address, user_type) " +
                                 "VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement userStmt = connection.prepareStatement(userSql);
                userStmt.setString(1, employee.getUserId());
                userStmt.setString(2, employee.getName());
                userStmt.setString(3, employee.getEmail());
                userStmt.setString(4, employee.getPhoneNumber());
                userStmt.setString(5, employee.getAddress());
                userStmt.setString(6, employee.getUserType());
                userStmt.executeUpdate();
            }
            
            // Then insert into employees table
            String employeeSql = "INSERT INTO employees (employee_id, position, date_hired, salary, user_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement employeeStmt = connection.prepareStatement(employeeSql);
            employeeStmt.setString(1, employee.getEmployeeId());
            employeeStmt.setString(2, employee.getPosition());
            
            if (employee.getDateHired() != null) {
                employeeStmt.setString(3, employee.getDateHired().toString());
            } else {
                employeeStmt.setNull(3, Types.VARCHAR);
            }
            
            employeeStmt.setDouble(4, employee.getSalary());
            employeeStmt.setString(5, employee.getUserId());
            employeeStmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving employee: " + e.getMessage());
        }
    }

    @Override
    public void update(Employee employee) {
        try {
            // Update users table
            String userSql = "UPDATE users SET name = ?, email = ?, phone_number = ?, address = ? WHERE user_id = ?";
            PreparedStatement userStmt = connection.prepareStatement(userSql);
            userStmt.setString(1, employee.getName());
            userStmt.setString(2, employee.getEmail());
            userStmt.setString(3, employee.getPhoneNumber());
            userStmt.setString(4, employee.getAddress());
            userStmt.setString(5, employee.getUserId());
            userStmt.executeUpdate();
            
            // Update employees table
            String employeeSql = "UPDATE employees SET position = ?, date_hired = ?, salary = ? WHERE employee_id = ?";
            PreparedStatement employeeStmt = connection.prepareStatement(employeeSql);
            employeeStmt.setString(1, employee.getPosition());
            
            if (employee.getDateHired() != null) {
                employeeStmt.setString(2, employee.getDateHired().toString());
            } else {
                employeeStmt.setNull(2, Types.VARCHAR);
            }
            
            employeeStmt.setDouble(3, employee.getSalary());
            employeeStmt.setString(4, employee.getEmployeeId());
            employeeStmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
        }
    }

    @Override
    public void delete(Employee employee) {
        try {
            // First delete from employees table
            String employeeSql = "DELETE FROM employees WHERE employee_id = ?";
            PreparedStatement employeeStmt = connection.prepareStatement(employeeSql);
            employeeStmt.setString(1, employee.getEmployeeId());
            employeeStmt.executeUpdate();
            
            // Then delete from users table
            String userSql = "DELETE FROM users WHERE user_id = ?";
            PreparedStatement userStmt = connection.prepareStatement(userSql);
            userStmt.setString(1, employee.getUserId());
            userStmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
        }
    }

    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setUserId(rs.getString("user_id"));
        employee.setEmployeeId(rs.getString("employee_id"));
        employee.setName(rs.getString("name"));
        employee.setEmail(rs.getString("email"));
        employee.setPhoneNumber(rs.getString("phone_number"));
        employee.setAddress(rs.getString("address"));
        employee.setPosition(rs.getString("position"));
        
        String dateHiredStr = rs.getString("date_hired");
        if (dateHiredStr != null) {
            employee.setDateHired(LocalDate.parse(dateHiredStr));
        }
        
        employee.setSalary(rs.getDouble("salary"));
        return employee;
    }
}