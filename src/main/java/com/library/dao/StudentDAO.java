package com.library.dao;

import com.library.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDAO extends BaseDAO implements DAO<Student> {

    @Override
    public Optional<Student> get(String id) {
        String sql = "SELECT s.*, u.* FROM students s " +
                     "JOIN users u ON s.user_id = u.user_id " +
                     "WHERE s.student_id = ? OR u.user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            handleException("Error getting student", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Student> getAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.*, u.* FROM students s " +
                     "JOIN users u ON s.user_id = u.user_id";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            handleException("Error getting all students", e);
        }
        return students;
    }

    @Override
    public void save(Student student) {
        try {
            connection.setAutoCommit(false); // Start transaction
            
            // Save to users table
            String userSql = "INSERT INTO users (user_id, name, email, phone_number, address, user_type) " +
                             "VALUES (?, ?, ?, ?, ?, 'Student')";
            try (PreparedStatement userStmt = connection.prepareStatement(userSql)) {
                userStmt.setString(1, student.getUserId());
                userStmt.setString(2, student.getName());
                userStmt.setString(3, student.getEmail());
                userStmt.setString(4, student.getPhoneNumber());
                userStmt.setString(5, student.getAddress());
                userStmt.executeUpdate();
            }

            // Save to students table
            String studentSql = "INSERT INTO students (student_id, department, user_id) " +
                                "VALUES (?, ?, ?)";
            try (PreparedStatement studentStmt = connection.prepareStatement(studentSql)) {
                studentStmt.setString(1, student.getStudentId());
                studentStmt.setString(2, student.getDepartment());
                studentStmt.setString(3, student.getUserId());
                studentStmt.executeUpdate();
            }
            
            connection.commit();
        } catch (SQLException e) {
            rollbackTransaction();
            handleException("Error saving student", e);
        } finally {
            resetAutoCommit();
        }
    }

    @Override
    public void update(Student student) {
        try {
            connection.setAutoCommit(false);
            
            // Update users table
            String userSql = "UPDATE users SET name = ?, email = ?, phone_number = ?, address = ? " +
                             "WHERE user_id = ?";
            try (PreparedStatement userStmt = connection.prepareStatement(userSql)) {
                userStmt.setString(1, student.getName());
                userStmt.setString(2, student.getEmail());
                userStmt.setString(3, student.getPhoneNumber());
                userStmt.setString(4, student.getAddress());
                userStmt.setString(5, student.getUserId());
                userStmt.executeUpdate();
            }

            // Update students table
            String studentSql = "UPDATE students SET department = ? " +
                               "WHERE student_id = ?";
            try (PreparedStatement studentStmt = connection.prepareStatement(studentSql)) {
                studentStmt.setString(1, student.getDepartment());
                studentStmt.setString(2, student.getStudentId());
                studentStmt.executeUpdate();
            }
            
            connection.commit();
        } catch (SQLException e) {
            rollbackTransaction();
            handleException("Error updating student", e);
        } finally {
            resetAutoCommit();
        }
    }

    @Override
    public void delete(Student student) {
        try {
            connection.setAutoCommit(false);
            
            // Delete from students table
            String studentSql = "DELETE FROM students WHERE student_id = ?";
            try (PreparedStatement studentStmt = connection.prepareStatement(studentSql)) {
                studentStmt.setString(1, student.getStudentId());
                studentStmt.executeUpdate();
            }

            // Delete from users table
            String userSql = "DELETE FROM users WHERE user_id = ?";
            try (PreparedStatement userStmt = connection.prepareStatement(userSql)) {
                userStmt.setString(1, student.getUserId());
                userStmt.executeUpdate();
            }
            
            connection.commit();
        } catch (SQLException e) {
            rollbackTransaction();
            handleException("Error deleting student", e);
        } finally {
            resetAutoCommit();
        }
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setUserId(rs.getString("user_id"));
        student.setName(rs.getString("name"));
        student.setEmail(rs.getString("email"));
        student.setPhoneNumber(rs.getString("phone_number"));
        student.setAddress(rs.getString("address"));
        student.setStudentId(rs.getString("student_id"));
        student.setDepartment(rs.getString("department"));
        return student;
    }
    
    // Helper methods for transaction management
    private void rollbackTransaction() {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException ex) {
            System.err.println("Rollback failed: " + ex.getMessage());
        }
    }
    
    private void resetAutoCommit() {
        try {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            System.err.println("Reset auto-commit failed: " + ex.getMessage());
        }
    }
    
    private void handleException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
        throw new RuntimeException("Database error", e);
    }
}