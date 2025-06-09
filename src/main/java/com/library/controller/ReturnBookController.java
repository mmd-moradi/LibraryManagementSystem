package com.library.controller;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.Student;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ReturnBookController {
    @FXML
    private TextField bookIdField;
    
    @FXML
    private TextField bookTitleField;
    
    @FXML
    private TextField bookAuthorField;
    
    @FXML
    private TextField borrowedByField;
    
    @FXML
    private TextField issueDateField;
    
    @FXML
    private TextField dueDateField;
    
    @FXML
    private TextField statusField;
    
    @FXML
    private DatePicker returnDatePicker;
    
    @FXML
    private TextField lateFeeField;
    
    @FXML
    private Button returnButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Label messageLabel;
    
    private Book selectedBook;
    private Student borrower;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private static final double DAILY_LATE_FEE = 0.50;
    
    @FXML
    private void initialize() {
        returnDatePicker.setValue(LocalDate.now());
        
        returnButton.setDisable(true);
        
        returnDatePicker.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (dueDate != null && newValue != null) {
                calculateLateFee(newValue);
            }
        });
    }
    
    @FXML
    private void handleBookSearch() {
        String bookId = bookIdField.getText().trim();
        if (bookId.isEmpty()) {
            messageLabel.setText("Por favor, digite um ID de Livro");
            return;
        }
        
        if (bookId.equals("B003")) {
            selectedBook = new Book();
            selectedBook.setBookId(bookId);
            selectedBook.setTitle("1984");
            selectedBook.setAuthor("George Orwell");
            selectedBook.setStatus(BookStatus.BORROWED);
            
            borrower = new Student();
            borrower.setStudentId("S001");
            borrower.setName("João Silva");
            
            issueDate = LocalDate.now().minusDays(10);
            dueDate = LocalDate.now().plusDays(4);
            
            bookTitleField.setText(selectedBook.getTitle());
            bookAuthorField.setText(selectedBook.getAuthor());
            borrowedByField.setText(borrower.getName() + " (" + borrower.getStudentId() + ")");
            issueDateField.setText(issueDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
            dueDateField.setText(dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
            statusField.setText(selectedBook.getStatus().toString());
            
            calculateLateFee(returnDatePicker.getValue());
            
            returnButton.setDisable(false);
            messageLabel.setText("");
            
        } else if (bookId.equals("B001") || bookId.equals("B002") || bookId.equals("B004")) {
            selectedBook = new Book();
            selectedBook.setBookId(bookId);
            
            if (bookId.equals("B001")) {
                selectedBook.setTitle("O Grande Gatsby");
                selectedBook.setAuthor("F. Scott Fitzgerald");
            } else if (bookId.equals("B002")) {
                selectedBook.setTitle("O Sol é para Todos");
                selectedBook.setAuthor("Harper Lee");
            } else {
                selectedBook.setTitle("Orgulho e Preconceito");
                selectedBook.setAuthor("Jane Austen");
            }
            
            selectedBook.setStatus(BookStatus.AVAILABLE);
            
            bookTitleField.setText(selectedBook.getTitle());
            bookAuthorField.setText(selectedBook.getAuthor());
            borrowedByField.setText("N/A");
            issueDateField.setText("N/A");
            dueDateField.setText("N/A");
            statusField.setText(selectedBook.getStatus().toString());
            lateFeeField.setText("R$0,00");
            
            returnButton.setDisable(true);
            messageLabel.setText("Este livro não está atualmente emprestado");
            
        } else if (bookId.equals("B005")) {
            selectedBook = new Book();
            selectedBook.setBookId(bookId);
            selectedBook.setTitle("O Hobbit");
            selectedBook.setAuthor("J.R.R. Tolkien");
            selectedBook.setStatus(BookStatus.RESERVED);
            
            bookTitleField.setText(selectedBook.getTitle());
            bookAuthorField.setText(selectedBook.getAuthor());
            borrowedByField.setText("N/A");
            issueDateField.setText("N/A");
            dueDateField.setText("N/A");
            statusField.setText(selectedBook.getStatus().toString());
            lateFeeField.setText("R$0,00");
            
            returnButton.setDisable(true);
            messageLabel.setText("Este livro está reservado, não emprestado");
            
        } else {
            messageLabel.setText("Livro não encontrado");
            clearFields();
            selectedBook = null;
            returnButton.setDisable(true);
        }
    }
    
    private void calculateLateFee(LocalDate returnDate) {
        if (returnDate.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            double fee = daysLate * DAILY_LATE_FEE;
            lateFeeField.setText(String.format("R$%.2f", fee));
            
            if (daysLate > 0) {
                messageLabel.setText(String.format("Livro está %d dia(s) atrasado", daysLate));
            }
        } else {
            lateFeeField.setText("R$0,00");
            messageLabel.setText("");
        }
    }
    
    public void preloadBookId(String bookId) {
        bookIdField.setText(bookId);
        handleBookSearch();
    }

    @FXML
    private void handleReturn() {
        if (selectedBook == null || selectedBook.getStatus() != BookStatus.BORROWED) {
            messageLabel.setText("Por favor, selecione um livro emprestado");
            return;
        }
        
        LocalDate returnDate = returnDatePicker.getValue();
        if (returnDate == null) {
            messageLabel.setText("Por favor, selecione uma data de devolução");
            return;
        }
        
        String feeMessage = "";
        if (!lateFeeField.getText().equals("R$0,00")) {
            feeMessage = " com multa de " + lateFeeField.getText();
        }
        
        messageLabel.setText("Livro devolvido com sucesso" + feeMessage);
        
        selectedBook.setStatus(BookStatus.AVAILABLE);
        statusField.setText(selectedBook.getStatus().toString());
        
        returnButton.setDisable(true);
    }
    
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    private void clearFields() {
        bookTitleField.clear();
        bookAuthorField.clear();
        borrowedByField.clear();
        issueDateField.clear();
        dueDateField.clear();
        statusField.clear();
        lateFeeField.clear();
    }
}