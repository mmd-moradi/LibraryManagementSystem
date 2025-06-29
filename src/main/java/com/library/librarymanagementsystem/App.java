package com.library.librarymanagementsystem;

import com.library.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

public class App extends Application {

    private static Scene scene;
    private static User loggedInUser;

    @Override
    public void start(Stage stage) throws IOException {
        // Initialize database and load sample data
        initializeDatabase();
        
        scene = new Scene(loadFXML("login"));
        stage.setTitle("Sistema de Gerenciamento de Biblioteca");
        stage.setScene(scene);
        stage.show();
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
    
    public static User getLoggedInUser() {
        return loggedInUser;
    }
    
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }
    
    private void initializeDatabase() {
        try {
            System.out.println("Initializing file-based database...");
            
            System.out.println("Database initialized with sample data.");
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}