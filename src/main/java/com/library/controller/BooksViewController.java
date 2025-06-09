package com.library.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.library.model.Book;
import com.library.model.BookStatus;

import java.io.IOException;

public class BooksViewController {
    @FXML
    private TextField searchField;
    
    @FXML
    private ComboBox<String> filterComboBox;
    
    @FXML
    private TableView<Book> booksTable;
    
    @FXML
    private TableColumn<Book, String> idColumn;
    
    @FXML
    private TableColumn<Book, String> titleColumn;
    
    @FXML
    private TableColumn<Book, String> authorColumn;
    
    @FXML
    private TableColumn<Book, String> isbnColumn;
    
    @FXML
    private TableColumn<Book, String> categoryColumn;
    
    @FXML
    private TableColumn<Book, Integer> yearColumn;
    
    @FXML
    private TableColumn<Book, BookStatus> statusColumn;
    
    private ObservableList<Book> booksList = FXCollections.observableArrayList();
    
    @FXML
    private void initialize() {
        filterComboBox.setItems(FXCollections.observableArrayList(
                "Todos", "Título", "Autor", "ISBN", "Categoria", "Apenas Disponíveis"));
        filterComboBox.getSelectionModel().selectFirst();
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        loadDummyData();
        
        booksTable.setItems(booksList);
    }
    
    private void loadDummyData() {
        booksList.add(new Book("B001", "O Grande Gatsby", "F. Scott Fitzgerald", "978-0743273565", "Ficção", 1925, BookStatus.AVAILABLE));
        booksList.add(new Book("B002", "O Sol é Para Todos", "Harper Lee", "978-0061120084", "Ficção", 1960, BookStatus.AVAILABLE));
        booksList.add(new Book("B003", "1984", "George Orwell", "978-0451524935", "Distopia", 1949, BookStatus.BORROWED));
        booksList.add(new Book("B004", "Orgulho e Preconceito", "Jane Austen", "978-0141439518", "Clássico", 1813, BookStatus.AVAILABLE));
        booksList.add(new Book("B005", "O Hobbit", "J.R.R. Tolkien", "978-0547928227", "Fantasia", 1937, BookStatus.RESERVED));
    }
    
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        String filter = filterComboBox.getValue();
        
        ObservableList<Book> filteredList = FXCollections.observableArrayList();
        
        for (Book book : booksList) {
            boolean matches = false;
            
            if ("Todos".equals(filter)) {
                matches = book.getTitle().toLowerCase().contains(searchText) ||
                         book.getAuthor().toLowerCase().contains(searchText) ||
                         book.getISBN().toLowerCase().contains(searchText) ||
                         book.getCategory().toLowerCase().contains(searchText);
            } else if ("Título".equals(filter)) {
                matches = book.getTitle().toLowerCase().contains(searchText);
            } else if ("Autor".equals(filter)) {
                matches = book.getAuthor().toLowerCase().contains(searchText);
            } else if ("ISBN".equals(filter)) {
                matches = book.getISBN().toLowerCase().contains(searchText);
            } else if ("Categoria".equals(filter)) {
                matches = book.getCategory().toLowerCase().contains(searchText);
            } else if ("Apenas Disponíveis".equals(filter)) {
                matches = book.isAvailable() && 
                        (book.getTitle().toLowerCase().contains(searchText) ||
                         book.getAuthor().toLowerCase().contains(searchText) ||
                         book.getISBN().toLowerCase().contains(searchText));
            }
            
            if (matches) {
                filteredList.add(book);
            }
        }
        
        booksTable.setItems(filteredList);
    }
    
    @FXML
    private void handleClear() {
        searchField.clear();
        filterComboBox.getSelectionModel().selectFirst();
        booksTable.setItems(booksList);
    }
    
    @FXML
    private void handleAddBook() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/book_form.fxml"));
            Scene scene = new Scene(loader.load());
            
            Stage stage = new Stage();
            stage.setTitle("Adicionar Livro");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            BookFormController controller = loader.getController();
            Book newBook = controller.getBook();
            
            if (newBook != null) {
                booksList.add(newBook);
                booksTable.refresh();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erro ao abrir formulário de livro: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleEditBook() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/book_form.fxml"));
                Scene scene = new Scene(loader.load());
                
                BookFormController controller = loader.getController();
                controller.setBook(selectedBook);
                
                Stage stage = new Stage();
                stage.setTitle("Editar Livro");
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                
                booksTable.refresh();
                
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erro ao abrir formulário de edição: " + e.getMessage());
            }
        } else {
            showAlert("Por favor, selecione um livro para editar");
        }
    }
    
    @FXML
    private void handleDeleteBook() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Exclusão");
            alert.setHeaderText("Excluir Livro");
            alert.setContentText("Tem certeza que deseja excluir o livro '" + selectedBook.getTitle() + "'?");
            
            if (alert.showAndWait().get() == ButtonType.OK) {
                booksList.remove(selectedBook);
            }
        } else {
            showAlert("Por favor, selecione um livro para excluir");
        }
    }
    
    @FXML
    private void handleIssueBook() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            if (selectedBook.isAvailable()) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/issue_book.fxml"));
                    Scene scene = new Scene(loader.load());
                    
                    IssueBookController controller = loader.getController();
                    // Pré-configurar o ID do livro
                    controller.preloadBookId(selectedBook.getBookId());
                    
                    Stage stage = new Stage();
                    stage.setTitle("Emprestar Livro");
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                    
                    // Atualizar a visualização após empréstimo
                    booksTable.refresh();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Erro ao abrir formulário de empréstimo: " + e.getMessage());
                }
            } else {
                showAlert("Este livro não está disponível para empréstimo");
            }
        } else {
            showAlert("Por favor, selecione um livro para emprestar");
        }
    }
    
    @FXML
    private void handleReturnBook() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            if (selectedBook.getStatus() == BookStatus.BORROWED) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/return_book.fxml"));
                    Scene scene = new Scene(loader.load());
                    
                    ReturnBookController controller = loader.getController();
                    // Pré-configurar o ID do livro
                    controller.preloadBookId(selectedBook.getBookId());
                    
                    Stage stage = new Stage();
                    stage.setTitle("Devolver Livro");
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                    
                    // Atualizar a visualização após devolução
                    booksTable.refresh();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Erro ao abrir formulário de devolução: " + e.getMessage());
                }
            } else {
                showAlert("Este livro não está emprestado atualmente");
            }
        } else {
            showAlert("Por favor, selecione um livro para devolver");
        }
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}