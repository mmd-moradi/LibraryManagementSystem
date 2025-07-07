package com.library.controller;

import java.time.LocalDate;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.LibraryManagementSystem;
import com.library.service.LibraryDatabaseService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BookFormController {
    
    @FXML
    private TextField bookIdField;
    
    @FXML
    private TextField titleField;
    
    @FXML
    private TextField authorField;
    
    @FXML
    private TextField isbnField;
    
    @FXML
    private ComboBox<String> categoryComboBox;
    
    @FXML
    private TextField yearField;
    
    @FXML
    private ComboBox<BookStatus> statusComboBox;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Label messageLabel;
    
    private String generatedBookId;
    private Book book;
    private boolean isEditMode = false;
    private LibraryDatabaseService dbService = new LibraryDatabaseService();
    private LibraryManagementSystem librarySystem;
    
    public void setLibrarySystem(LibraryManagementSystem librarySystem) {
        this.librarySystem = librarySystem;
    }
    
    @FXML
    private void initialize() {
        categoryComboBox.setItems(FXCollections.observableArrayList(
          "Ficção", "Não-Ficção", "Ciência", "História", "Biografia", 
          "Tecnologia", "Referência", "Infantil", "Fantasia", "Mistério", 
          "Romance", "Auto-Ajuda", "Viagem", "Arte", "Outro"
          ));
        
          statusComboBox.setItems(FXCollections.observableArrayList(BookStatus.values()));
          statusComboBox.getSelectionModel().select(BookStatus.AVAILABLE);
          
          if (!isEditMode) {
              generatedBookId = "B" + String.format("%03d", (int)(Math.random() * 1000));
          }
    }
    
    public void setBook(Book book) {
        this.book = book;
        this.isEditMode = true;
        this.generatedBookId = book.getBookId();
        
        // Populate form fields
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        isbnField.setText(book.getISBN());
        categoryComboBox.getSelectionModel().select(book.getCategory());
        yearField.setText(String.valueOf(book.getPublicationYear()));
        statusComboBox.getSelectionModel().select(book.getStatus());
    }
    
    @FXML
    private void handleSave() {
        try {
            // Validate required fields
            if (titleField.getText().isEmpty() || authorField.getText().isEmpty()) {
                showAlert("Campos obrigatórios", "Título e Autor são campos obrigatórios");
                return;
            }
            
            // Validate publication year
            int year;
            try {
                year = Integer.parseInt(yearField.getText());
                if (year < 1000 || year > LocalDate.now().getYear() + 1) {
                    showAlert("Ano inválido", "Ano deve ser entre 1000 e " + (LocalDate.now().getYear() + 1));
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Formato inválido", "Ano deve ser um número válido");
                return;
            }
            
            // Create or update book
            if (book == null) {
                book = new Book();
                book.setBookId(generatedBookId);
            }
            
            book.setTitle(titleField.getText());
            book.setAuthor(authorField.getText());
            book.setISBN(isbnField.getText());
            book.setCategory(categoryComboBox.getValue());
            book.setPublicationYear(year);
            book.setStatus(statusComboBox.getValue());
            
            // Save to database
            if (isEditMode) {
                dbService.updateBook(book);
            } else {
                dbService.addBook(book);
            }
            
            closeForm();
            
        } catch (Exception e) {
            showAlert("Erro", "Ocorreu um erro: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleCancel() {
        book = null;
        closeForm();
    }
    
    private void closeForm() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    public Book getBook() {
        return book;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}