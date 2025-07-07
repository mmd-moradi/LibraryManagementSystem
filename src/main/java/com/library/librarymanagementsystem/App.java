package com.library.librarymanagementsystem;

import com.library.database.DatabaseConnection;
import com.library.util.TestDataGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * JavaFX App - Main class for the Library Management System
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Initializing database...");
        try {
            
            DatabaseConnection.initializeDatabase();
            
            
            Connection conn = DatabaseConnection.getConnection();
            boolean needsData = true;
            
            try {
                ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM accounts");
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Database already contains data.");
                    needsData = false;
                }
            } catch (SQLException e) {
                System.out.println("Error checking accounts table: " + e.getMessage());
                
            }
            
            if (needsData) {
                System.out.println("Generating test data...");
                TestDataGenerator generator = new TestDataGenerator();
                generator.generateTestData();
            }
            
            scene = new Scene(loadFXML("login"), 640, 480);
            stage.setScene(scene);
            stage.setTitle("Library Management System - Login");
            stage.show();
            
        } catch (Exception e) {
            System.err.println("Error initializing application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
    
    @Override
    public void stop() {
        System.out.println("Closing database connection...");
        DatabaseConnection.closeConnection();
    }
}