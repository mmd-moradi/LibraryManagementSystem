package com.library.util;

import com.library.database.DatabaseConnection;
import com.library.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestDataGenerator {
    private Connection connection;
    
    public TestDataGenerator() {
        this.connection = DatabaseConnection.getConnection();
        DatabaseConnection.initializeDatabase(); // Initialize tables
    }
    
    public void generateTestData() {
        try {
            // Clean up any existing data
            clearExistingData();
            
            // Generate data
            generateUsers();
            generateBooks();
            generateAccounts();
            linkBooksToStudents();
            
            System.out.println("Test data generation complete!");
        } catch (Exception e) {
            System.err.println("Error generating test data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void clearExistingData() {
        try {
            // Clear tables in reverse order of dependencies
            connection.createStatement().execute("DELETE FROM books");
            connection.createStatement().execute("DELETE FROM librarians");
            connection.createStatement().execute("DELETE FROM accounts");
            connection.createStatement().execute("DELETE FROM employees");
            connection.createStatement().execute("DELETE FROM students");
            connection.createStatement().execute("DELETE FROM users");
            System.out.println("Cleared existing data");
        } catch (Exception e) {
            System.err.println("Error clearing data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void generateUsers() {
        try {
            // Insert Student 1
            insertUser("U001", "João Silva", "joao.silva@example.com", "(11) 98765-4321", 
                     "Av. Paulista, 123 - São Paulo, SP", "Student");
            insertStudent("S001", "Ciência da Computação", "U001");
            
            // Insert Student 2
            insertUser("U002", "Maria Oliveira", "maria.oliveira@example.com", "(11) 91234-5678", 
                     "Rua Augusta, 456 - São Paulo, SP", "Student");
            insertStudent("S002", "Engenharia", "U002");
            
            // Insert Librarian
            insertUser("U003", "Carlos Pereira", "carlos.pereira@library.com", "(11) 97777-8888", 
                     "Rua dos Bibliotecários, 789 - São Paulo, SP", "Librarian");
            insertEmployee("E001", "Bibliotecário Chefe", "2020-03-15", 5000.0, "U003");
            insertLibrarian("Literatura Brasileira", "E001");
            
            System.out.println("Users generated successfully");
        } catch (Exception e) {
            System.err.println("Error generating users: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void insertUser(String userId, String name, String email, String phone, 
                            String address, String userType) throws Exception {
        String sql = "INSERT INTO users (user_id, name, email, phone_number, address, user_type) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.setString(5, address);
            stmt.setString(6, userType);
            stmt.executeUpdate();
        }
    }
    
    private void insertStudent(String studentId, String department, String userId) throws Exception {
        String sql = "INSERT INTO students (student_id, department, user_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            stmt.setString(2, department);
            stmt.setString(3, userId);
            stmt.executeUpdate();
        }
    }
    
    private void insertEmployee(String employeeId, String position, String dateHired, 
                              double salary, String userId) throws Exception {
        String sql = "INSERT INTO employees (employee_id, position, date_hired, salary, user_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employeeId);
            stmt.setString(2, position);
            stmt.setString(3, dateHired);
            stmt.setDouble(4, salary);
            stmt.setString(5, userId);
            stmt.executeUpdate();
        }
    }
    
    private void insertLibrarian(String specialization, String employeeId) throws Exception {
        String sql = "INSERT INTO librarians (specialization, employee_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, specialization);
            stmt.setString(2, employeeId);
            stmt.executeUpdate();
        }
    }
    
    private void generateBooks() {
        try {
            // Book 1
            insertBook("B001", "O Grande Gatsby", "F. Scott Fitzgerald", "9788525056009", 
                      "Ficção", 1925, "AVAILABLE", null, null, null);
            
            // Book 2
            insertBook("B002", "O Sol é para Todos", "Harper Lee", "9788501116598", 
                      "Ficção", 1960, "AVAILABLE", null, null, null);
            
            // Book 3
            insertBook("B003", "1984", "George Orwell", "9788522106169", 
                      "Ficção Científica", 1949, "AVAILABLE", null, null, null);
            
            // Book 4
            insertBook("B004", "Orgulho e Preconceito", "Jane Austen", "9788544001820", 
                      "Romance", 1813, "AVAILABLE", null, null, null);
            
            // Book 5
            insertBook("B005", "O Hobbit", "J.R.R. Tolkien", "9788595084742", 
                      "Fantasia", 1937, "AVAILABLE", null, null, null);
            
            System.out.println("Books generated successfully");
        } catch (Exception e) {
            System.err.println("Error generating books: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void insertBook(String bookId, String title, String author, String isbn, 
                          String category, int publicationYear, String status, 
                          String borrowedBy, String borrowDate, String dueDate) throws Exception {
        String sql = "INSERT INTO books (book_id, title, author, isbn, category, publication_year, " +
                    "status, borrowed_by, borrow_date, due_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bookId);
            stmt.setString(2, title);
            stmt.setString(3, author);
            stmt.setString(4, isbn);
            stmt.setString(5, category);
            stmt.setInt(6, publicationYear);
            stmt.setString(7, status);
            stmt.setString(8, borrowedBy);
            stmt.setString(9, borrowDate);
            stmt.setString(10, dueDate);
            stmt.executeUpdate();
        }
    }
    
    private void generateAccounts() {
        try {
            // Account 1
            insertAccount("A001", "joao.silva", "senha123", "ACTIVE", 
                        LocalDateTime.now().minusMonths(3).toString(), null, "U001");
            
            // Account 2
            insertAccount("A002", "maria.oliveira", "senha456", "ACTIVE", 
                        LocalDateTime.now().minusMonths(2).toString(), null, "U002");
            
            // Account 3
            insertAccount("A003", "carlos.pereira", "admin789", "ACTIVE", 
                        LocalDateTime.now().minusMonths(6).toString(), null, "U003");
            
            System.out.println("Accounts generated successfully");
        } catch (Exception e) {
            System.err.println("Error generating accounts: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void insertAccount(String accountId, String username, String password, 
                             String status, String creationDate, String lastLogin, 
                             String userId) throws Exception {
        String sql = "INSERT INTO accounts (account_id, username, password, status, creation_date, " +
                    "last_login, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountId);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, status);
            stmt.setString(5, creationDate);
            stmt.setString(6, lastLogin);
            stmt.setString(7, userId);
            stmt.executeUpdate();
        }
    }
    
    private void linkBooksToStudents() {
        try {
            // Make student1 borrow book3
            LocalDate borrowDate = LocalDate.now();
            LocalDate dueDate = borrowDate.plusDays(14);
            
            String sql = "UPDATE books SET status = ?, borrowed_by = ?, borrow_date = ?, due_date = ? WHERE book_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, "BORROWED");
                stmt.setString(2, "S001"); // Student1's ID
                stmt.setString(3, borrowDate.toString());
                stmt.setString(4, dueDate.toString());
                stmt.setString(5, "B003"); // Book3's ID
                stmt.executeUpdate();
            }
            
            System.out.println("Books linked to students successfully");
        } catch (Exception e) {
            System.err.println("Error linking books to students: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Starting test data generation...");
        TestDataGenerator generator = new TestDataGenerator();
        generator.generateTestData();
        // DON'T close the connection here - it will be used by the application
    }
}