package com.library.controller;

import com.library.model.Book;
import com.library.model.BookStatus;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BookFormController {
    @FXML
    private Text formTitleText;
    
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
    
    private Book book;
    private boolean isEditMode = false;
    
    @FXML
    private void initialize() {
        categoryComboBox.setItems(FXCollections.observableArrayList(
                "Ficção", "Não-Ficção", "Ciência", "História", "Biografia", 
                "Tecnologia", "Referência", "Infantil", "Fantasia", "Mistério", 
                "Romance", "Auto-Ajuda", "Viagem", "Arte", "Outro"
        ));
        
        statusComboBox.setItems(FXCollections.observableArrayList(
                BookStatus.values()
        ));
        statusComboBox.getSelectionModel().select(BookStatus.AVAILABLE);
        
        if (!isEditMode) {
            bookIdField.setText("B" + String.format("%03d", (int)(Math.random() * 1000)));
        }
    }
    
    public void setBook(Book book) {
        this.book = book;
        this.isEditMode = true;
        
        formTitleText.setText("Editar Livro");
        
        bookIdField.setText(book.getBookId());
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        isbnField.setText(book.getISBN());
        categoryComboBox.getSelectionModel().select(book.getCategory());
        yearField.setText(String.valueOf(book.getPublicationYear()));
        statusComboBox.getSelectionModel().select(book.getStatus());
        
        bookIdField.setDisable(true);
    }
    
    @FXML
    private void handleSave() {
        try {
            if (titleField.getText().isEmpty() || authorField.getText().isEmpty()) {
                messageLabel.setText("Título e Autor são campos obrigatórios");
                return;
            }
            
            int year;
            try {
                year = Integer.parseInt(yearField.getText());
                if (year < 1000 || year > 9999) {
                    messageLabel.setText("Ano deve ser um número válido de 4 dígitos");
                    return;
                }
            } catch (NumberFormatException e) {
                messageLabel.setText("Ano deve ser um número válido");
                return;
            }
            
            if (book == null) {
                book = new Book();
            }
            
            book.setBookId(bookIdField.getText());
            book.setTitle(titleField.getText());
            book.setAuthor(authorField.getText());
            book.setISBN(isbnField.getText());
            book.setCategory(categoryComboBox.getValue());
            book.setPublicationYear(year);
            book.setStatus(statusComboBox.getValue());
            
            closeForm();
            
        } catch (Exception e) {
            messageLabel.setText("Erro: " + e.getMessage());
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
}