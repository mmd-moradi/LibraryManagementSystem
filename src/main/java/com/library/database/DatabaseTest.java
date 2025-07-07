package com.library.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseTest {
    public static void testConnection() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Database connection successful!");
                
                
                Statement stmt = conn.createStatement();
                stmt.execute("CREATE TABLE IF NOT EXISTS test_table (id INTEGER, name TEXT)");
                stmt.execute("INSERT INTO test_table VALUES (1, 'Test Record')");
                System.out.println("Test table created and record inserted.");
                
                
                stmt.execute("DROP TABLE test_table");
                System.out.println("Test table removed.");
            } else {
                System.err.println("Failed to establish database connection.");
            }
        } catch (SQLException e) {
            System.err.println("Database test error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        testConnection();
        DatabaseConnection.closeConnection();
    }
}