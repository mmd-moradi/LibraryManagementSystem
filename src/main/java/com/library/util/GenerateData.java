package com.library.util;

public class GenerateData {
    public static void main(String[] args) {
        System.out.println("Starting test data generation...");
        TestDataGenerator generator = new TestDataGenerator();
        generator.generateTestData();
        System.out.println("Test data generation completed successfully!");
        
        // Important: Don't close the connection, just let the program exit
        System.out.println("You can now run the main application and log in with:");
        System.out.println("Username: carlos.pereira");
        System.out.println("Password: admin789");
    }
}