package com.library.controller;

import com.library.librarymanagementsystem.App;
import com.library.dao.UserDao;
import com.library.model.Account;
import com.library.model.AccountStatus;
import com.library.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
    private Button cancelButton;
    
    @FXML
    private Label messageLabel;
    
    private UserDao userDao = new UserDao();
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Usuário e senha não podem estar vazios");
            return;
        }
        
        try {
            User user = userDao.findByUsername(username);
            
            if (user != null && user.getAccount() != null && 
                password.equals(user.getAccount().getPassword())) {
                
                // Check account status
                if (user.getAccount().getStatus() != AccountStatus.ACTIVE) {
                    messageLabel.setText("Conta não está ativa. Contate o administrador.");
                    return;
                }
                
                // Update last login
                Account account = user.getAccount();
                account.setLastLogin(LocalDateTime.now());
                userDao.update(user);
                
                // Store logged in user for use throughout the application
                App.setLoggedInUser(user);
                
                // Navigate to dashboard
                App.setRoot("dashboard");
            } else {
                messageLabel.setText("Usuário ou senha inválidos");
            }
        } catch (Exception e) {
            messageLabel.setText("Erro ao fazer login: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleCancel() {
        usernameField.clear();
        passwordField.clear();
        messageLabel.setText("");
    }
}