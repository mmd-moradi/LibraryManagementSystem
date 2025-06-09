package com.library.controller;

import com.library.librarymanagementsystem.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

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
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Usuário e senha não podem estar vazios");
            return;
        }
        
        if (username.equals("admin") && password.equals("admin")) {
            try {
                App.setRoot("dashboard");
            } catch (IOException e) {
                messageLabel.setText("Erro ao carregar painel: " + e.getMessage());
            }
        } else {
            messageLabel.setText("Usuário ou senha inválidos");
        }
    }
    
    @FXML
    private void handleCancel() {
        usernameField.clear();
        passwordField.clear();
        messageLabel.setText("");
    }
}