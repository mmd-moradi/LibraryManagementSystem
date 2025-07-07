package com.library.controller;

import com.library.model.LibraryManagementSystem;
import com.library.model.User;
import com.library.model.Student;
import com.library.model.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UserProfileController {
    @FXML private Label nameLabel;
    @FXML private Label userIdLabel;
    @FXML private Label userTypeLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;
    @FXML private Button closeButton;
    
    private LibraryManagementSystem librarySystem;
    private User user;
    
    public void setLibrarySystem(LibraryManagementSystem librarySystem) {
        this.librarySystem = librarySystem;
    }
    
    public void setUser(User user) {
        this.user = user;
        
        // Populate user details if labels exist
        if (nameLabel != null) nameLabel.setText(user.getName());
        if (userIdLabel != null) userIdLabel.setText(user.getUserId());
        if (userTypeLabel != null) userTypeLabel.setText(user.getUserType());
        if (emailLabel != null) emailLabel.setText(user.getEmail());
        if (phoneLabel != null) phoneLabel.setText(user.getPhoneNumber());
        if (addressLabel != null) addressLabel.setText(user.getAddress());
    }
    
    @FXML
    private void handleClose() {
        if (closeButton != null) {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        }
    }
}