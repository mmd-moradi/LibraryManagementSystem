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
    @FXML private DatePicker issueDatePicker;
    @FXML private DatePicker dueDatePicker;
    @FXML private Button issueButton;
    @FXML private Button cancelButton;
    @FXML private Label messageLabel;
    
    private final LibraryDatabaseService dbService = new LibraryDatabaseService();
    private Book currentBook;
    private Student currentStudent;

    @FXML
    private void initialize() {
        
        issueDatePicker.setValue(LocalDate.now());
        issueDatePicker.setDisable(true); 
        
        
        dueDatePicker.setValue(LocalDate.now().plusWeeks(2));
        
        
        issueButton.setDisable(true);
    }

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
            updateIssueButtonState();
            return;
        }

        bookTitleField.setText(currentBook.getTitle());
        bookAuthorField.setText(currentBook.getAuthor());
        bookStatusField.setText(currentBook.getStatus().toString());
            
        
        if (!currentBook.isAvailable()) {
            showMessage("Livro não está disponível para empréstimo.");
            updateIssueButtonState();
        } else {
            showMessage("");
            updateIssueButtonState();
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
            updateIssueButtonState();
            return;
        }

        currentStudent = (Student) user;
        studentNameField.setText(currentStudent.getName());
            
        int activeBorrowings = dbService.getActiveBorrowingsByUser(studentId).size();
        borrowedBooksField.setText(String.valueOf(activeBorrowings));
            
        if (activeBorrowings >= 5) { 
            showMessage("Estudante atingiu o limite de empréstimos.");
            updateIssueButtonState();
        } else {
            showMessage("");
            updateIssueButtonState();
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
            disableControls();
        } catch (Exception e) {
            showMessage("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        
        cancelButton.getScene().getWindow().hide();
    }
    
    private void clearBookFields() {
        bookTitleField.clear();
        bookAuthorField.clear();
        bookStatusField.clear();
        currentBook = null;
    }
    
    private void clearStudentFields() {
        studentNameField.clear();
        borrowedBooksField.clear();
        currentStudent = null;
    }
    
    private void showMessage(String message) {
        messageLabel.setText(message);
    }
    
    private void updateIssueButtonState() {
        boolean canIssue = currentBook != null && currentBook.isAvailable() && 
                         currentStudent != null && 
                         (borrowedBooksField.getText().isEmpty() || 
                         Integer.parseInt(borrowedBooksField.getText()) < 5);
        
        issueButton.setDisable(!canIssue);
    }
    
    private void disableControls() {
        
        bookIdField.setDisable(true);
        studentIdField.setDisable(true);
        dueDatePicker.setDisable(true);
        issueButton.setDisable(true);
        
        
        cancelButton.setText("Fechar");
    }
}