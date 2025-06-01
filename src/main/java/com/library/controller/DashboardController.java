package com.library.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;

import java.io.IOException;

import com.library.librarymanagementsystem.App;

public class DashboardController {
    @FXML
    private TabPane tabPane;
    
    @FXML
    private Label totalBooksLabel;
    
    @FXML
    private Label borrowedBooksLabel;
    
    @FXML
    private Label totalUsersLabel;
    
    @FXML
    private Label overdueBooksLabel;
    
    @FXML
    private Label currentUserLabel;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private TableView recentActivitiesTable;
    
    @FXML
    private void initialize() {
        totalBooksLabel.setText("153");
        borrowedBooksLabel.setText("42");
        totalUsersLabel.setText("78");
        overdueBooksLabel.setText("5");
    }
    
    @FXML
    private void handleLogout() {
        try {
            App.setRoot("login");
        } catch (IOException e) {
            statusLabel.setText("Error logging out: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleExit() {
        Platform.exit();
    }
    
    @FXML
    private void showAllBooks() {
        openTab("Books", "books_view");
    }
    
    @FXML
    private void showAddBook() {
        statusLabel.setText("Add Book feature not implemented yet");
    }
    
    @FXML
    private void showSearchBooks() {
        statusLabel.setText("Search Books feature not implemented yet");
    }
    
    @FXML
    private void showAllUsers() {
        statusLabel.setText("View Users feature not implemented yet");
    }
    
    @FXML
    private void showAddUser() {
        statusLabel.setText("Add User feature not implemented yet");
    }
    
    @FXML
    private void showSearchUsers() {
        // Will implement later
        statusLabel.setText("Search Users feature not implemented yet");
    }
    
    @FXML
    private void showIssueBook() {
        // Will implement later
        statusLabel.setText("Issue Book feature not implemented yet");
    }
    
    @FXML
    private void showReturnBook() {
        // Will implement later
        statusLabel.setText("Return Book feature not implemented yet");
    }
    
    @FXML
    private void showCurrentBorrows() {
        // Will implement later
        statusLabel.setText("Current Borrows feature not implemented yet");
    }
    
    @FXML
    private void showOverdueBooks() {
        // Will implement later
        statusLabel.setText("Overdue Books feature not implemented yet");
    }
    
    @FXML
    private void showPopularBooks() {
        // Will implement later
        statusLabel.setText("Popular Books feature not implemented yet");
    }
    
    @FXML
    private void showAbout() {
        // Will implement later
        statusLabel.setText("Library Management System v1.0");
    }
    
    private void openTab(String title, String fxmlName) {
        try {
            // Check if tab already exists
            for (Tab tab : tabPane.getTabs()) {
                if (tab.getText().equals(title)) {
                    tabPane.getSelectionModel().select(tab);
                    return;
                }
            }
            
            // Create new tab
            Tab tab = new Tab(title);
            tab.setContent(FXMLLoader.load(getClass().getResource("/fxml/" + fxmlName + ".fxml")));
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            
        } catch (IOException e) {
            statusLabel.setText("Error loading tab: " + e.getMessage());
        }
    }
}