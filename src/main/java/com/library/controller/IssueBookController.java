package com.library.controller;

import com.library.model.*;
import com.library.service.LibraryDatabaseService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class IssueBookController {

    @FXML private TextField bookIdField;
    @FXML private TextField bookTitleField;
    @FXML private TextField bookAuthorField;
    @FXML private TextField bookStatusField;
    @FXML private TextField studentIdField;
    @FXML private TextField studentNameField;
    @FXML private TextField borrowedBooksField;
    @FXML private DatePicker dueDatePicker;
    @FXML private Button issueButton;
    @FXML private Label messageLabel;
    
    private final LibraryDatabaseService dbService = new LibraryDatabaseService();
    private Book currentBook;
    private Student currentStudent;

    // IssueBookController.java - Update handleBookSearch
    @FXML
    private void handleBookSearch() {
        String bookId = bookIdField.getText().trim();
        if (bookId.isEmpty()) {
            showMessage("Por favor, digite o ID do livro.");
            return;
        }

        currentBook = dbService.getBookById(bookId);
        if (currentBook == null) {
            showMessage("Livro não encontrado.");
            clearBookFields();
        } else {
            bookTitleField.setText(currentBook.getTitle());
            bookAuthorField.setText(currentBook.getAuthor());
            bookStatusField.setText(currentBook.getStatus().toString());
            
            // Enable/disable based on availability
            issueButton.setDisable(!currentBook.isAvailable());
            dueDatePicker.setDisable(!currentBook.isAvailable());
            
            if (!currentBook.isAvailable()) {
                showMessage("Livro não está disponível para empréstimo.");
            }
        }
    }

    @FXML
    private void handleStudentSearch() {
        String studentId = studentIdField.getText().trim();
        if (studentId.isEmpty()) {
            showMessage("Por favor, digite o ID do estudante.");
            return;
        }

        User user = dbService.getUserById(studentId);
        if (user == null || !(user instanceof Student)) {
            showMessage("Estudante não encontrado.");
            clearStudentFields();
            disableIssue();
        } else {
            currentStudent = (Student) user;
            studentNameField.setText(currentStudent.getName());
            
            int activeBorrowings = dbService.getActiveBorrowingsByUser(studentId).size();
            borrowedBooksField.setText(String.valueOf(activeBorrowings));
            
            if (activeBorrowings >= 5) { // Max 5 books per student
                showMessage("Estudante atingiu o limite de empréstimos.");
                disableIssue();
            } else {
                enableIssue();
            }
        }
    }

    @FXML
    private void handleIssue() {
        if (currentBook == null || currentStudent == null) {
            showMessage("Por favor, busque um livro e estudante válidos.");
            return;
        }
        
        LocalDate dueDate = dueDatePicker.getValue();
        if (dueDate == null || dueDate.isBefore(LocalDate.now().plusDays(1))) {
            showMessage("Data de devolução inválida. Deve ser pelo menos 1 dia no futuro.");
            return;
        }
        
        try {
            dbService.issueBook(currentBook, currentStudent, dueDate);
            showMessage("Livro emprestado com sucesso! Data de devolução: " + dueDate);
            resetForm();
        } catch (Exception e) {
            showMessage("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        resetForm();
    }
    
    private void resetForm() {
        bookIdField.clear();
        studentIdField.clear();
        dueDatePicker.setValue(null);
        clearBookFields();
        clearStudentFields();
        messageLabel.setText("");
        currentBook = null;
        currentStudent = null;
    }
    
    private void clearBookFields() {
        bookTitleField.clear();
        bookAuthorField.clear();
        bookStatusField.clear();
    }
    
    private void clearStudentFields() {
        studentNameField.clear();
        borrowedBooksField.clear();
    }
    
    private void showMessage(String message) {
        messageLabel.setText(message);
    }
    
    private void disableIssue() {
        issueButton.setDisable(true);
        dueDatePicker.setDisable(true);
    }
    
    private void enableIssue() {
        if (currentBook != null && currentBook.isAvailable() && 
            currentStudent != null && 
            Integer.parseInt(borrowedBooksField.getText()) < 5) {
            
            issueButton.setDisable(false);
            dueDatePicker.setDisable(false);
            dueDatePicker.setValue(LocalDate.now().plusWeeks(2)); // Default 2 weeks
        }
    }
}