package com.library.util;

import com.library.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

public class DirectDatabaseSetup {

    public static void main(String[] args) {
        System.out.println("Starting direct database setup...");
        
        try {
            
            DatabaseConnection.initializeDatabase();
            
            
            Connection conn = DatabaseConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM users");
            boolean hasData = rs.next() && rs.getInt(1) > 0;
            
            if (hasData) {
                System.out.println("Database already contains data. Skipping data generation.");
            } else {
                System.out.println("Generating test data...");
                clearTables(conn);
                createUsers(conn);
                createBooks(conn);
                createAccounts(conn);
                linkBooksToStudents(conn);
                System.out.println("Test data generation complete!");
            }
            
            System.out.println("Database setup complete. You can now run the application.");
            System.out.println("Login credentials:");
            System.out.println("Username: carlos.pereira");
            System.out.println("Password: admin789");
            
        } catch (Exception e) {
            System.err.println("Error setting up database: " + e.getMessage());
            e.printStackTrace();
        } finally {
            
        }
    }
    
    private static void clearTables(Connection conn) throws Exception {
        
        String[] tableNames = {
            "books", "librarians", "accounts", "employees", "students", "users"
        };
        
        for (String tableName : tableNames) {
            String sql = "DELETE FROM " + tableName;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
                System.out.println("Cleared table: " + tableName);
            }
        }
    }
    
    private static void createUsers(Connection conn) throws Exception {
        
        String userSql = "INSERT INTO users (user_id, name, email, phone_number, address, user_type) VALUES (?, ?, ?, ?, ?, ?)";
        
        
        try (PreparedStatement stmt = conn.prepareStatement(userSql)) {
            stmt.setString(1, "U001");
            stmt.setString(2, "João Silva");
            stmt.setString(3, "joao.silva@example.com");
            stmt.setString(4, "(11) 98765-4321");
            stmt.setString(5, "Av. Paulista, 123 - São Paulo, SP");
            stmt.setString(6, "Student");
            stmt.executeUpdate();
        }
        
        
        try (PreparedStatement stmt = conn.prepareStatement(userSql)) {
            stmt.setString(1, "U002");
            stmt.setString(2, "Maria Oliveira");
            stmt.setString(3, "maria.oliveira@example.com");
            stmt.setString(4, "(11) 91234-5678");
            stmt.setString(5, "Rua Augusta, 456 - São Paulo, SP");
            stmt.setString(6, "Student");
            stmt.executeUpdate();
        }
        
        
        try (PreparedStatement stmt = conn.prepareStatement(userSql)) {
            stmt.setString(1, "U003");
            stmt.setString(2, "Carlos Pereira");
            stmt.setString(3, "carlos.pereira@library.com");
            stmt.setString(4, "(11) 97777-8888");
            stmt.setString(5, "Rua dos Bibliotecários, 789 - São Paulo, SP");
            stmt.setString(6, "Librarian");
            stmt.executeUpdate();
        }
        
        
        String studentSql = "INSERT INTO students (student_id, department, user_id) VALUES (?, ?, ?)";
        
        
        try (PreparedStatement stmt = conn.prepareStatement(studentSql)) {
            stmt.setString(1, "S001");
            stmt.setString(2, "Ciência da Computação");
            stmt.setString(3, "U001");
            stmt.executeUpdate();
        }
        
        
        try (PreparedStatement stmt = conn.prepareStatement(studentSql)) {
            stmt.setString(1, "S002");
            stmt.setString(2, "Engenharia");
            stmt.setString(3, "U002");
            stmt.executeUpdate();
        }
        
        
        String employeeSql = "INSERT INTO employees (employee_id, position, date_hired, salary, user_id) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(employeeSql)) {
            stmt.setString(1, "E001");
            stmt.setString(2, "Bibliotecário Chefe");
            stmt.setString(3, "2020-03-15");
            stmt.setDouble(4, 5000.0);
            stmt.setString(5, "U003");
            stmt.executeUpdate();
        }
        
        
        String librarianSql = "INSERT INTO librarians (specialization, employee_id) VALUES (?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(librarianSql)) {
            stmt.setString(1, "Literatura Brasileira");
            stmt.setString(2, "E001");
            stmt.executeUpdate();
        }
        
        System.out.println("Users created successfully");
    }
    
    private static void createBooks(Connection conn) throws Exception {
        String bookSql = "INSERT INTO books (book_id, title, author, isbn, category, publication_year, status, borrowed_by, borrow_date, due_date) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        
        try (PreparedStatement stmt = conn.prepareStatement(bookSql)) {
            stmt.setString(1, "B001");
            stmt.setString(2, "O Grande Gatsby");
            stmt.setString(3, "F. Scott Fitzgerald");
            stmt.setString(4, "9788525056009");
            stmt.setString(5, "Ficção");
            stmt.setInt(6, 1925);
            stmt.setString(7, "AVAILABLE");
            stmt.setString(8, null);
            stmt.setString(9, null);
            stmt.setString(10, null);
            stmt.executeUpdate();
        }
        
        
        try (PreparedStatement stmt = conn.prepareStatement(bookSql)) {
            stmt.setString(1, "B002");
            stmt.setString(2, "O Sol é para Todos");
            stmt.setString(3, "Harper Lee");
            stmt.setString(4, "9788501116598");
            stmt.setString(5, "Ficção");
            stmt.setInt(6, 1960);
            stmt.setString(7, "AVAILABLE");
            stmt.setString(8, null);
            stmt.setString(9, null);
            stmt.setString(10, null);
            stmt.executeUpdate();
        }
        
        
        try (PreparedStatement stmt = conn.prepareStatement(bookSql)) {
            stmt.setString(1, "B003");
            stmt.setString(2, "1984");
            stmt.setString(3, "George Orwell");
            stmt.setString(4, "9788522106169");
            stmt.setString(5, "Ficção Científica");
            stmt.setInt(6, 1949);
            stmt.setString(7, "AVAILABLE");
            stmt.setString(8, null);
            stmt.setString(9, null);
            stmt.setString(10, null);
            stmt.executeUpdate();
        }
        
        
        try (PreparedStatement stmt = conn.prepareStatement(bookSql)) {
            stmt.setString(1, "B004");
            stmt.setString(2, "Orgulho e Preconceito");
            stmt.setString(3, "Jane Austen");
            stmt.setString(4, "9788544001820");
            stmt.setString(5, "Romance");
            stmt.setInt(6, 1813);
            stmt.setString(7, "AVAILABLE");
            stmt.setString(8, null);
            stmt.setString(9, null);
            stmt.setString(10, null);
            stmt.executeUpdate();
        }
        
        
        try (PreparedStatement stmt = conn.prepareStatement(bookSql)) {
            stmt.setString(1, "B005");
            stmt.setString(2, "O Hobbit");
            stmt.setString(3, "J.R.R. Tolkien");
            stmt.setString(4, "9788595084742");
            stmt.setString(5, "Fantasia");
            stmt.setInt(6, 1937);
            stmt.setString(7, "AVAILABLE");
            stmt.setString(8, null);
            stmt.setString(9, null);
            stmt.setString(10, null);
            stmt.executeUpdate();
        }
        
        System.out.println("Books created successfully");
    }
    
    private static void createAccounts(Connection conn) throws Exception {
        String accountSql = "INSERT INTO accounts (account_id, username, password, status, creation_date, last_login, user_id) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        
        try (PreparedStatement stmt = conn.prepareStatement(accountSql)) {
            stmt.setString(1, "A001");
            stmt.setString(2, "joao.silva");
            stmt.setString(3, "senha123");
            stmt.setString(4, "ACTIVE");
            stmt.setString(5, LocalDateTime.now().minusMonths(3).toString());
            stmt.setString(6, null);
            stmt.setString(7, "U001");
            stmt.executeUpdate();
        }
        
        
        try (PreparedStatement stmt = conn.prepareStatement(accountSql)) {
            stmt.setString(1, "A002");
            stmt.setString(2, "maria.oliveira");
            stmt.setString(3, "senha456");
            stmt.setString(4, "ACTIVE");
            stmt.setString(5, LocalDateTime.now().minusMonths(2).toString());
            stmt.setString(6, null);
            stmt.setString(7, "U002");
            stmt.executeUpdate();
        }
        
        
        try (PreparedStatement stmt = conn.prepareStatement(accountSql)) {
            stmt.setString(1, "A003");
            stmt.setString(2, "carlos.pereira");
            stmt.setString(3, "admin789");
            stmt.setString(4, "ACTIVE");
            stmt.setString(5, LocalDateTime.now().minusMonths(6).toString());
            stmt.setString(6, null);
            stmt.setString(7, "U003");
            stmt.executeUpdate();
        }
        
        System.out.println("Accounts created successfully");
    }
    
    private static void linkBooksToStudents(Connection conn) throws Exception {
        
        String updateSql = "UPDATE books SET status = ?, borrowed_by = ?, borrow_date = ?, due_date = ? WHERE book_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setString(1, "BORROWED");
            stmt.setString(2, "S001");
            stmt.setString(3, java.time.LocalDate.now().toString());
            stmt.setString(4, java.time.LocalDate.now().plusDays(14).toString());
            stmt.setString(5, "B003");
            stmt.executeUpdate();
        }
        
        System.out.println("Books linked to students successfully");
    }
}