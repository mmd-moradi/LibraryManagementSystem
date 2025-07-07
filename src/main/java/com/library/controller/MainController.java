package com.library.controller;

import com.library.model.Book;
import com.library.model.LibraryManagementSystem;
import com.library.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private BorderPane mainContainer;
    
    private LibraryManagementSystem librarySystem;
    private User currentUser;
    
    @FXML
    private void initialize() {
        // Initialize the library system
        librarySystem = new LibraryManagementSystem("Sistema de Gerenciamento de Biblioteca");
        
        // Load the default view (dashboard)
        loadDashboard();
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        librarySystem.setCurrentUser(user);
    }
    
    public void loadDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Parent dashboard = loader.load();
            
            DashboardController controller = loader.getController();
            controller.setLibrarySystem(librarySystem);
            if (currentUser != null) {
                controller.setLoggedInUser(currentUser);
            }
            
            mainContainer.setCenter(dashboard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void openBookForm(boolean isEditMode, Book bookToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bookForm.fxml"));
            Parent bookForm = loader.load();
            
            BookFormController controller = loader.getController();
            controller.setLibrarySystem(librarySystem);
            
            if (isEditMode && bookToEdit != null) {
                controller.setBook(bookToEdit);
            }
            
            Stage stage = new Stage();
            stage.setTitle(isEditMode ? "Editar Livro" : "Adicionar Livro");
            stage.setScene(new Scene(bookForm));
            stage.showAndWait();
            
            // Refresh dashboard if needed
            loadDashboard();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}