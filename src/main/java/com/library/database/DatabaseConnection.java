package com.library.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_NAME = "library.db";
    private static final String URL = "jdbc:sqlite:" + DB_NAME;
    private static Connection connection;
    
    /**
     * Gets the database connection. If the connection doesn't exist or is closed, 
     * creates a new one.
     */
    public static synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Print database path for debugging
                File dbFile = new File(DB_NAME);
                System.out.println("Database path: " + dbFile.getAbsolutePath());
                System.out.println("Database exists: " + dbFile.exists());
                
                connection = DriverManager.getConnection(URL);
                System.out.println("Database connection established successfully.");
                
                // Enable foreign keys
                Statement stmt = connection.createStatement();
                stmt.execute("PRAGMA foreign_keys = ON");
                stmt.close();
            }
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return connection;
    }
    
    /**
     * Closes the database connection.
     */
    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Database connection closed.");
                }
                connection = null;
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Initializes the database by creating required tables.
     */
    public static void initializeDatabase() {
        Connection conn = getConnection();
        
        try {
            createTables(conn);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
            e.printStackTrace();
        }
        // DO NOT close the connection here
    }
    
    private static void createTables(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Create users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                        "user_id TEXT PRIMARY KEY," +
                        "name TEXT NOT NULL," +
                        "email TEXT," +
                        "phone_number TEXT," +
                        "address TEXT," +
                        "user_type TEXT NOT NULL" +
                        ")");
            
            // Create students table
            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                        "student_id TEXT PRIMARY KEY," +
                        "department TEXT," +
                        "user_id TEXT NOT NULL," +
                        "FOREIGN KEY (user_id) REFERENCES users(user_id)" +
                        ")");
            
            // Create employees table
            stmt.execute("CREATE TABLE IF NOT EXISTS employees (" +
                        "employee_id TEXT PRIMARY KEY," +
                        "position TEXT," +
                        "date_hired TEXT," +
                        "salary REAL," +
                        "user_id TEXT NOT NULL," +
                        "FOREIGN KEY (user_id) REFERENCES users(user_id)" +
                        ")");
            
            // Create librarians table
            stmt.execute("CREATE TABLE IF NOT EXISTS librarians (" +
                        "specialization TEXT," +
                        "employee_id TEXT NOT NULL," +
                        "FOREIGN KEY (employee_id) REFERENCES employees(employee_id)" +
                        ")");
            
            // Create accounts table
            stmt.execute("CREATE TABLE IF NOT EXISTS accounts (" +
                        "account_id TEXT PRIMARY KEY," +
                        "username TEXT UNIQUE NOT NULL," +
                        "password TEXT NOT NULL," +
                        "status TEXT NOT NULL," +
                        "creation_date TEXT," +
                        "last_login TEXT," +
                        "user_id TEXT NOT NULL," +
                        "FOREIGN KEY (user_id) REFERENCES users(user_id)" +
                        ")");
            
            // Create books table
            stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                        "book_id TEXT PRIMARY KEY," +
                        "title TEXT NOT NULL," +
                        "author TEXT NOT NULL," + 
                        "isbn TEXT," +
                        "category TEXT," +
                        "publication_year INTEGER," +
                        "status TEXT NOT NULL," +
                        "borrowed_by TEXT," +
                        "borrow_date TEXT," +
                        "due_date TEXT," +
                        "FOREIGN KEY (borrowed_by) REFERENCES students(student_id)" +
                        ")");
                                    
            String createBorrowingsTable = "CREATE TABLE IF NOT EXISTS borrowings (" +
                "borrowingId TEXT PRIMARY KEY," +
                "bookId TEXT NOT NULL," +
                "userId TEXT NOT NULL," +
                "borrowDate DATE NOT NULL," +
                "dueDate DATE NOT NULL," +
                "returnDate DATE," +
                "status TEXT NOT NULL," +
                "FOREIGN KEY (bookId) REFERENCES books(book_id)," + // Fixed reference
                "FOREIGN KEY (userId) REFERENCES users(user_id)" +  // Fixed reference
                ")";
            stmt.execute(createBorrowingsTable);
        }
    }
    
    /**
     * For direct database manipulation
     */
    public static int executeUpdate(String sql) {
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Error executing SQL update: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
}