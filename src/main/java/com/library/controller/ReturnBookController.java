package com.library.controller;

import java.time.LocalDate;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.Borrowing;
import com.library.model.User;
import com.library.service.LibraryDatabaseService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ReturnBookController {

    @FXML private TextField bookIdField;
    @FXML private TextField bookTitleField;
    @FXML private TextField bookAuthorField;
    @FXML private TextField borrowedByField;
    @FXML private TextField issueDateField;
    @FXML private TextField dueDateField;
    @FXML private TextField statusField;
    @FXML private DatePicker returnDatePicker;
    @FXML private TextField lateFeeField;
    @FXML private Button returnButton;
    @FXML private Label messageLabel;
    
    private final LibraryDatabaseService dbService = new LibraryDatabaseService();
    private Book currentBook;
    private Borrowing currentBorrowing;

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
            clearFields();
            return;
        }

        bookTitleField.setText(currentBook.getTitle());
        bookAuthorField.setText(currentBook.getAuthor());
        
        // Get borrowing information
        currentBorrowing = dbService.getActiveBorrowingForBook(bookId);
        
        if (currentBorrowing == null) {
            statusField.setText("NÃO EMPRESTADO");
            disableReturn();
            showMessage("Livro não está emprestado atualmente.");
        } else {
            User borrower = dbService.getUserById(currentBorrowing.getUserId());
            borrowedByField.setText(borrower != null ? borrower.getName() : "Desconhecido");
            issueDateField.setText(currentBorrowing.getBorrowDate().toString());
            dueDateField.setText(currentBorrowing.getDueDate().toString());
            statusField.setText("EMPRESTADO");
            returnDatePicker.setValue(LocalDate.now());
            enableReturn();
        }
    }

// ReturnBookController.java - Update handleReturn
    @FXML
    private void handleReturn() {
        if (currentBook == null || currentBorrowing == null) {
            showMessage("Por favor, busque um livro válido que esteja emprestado.");
            return;
        }
        
        LocalDate returnDate = returnDatePicker.getValue();
        if (returnDate == null) {
            showMessage("Selecione uma data de devolução.");
            return;
        }
        
        try {
            // Update book status
            currentBook.setStatus(BookStatus.AVAILABLE);
            dbService.updateBook(currentBook);
            
            // Update borrowing record
            currentBorrowing.setReturnDate(returnDate);
            currentBorrowing.setStatus("RETURNED");
            dbService.updateBorrowing(currentBorrowing);
            
            // Calculate late fee
            double lateFee = dbService.calculateLateFee(currentBorrowing);
            lateFeeField.setText(String.format("R$%.2f", lateFee));
            
            showMessage("Livro devolvido com sucesso! Multa: R$" + lateFee);
            returnButton.setDisable(true);
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
        clearFields();
        returnDatePicker.setValue(null);
        lateFeeField.clear();
        messageLabel.setText("");
        currentBook = null;
        currentBorrowing = null;
        returnButton.setDisable(false);
    }
    
    private void clearFields() {
        bookTitleField.clear();
        bookAuthorField.clear();
        borrowedByField.clear();
        issueDateField.clear();
        dueDateField.clear();
        statusField.clear();
    }
    
    private void showMessage(String message) {
        messageLabel.setText(message);
    }
    
    private void disableReturn() {
        returnButton.setDisable(true);
        returnDatePicker.setDisable(true);
    }
    
    private void enableReturn() {
        returnButton.setDisable(false);
        returnDatePicker.setDisable(false);
    }
}