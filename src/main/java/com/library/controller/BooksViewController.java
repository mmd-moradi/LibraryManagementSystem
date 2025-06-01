package com.library.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import com.library.model.Book;
import com.library.model.BookStatus;

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
        // Initialize the ComboBox
        filterComboBox.setItems(FXCollections.observableArrayList(
                "All", "Title", "Author", "ISBN", "Category", "Available Only"));
        filterComboBox.getSelectionModel().selectFirst();
        
        // Set up the table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Add some dummy data
        loadDummyData();
        
        // Set the items to the table
        booksTable.setItems(booksList);
    }
    
    private void loadDummyData() {
        // This will be replaced with actual data from the database later
        booksList.add(new Book("B001", "The Great Gatsby", "F. Scott Fitzgerald", "978-0743273565", "Fiction", 1925, BookStatus.AVAILABLE));
        booksList.add(new Book("B002", "To Kill a Mockingbird", "Harper Lee", "978-0061120084", "Fiction", 1960, BookStatus.AVAILABLE));
        booksList.add(new Book("B003", "1984", "George Orwell", "978-0451524935", "Dystopian", 1949, BookStatus.BORROWED));
        booksList.add(new Book("B004", "Pride and Prejudice", "Jane Austen", "978-0141439518", "Classic", 1813, BookStatus.AVAILABLE));
        booksList.add(new Book("B005", "The Hobbit", "J.R.R. Tolkien", "978-0547928227", "Fantasy", 1937, BookStatus.RESERVED));
    }
    
    @FXML
    private void handleSearch() {
        // This will be implemented later
        System.out.println("Search for: " + searchField.getText() + 
                           " with filter: " + filterComboBox.getValue());
    }
    
    @FXML
    private void handleClear() {
        searchField.clear();
        filterComboBox.getSelectionModel().selectFirst();
        booksTable.setItems(booksList); // Reset to all books
    }
    
    @FXML
    private void handleAddBook() {
        // This will be implemented later
        System.out.println("Add Book clicked");
    }
    
    @FXML
    private void handleEditBook() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            System.out.println("Edit Book: " + selectedBook.getTitle());
        } else {
            System.out.println("No book selected");
        }
    }
    
    @FXML
    private void handleDeleteBook() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            booksList.remove(selectedBook);
            System.out.println("Deleted book: " + selectedBook.getTitle());
        } else {
            System.out.println("No book selected");
        }
    }
    
    @FXML
    private void handleIssueBook() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            System.out.println("Issue Book: " + selectedBook.getTitle());
        } else {
            System.out.println("No book selected");
        }
    }
    
    @FXML
    private void handleReturnBook() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            System.out.println("Return Book: " + selectedBook.getTitle());
        } else {
            System.out.println("No book selected");
        }
    }
}