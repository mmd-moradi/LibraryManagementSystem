package com.library.controller;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.LibraryManagementSystem;
import com.library.service.LibraryDatabaseService;
import com.library.model.Librarian;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class BookManagementController {
    @FXML
    private TableView<Book> bookTable;
    
    @FXML
    private TableColumn<Book, String> idColumn;
    
    @FXML
    private TableColumn<Book, String> titleColumn;
    
    @FXML
    private TableColumn<Book, String> authorColumn;
    
    @FXML
    private TableColumn<Book, String> categoryColumn;
    
    @FXML
    private TableColumn<Book, Integer> yearColumn;
    
    @FXML
    private TableColumn<Book, BookStatus> statusColumn;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private ComboBox<String> searchTypeComboBox;
    
    @FXML
    private Button addButton;
    
    @FXML
    private Button editButton;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private Button issueButton;
    
    @FXML
    private Button returnButton;
    
    @FXML
    private Label messageLabel;
    
    private LibraryManagementSystem librarySystem;
    
    private final LibraryDatabaseService dbService = new LibraryDatabaseService();

    @FXML
    private void initialize() {
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        
        searchTypeComboBox.setItems(FXCollections.observableArrayList(
                "Título", "Autor", "ID", "Categoria"
        ));
        searchTypeComboBox.getSelectionModel().selectFirst();
        
        
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        issueButton.setDisable(true);
        returnButton.setDisable(true);
        
        
        bookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean bookSelected = newSelection != null;
            editButton.setDisable(!bookSelected);
            deleteButton.setDisable(!bookSelected);
            
            if (bookSelected) {
                issueButton.setDisable(!newSelection.isAvailable());
                returnButton.setDisable(newSelection.getStatus() != BookStatus.BORROWED);
            } else {
                issueButton.setDisable(true);
                returnButton.setDisable(true);
            }
        });
    }
    
    public void setLibrarySystem(LibraryManagementSystem librarySystem) {
        this.librarySystem = librarySystem;
        loadBooks();
    }
    
    private void loadBooks() {
        List<Book> books = dbService.getAllBooks();
        bookTable.setItems(FXCollections.observableArrayList(books));
    }
    
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim();
        String searchType = searchTypeComboBox.getValue();
        
        List<Book> results;
        
        if (searchText.isEmpty()) {
            results = dbService.getAllBooks();
        } else {
            switch (searchType) {
                case "Título":
                    results = dbService.findBooksByTitle(searchText);
                    break;
                case "Autor":
                    results = dbService.findBooksByAuthor(searchText);
                    break;
                case "ID":
                    Book book = dbService.getBookById(searchText);
                    results = book != null ? List.of(book) : List.of();
                    break;
                case "Categoria":
                    results = dbService.findBooksByCategory(searchText);
                    break;
                default:
                    results = dbService.getAllBooks();
            }
        }
        
        bookTable.setItems(FXCollections.observableArrayList(results));
        messageLabel.setText("Encontrados " + results.size() + " livros");
    }
    
    @FXML
    private void handleAddBook() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/book_form.fxml"));
            Parent bookForm = loader.load();
            
            BookFormController controller = loader.getController();
            
            Stage stage = new Stage();
            stage.setTitle("Adicionar Livro");
            stage.setScene(new Scene(bookForm));
            stage.showAndWait();
            
            loadBooks();
            
        } catch (IOException e) {
            showError("Erro ao abrir o formulário: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleEditBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) return;
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/book_form.fxml"));
            Parent bookForm = loader.load();
            
            BookFormController controller = loader.getController();
            controller.setBook(selectedBook);
            
            Stage stage = new Stage();
            stage.setTitle("Editar Livro");
            stage.setScene(new Scene(bookForm));
            stage.showAndWait();
            
            loadBooks();
            
        } catch (IOException e) {
            showError("Erro ao abrir o formulário: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeleteBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) return;
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir " + selectedBook.getTitle());
        alert.setContentText("Tem certeza que deseja excluir este livro?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    dbService.removeBook(selectedBook);
                    loadBooks();
                    messageLabel.setText("Livro excluído com sucesso");
                } catch (Exception e) {
                    showError("Erro ao excluir livro: " + e.getMessage());
                }
            }
        });
    }
    
    @FXML
    private void handleIssueBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null || !selectedBook.isAvailable()) return;
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/issue_book.fxml"));
            Parent issueForm = loader.load();
            
            
            Stage stage = new Stage();
            stage.setTitle("Emprestar Livro");
            stage.setScene(new Scene(issueForm));
            stage.showAndWait();
            
            loadBooks();
            
        } catch (IOException e) {
            showError("Erro ao abrir o formulário: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleReturnBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null || selectedBook.getStatus() != BookStatus.BORROWED) return;
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/return_book.fxml"));
            Parent returnForm = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Devolver Livro");
            stage.setScene(new Scene(returnForm));
            stage.showAndWait();
            
            loadBooks();
            
        } catch (IOException e) {
            showError("Erro ao abrir o formulário: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleReset() {
        searchField.clear();
        searchTypeComboBox.getSelectionModel().selectFirst();
        loadBooks();
        messageLabel.setText("");
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}