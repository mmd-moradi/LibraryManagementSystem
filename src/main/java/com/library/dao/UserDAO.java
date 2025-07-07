package com.library.dao;

import com.library.model.Employee;
import com.library.model.Student;
import com.library.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO extends BaseDAO {
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            
            StudentDAO studentDAO = new StudentDAO();
            users.addAll(studentDAO.getAll());
            
            
            EmployeeDAO employeeDAO = new EmployeeDAO();
            users.addAll(employeeDAO.getAll());
            
        } catch (Exception e) {
            System.err.println("Error getting all users: " + e.getMessage());
        }
        return users;
    }
    
    public Optional<User> getUserById(String userId) {
        try {
            
            StudentDAO studentDAO = new StudentDAO();
            Optional<Student> student = studentDAO.get(userId);
            if (student.isPresent()) return Optional.of(student.get());
            
            
            EmployeeDAO employeeDAO = new EmployeeDAO();
            Optional<Employee> employee = employeeDAO.get(userId);
            if (employee.isPresent()) return Optional.of(employee.get());
            
        } catch (Exception e) {
            System.err.println("Error getting user: " + e.getMessage());
        }
        return Optional.empty();
    }
    
    public void addUser(User user) {
        if (user instanceof Student) {
            new StudentDAO().save((Student) user);
        } else if (user instanceof Employee) {
            new EmployeeDAO().save((Employee) user);
        }
    }
    
    public void updateUser(User user) {
        if (user instanceof Student) {
            new StudentDAO().update((Student) user);
        } else if (user instanceof Employee) {
            new EmployeeDAO().update((Employee) user);
        }
    }
    
    public void deleteUser(User user) {
        if (user instanceof Student) {
            new StudentDAO().delete((Student) user);
        } else if (user instanceof Employee) {
            new EmployeeDAO().delete((Employee) user);
        }
    }

    public int getUserCount() {
      int count = 0;
      try {
        
        StudentDAO studentDAO = new StudentDAO();
        count += studentDAO.getAll().size();
        
        
        EmployeeDAO employeeDAO = new EmployeeDAO();
        count += employeeDAO.getAll().size();
        
      } catch (Exception e) {
        System.err.println("Error counting users: " + e.getMessage());
      }
      return count;
    }
}