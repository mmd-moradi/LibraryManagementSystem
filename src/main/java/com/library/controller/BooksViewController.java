package com.library.controller;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.LibraryManagementSystem;
import com.library.model.User;
import com.library.service.LibraryDatabaseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class BooksViewController implements DashboardController.BaseController {

    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, String> bookIdColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> isbnColumn;
    @FXML private TableColumn<Book, String> categoryColumn;
    @FXML private TableColumn<Book, Integer> yearColumn;
    @FXML private TableColumn<Book, BookStatus> statusColumn;
    
    @FXML private TextField searchField;
    @FXML private ComboBox<String> searchTypeComboBox;
    
    private LibraryManagementSystem librarySystem;
    private LibraryDatabaseService dbService;
    private User loggedInUser;
    private BookStatus filterStatus;
    
    private ObservableList<Book> books = FXCollections.observableArrayList();
    
    @FXML
    private void initialize() {
        dbService = new LibraryDatabaseService();
        
        // Initialize the ComboBox
        searchTypeComboBox.setItems(FXCollections.observableArrayList(
                "Título", "Autor", "ISBN", "Categoria"));
        searchTypeComboBox.getSelectionModel().selectFirst();
        
        // Set up the columns
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Load the books
        loadBooks();
        
        // Set up the search field to filter as you type
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchBooks();
        });
    }

    @Override
    public void setLibrarySystem(LibraryManagementSystem librarySystem) {
        this.librarySystem = librarySystem;
    }

    @Override
    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
    
    public void setFilter(BookStatus status) {
        this.filterStatus = status;
        loadBooks();
    }
    
    @FXML
    private void searchBooks() {
        String searchText = searchField.getText().toLowerCase().trim();
        String searchType = searchTypeComboBox.getSelectionModel().getSelectedItem();
        
        if (searchText.isEmpty()) {
            loadBooks(); // Reset to show all books
            return;
        }
        
        List<Book> allBooks = dbService.getAllBooks();
        List<Book> filteredBooks;
        
        switch (searchType) {
            case "Título":
                filteredBooks = allBooks.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
                break;
            case "Autor":
                filteredBooks = allBooks.stream()
                    .filter(book -> book.getAuthor().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
                break;
            case "ISBN":
                filteredBooks = allBooks.stream()
                    .filter(book -> book.getISBN() != null && 
                                     book.getISBN().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
                break;
            case "Categoria":
                filteredBooks = allBooks.stream()
                    .filter(book -> book.getCategory() != null && 
                                     book.getCategory().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
                break;
            default:
                filteredBooks = allBooks;
        }
        
        // Apply status filter if set
        if (filterStatus != null) {
            filteredBooks = filteredBooks.stream()
                .filter(book -> book.getStatus() == filterStatus)
                .collect(Collectors.toList());
        }
        
        books.setAll(filteredBooks);
    }
    
    private void loadBooks() {
        List<Book> allBooks = dbService.getAllBooks();
        
        if (filterStatus != null) {
            allBooks = allBooks.stream()
                .filter(book -> book.getStatus() == filterStatus)
                .collect(Collectors.toList());
        }
        
        books.setAll(allBooks);
        booksTable.setItems(books);
    }
    
    @FXML
    private void handleRefresh() {
        loadBooks();
    }

    @FXML
    private void handleBack() {
        // Get the tab pane and close the current tab
        TabPane tabPane = (TabPane) booksTable.getScene().lookup("#tabPane");
        if (tabPane != null && tabPane.getTabs().size() > 1) {
            tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void handleAddBook() {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/book_form.fxml"));
        Parent root = loader.load();
        
        BookFormController controller = loader.getController();
        controller.setLibrarySystem(librarySystem);
        
        Stage stage = new Stage();
        stage.setTitle("Adicionar Livro");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(booksTable.getScene().getWindow());
        stage.setScene(new Scene(root));
        stage.showAndWait();
        
        // Refresh book list after form closes
        loadBooks();
      } catch (IOException e) {
          e.printStackTrace();
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Erro");
          alert.setHeaderText("Erro ao abrir formulário");
          alert.setContentText("Não foi possível abrir o formulário de livro: " + e.getMessage());
          alert.showAndWait();
      }
    }

    @FXML
    private void handleEditBook() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("Selecione um livro para editar");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/book_form.fxml"));
            Parent root = loader.load();
            
            BookFormController controller = loader.getController();
            controller.setLibrarySystem(librarySystem);
            controller.setBook(selectedBook);
            
            Stage stage = new Stage();
            stage.setTitle("Editar Livro");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(booksTable.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            // Refresh book list after form closes
            loadBooks();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erro ao abrir formulário: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteBook() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("Selecione um livro para excluir");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir " + selectedBook.getTitle());
        alert.setContentText("Tem certeza que deseja excluir este livro permanentemente?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    dbService.deleteBook(selectedBook);
                    loadBooks();
                    showInfo("Livro excluído com sucesso");
                } catch (Exception e) {
                    showAlert("Erro ao excluir livro: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

// Add helper method for info alerts
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase().trim();
        String searchType = searchTypeComboBox.getSelectionModel().getSelectedItem();
        
        if (searchText.isEmpty()) {
            loadBooks(); // Reset to show all books
            return;
        }
        
        List<Book> allBooks = dbService.getAllBooks();
        List<Book> filteredBooks;
        
        switch (searchType) {
            case "Título":
                filteredBooks = allBooks.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
                break;
            case "Autor":
                filteredBooks = allBooks.stream()
                    .filter(book -> book.getAuthor().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
                break;
            case "ISBN":
                filteredBooks = allBooks.stream()
                    .filter(book -> book.getISBN() != null && 
                                     book.getISBN().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
                break;
            case "Categoria":
                filteredBooks = allBooks.stream()
                    .filter(book -> book.getCategory() != null && 
                                     book.getCategory().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
                break;
            default:
                filteredBooks = allBooks;
        }
        
        books.setAll(filteredBooks);
    }

    @FXML
    private void handleClear() {
        searchField.clear();
        loadBooks();
    }

    private void showAlert(String message) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Erro");
      alert.setHeaderText(null);
      alert.setContentText(message);
      alert.showAndWait();
    }

}