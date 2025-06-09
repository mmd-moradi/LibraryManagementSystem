package com.library.controller;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.Student;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class IssueBookController {
    @FXML
    private TextField bookIdField;
    
    @FXML
    private TextField bookTitleField;
    
    @FXML
    private TextField bookAuthorField;
    
    @FXML
    private TextField bookStatusField;
    
    @FXML
    private TextField studentIdField;
    
    @FXML
    private TextField studentNameField;
    
    @FXML
    private TextField borrowedBooksField;
    
    @FXML
    private DatePicker issueDatePicker;
    
    @FXML
    private DatePicker dueDatePicker;
    
    @FXML
    private Button issueButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Label messageLabel;
    
    private Book selectedBook;
    private Student selectedStudent;
    
    @FXML
    private void initialize() {
        issueDatePicker.setValue(LocalDate.now());
        dueDatePicker.setValue(LocalDate.now().plusDays(14));
        
        issueButton.setDisable(true);
    }
    
    @FXML
    private void handleBookSearch() {
        String bookId = bookIdField.getText().trim();
        if (bookId.isEmpty()) {
            messageLabel.setText("Por favor, digite um ID de Livro");
            return;
        }
        
        if (bookId.equals("B001") || bookId.equals("B002") || bookId.equals("B004")) {
            selectedBook = new Book();
            selectedBook.setBookId(bookId);
            
            if (bookId.equals("B001")) {
                selectedBook.setTitle("O Grande Gatsby");
                selectedBook.setAuthor("F. Scott Fitzgerald");
                selectedBook.setStatus(BookStatus.AVAILABLE);
            } else if (bookId.equals("B002")) {
                selectedBook.setTitle("O Sol é para Todos");
                selectedBook.setAuthor("Harper Lee");
                selectedBook.setStatus(BookStatus.AVAILABLE);
            } else {
                selectedBook.setTitle("Orgulho e Preconceito");
                selectedBook.setAuthor("Jane Austen");
                selectedBook.setStatus(BookStatus.AVAILABLE);
            }
            
            bookTitleField.setText(selectedBook.getTitle());
            bookAuthorField.setText(selectedBook.getAuthor());
            bookStatusField.setText(selectedBook.getStatus().toString());
            
            if (!selectedBook.isAvailable()) {
                messageLabel.setText("Este livro não está disponível para empréstimo");
                issueButton.setDisable(true);
            } else {
                messageLabel.setText("");
                issueButton.setDisable(selectedStudent == null);
            }
            
        } else if (bookId.equals("B003") || bookId.equals("B005")) {
            selectedBook = new Book();
            selectedBook.setBookId(bookId);
            
            if (bookId.equals("B003")) {
                selectedBook.setTitle("1984");
                selectedBook.setAuthor("George Orwell");
                selectedBook.setStatus(BookStatus.BORROWED);
            } else {
                selectedBook.setTitle("O Hobbit");
                selectedBook.setAuthor("J.R.R. Tolkien");
                selectedBook.setStatus(BookStatus.RESERVED);
            }
            
            bookTitleField.setText(selectedBook.getTitle());
            bookAuthorField.setText(selectedBook.getAuthor());
            bookStatusField.setText(selectedBook.getStatus().toString());
            
            messageLabel.setText("Este livro não está disponível para empréstimo");
            issueButton.setDisable(true);
            
        } else {
            messageLabel.setText("Livro não encontrado");
            clearBookFields();
            selectedBook = null;
            issueButton.setDisable(true);
        }
    }
    
    public void preloadBookId(String bookId) {
        bookIdField.setText(bookId);
        handleBookSearch();
    }

    @FXML
    private void handleStudentSearch() {
        String studentId = studentIdField.getText().trim();
        if (studentId.isEmpty()) {
            messageLabel.setText("Por favor, digite um ID de Estudante");
            return;
        }
        
        if (studentId.equals("S001") || studentId.equals("S002")) {
            selectedStudent = new Student();
            selectedStudent.setStudentId(studentId);
            
            if (studentId.equals("S001")) {
                selectedStudent.setName("João Silva");
                borrowedBooksField.setText("2");
            } else {
                selectedStudent.setName("Maria Oliveira");
                borrowedBooksField.setText("1");
            }
            
            studentNameField.setText(selectedStudent.getName());
            
            issueButton.setDisable(selectedBook == null || !selectedBook.isAvailable());
            messageLabel.setText("");
            
        } else {
            messageLabel.setText("Estudante não encontrado");
            clearStudentFields();
            selectedStudent = null;
            issueButton.setDisable(true);
        }
    }
    
    @FXML
    private void handleIssue() {
        if (selectedBook == null || selectedStudent == null) {
            messageLabel.setText("Por favor, selecione um livro e um estudante");
            return;
        }
        
        if (!selectedBook.isAvailable()) {
            messageLabel.setText("Este livro não está disponível para empréstimo");
            return;
        }
        
        LocalDate dueDate = dueDatePicker.getValue();
        if (dueDate == null || dueDate.isBefore(LocalDate.now())) {
            messageLabel.setText("Por favor, selecione uma data de devolução válida");
            return;
        }
        
        messageLabel.setText("Livro emprestado com sucesso");
        
        issueButton.setDisable(true);
    }
    
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
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
}