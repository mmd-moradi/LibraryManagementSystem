package com.library.controller;

import com.library.model.Account;
import com.library.model.LibraryManagementSystem;
import com.library.service.LibraryDatabaseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

public class LoginController {
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Label errorLabel;
    
    private LibraryManagementSystem librarySystem;
    private LibraryDatabaseService dbService;
    
    @FXML
    private void initialize() {
        dbService = new LibraryDatabaseService();
        librarySystem = new LibraryManagementSystem("Sistema de Gerenciamento de Biblioteca");
        errorLabel.setVisible(false);
    }
    
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Por favor, preencha todos os campos");
            return;
        }
        
        
        Account account = dbService.findAccountByUsername(username);
        if (account != null && account.login(password)) {
            
            account.setLastLogin(LocalDateTime.now());
            dbService.updateAccount(account);
            
            
            librarySystem.setCurrentUser(account.getUser());
            
            try {
                
                System.out.println("Loading dashboard.fxml");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
                
                Parent dashboardView = loader.load();
                
                DashboardController controller = loader.getController();
                controller.setLibrarySystem(librarySystem);
                controller.setLoggedInUser(account.getUser());
                
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle("Sistema de Biblioteca");
                stage.setScene(new Scene(dashboardView));
                stage.setMaximized(true);
                stage.show();
                
            } catch (IOException e) {
                showError("Error loading dashboard: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showError("Nome de usuário ou senha inválidos");
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}