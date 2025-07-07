package com.library.controller;

import com.library.model.*;
import com.library.service.LibraryDatabaseService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BorrowingsViewController implements DashboardController.BaseController {

    @FXML private TableView<Borrowing> borrowingsTable;
    @FXML private TableColumn<Borrowing, String> borrowingIdColumn;
    @FXML private TableColumn<Borrowing, String> bookIdColumn;
    @FXML private TableColumn<Borrowing, String> bookTitleColumn;
    @FXML private TableColumn<Borrowing, String> userIdColumn;
    @FXML private TableColumn<Borrowing, String> userNameColumn;
    @FXML private TableColumn<Borrowing, LocalDate> borrowDateColumn;
    @FXML private TableColumn<Borrowing, LocalDate> dueDateColumn;
    @FXML private TableColumn<Borrowing, String> statusColumn;
    
    @FXML private TextField searchField;
    @FXML private ComboBox<String> searchTypeComboBox;
    
    private LibraryManagementSystem librarySystem;
    private LibraryDatabaseService dbService;
    private User loggedInUser;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private ObservableList<Borrowing> borrowingsList = FXCollections.observableArrayList();
    
    @FXML
    private void initialize() {
        dbService = new LibraryDatabaseService();
        
        
        searchTypeComboBox.setItems(FXCollections.observableArrayList(
                "ID Empréstimo", "ID Livro", "Título do Livro", "ID Usuário", "Nome do Usuário", "Status"));
        searchTypeComboBox.getSelectionModel().selectFirst();
        
        
        borrowingIdColumn.setCellValueFactory(new PropertyValueFactory<>("borrowingId"));
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        
        bookTitleColumn.setCellValueFactory(cellData -> {
            Book book = dbService.getBookById(cellData.getValue().getBookId());
            return new SimpleStringProperty(book != null ? book.getTitle() : "N/A");
        });
        
        userNameColumn.setCellValueFactory(cellData -> {
            User user = dbService.getUserById(cellData.getValue().getUserId());
            return new SimpleStringProperty(user != null ? user.getName() : "N/A");
        });
        
        
        borrowDateColumn.setCellFactory(column -> new TableCell<Borrowing, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(dateFormatter));
                }
            }
        });
        
        dueDateColumn.setCellFactory(column -> new TableCell<Borrowing, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(dateFormatter));
                }
            }
        });
        
        
        statusColumn.setCellFactory(column -> new TableCell<Borrowing, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    if (status.equals("ACTIVE")) {
                        Borrowing borrowing = getTableView().getItems().get(getIndex());
                        if (borrowing.getDueDate().isBefore(LocalDate.now())) {
                            setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                            setText("ATRASADO");
                        } else {
                            setStyle("-fx-text-fill: green;");
                        }
                    } else if (status.equals("RETURNED")) {
                        setStyle("-fx-text-fill: blue;");
                        setText("DEVOLVIDO");
                    }
                }
            }
        });
        
        loadBorrowings();
    }

    @Override
    public void setLibrarySystem(LibraryManagementSystem librarySystem) {
        this.librarySystem = librarySystem;
    }

    @Override
    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
    
    private void loadBorrowings() {
        List<Borrowing> borrowings = dbService.getActiveBorrowings();
        borrowingsList.setAll(borrowings);
        borrowingsTable.setItems(borrowingsList);
    }
    
    @FXML
    private void handleBack() {
        
        TabPane tabPane = (TabPane) borrowingsTable.getScene().lookup("#tabPane");
        if (tabPane != null && tabPane.getTabs().size() > 1) {
            tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedItem());
        }
    }
    
    @FXML
    private void handleRefresh() {
        loadBorrowings();
    }
    
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase().trim();
        String searchType = searchTypeComboBox.getValue();
        
        if (searchText.isEmpty()) {
            loadBorrowings();
            return;
        }
        
        List<Borrowing> allBorrowings = dbService.getActiveBorrowings();
        ObservableList<Borrowing> filteredList = FXCollections.observableArrayList();
        
        for (Borrowing borrowing : allBorrowings) {
            boolean matches = false;
            
            switch (searchType) {
                case "ID Empréstimo":
                    matches = borrowing.getBorrowingId().toLowerCase().contains(searchText);
                    break;
                case "ID Livro":
                    matches = borrowing.getBookId().toLowerCase().contains(searchText);
                    break;
                case "Título do Livro":
                    Book book = dbService.getBookById(borrowing.getBookId());
                    if (book != null) {
                        matches = book.getTitle().toLowerCase().contains(searchText);
                    }
                    break;
                case "ID Usuário":
                    matches = borrowing.getUserId().toLowerCase().contains(searchText);
                    break;
                case "Nome do Usuário":
                    User user = dbService.getUserById(borrowing.getUserId());
                    if (user != null) {
                        matches = user.getName().toLowerCase().contains(searchText);
                    }
                    break;
                case "Status":
                    matches = borrowing.getStatus().toLowerCase().contains(searchText);
                    break;
            }
            
            if (matches) {
                filteredList.add(borrowing);
            }
        }
        
        borrowingsTable.setItems(filteredList);
    }
    
    @FXML
    private void handleClear() {
        searchField.clear();
        loadBorrowings();
    }
    
    @FXML
    private void handleIssueBook() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/issue_book.fxml"));
            Parent root = loader.load();
            
            IssueBookController controller = loader.getController();
            
            Stage stage = new Stage();
            stage.setTitle("Emprestar Livro");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(borrowingsTable.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            
            loadBorrowings();
            
        } catch (IOException e) {
            showAlert("Erro", "Erro ao abrir formulário: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleReturnBook() {
        Borrowing selectedBorrowing = borrowingsTable.getSelectionModel().getSelectedItem();
        if (selectedBorrowing == null) {
            
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/return_book.fxml"));
                Parent root = loader.load();
                
                ReturnBookController controller = loader.getController();
                
                Stage stage = new Stage();
                stage.setTitle("Devolver Livro");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(borrowingsTable.getScene().getWindow());
                stage.setScene(new Scene(root));
                stage.showAndWait();
                
                
                loadBorrowings();
                
            } catch (IOException e) {
                showAlert("Erro", "Erro ao abrir formulário: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            
            if (selectedBorrowing.getStatus().equals("RETURNED")) {
                showAlert("Aviso", "Este livro já foi devolvido.");
                return;
            }
            
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/return_book.fxml"));
                Parent root = loader.load();
                
                ReturnBookController controller = loader.getController();
                
                TextField bookIdField = (TextField) root.lookup("#bookIdField");
                if (bookIdField != null) {
                    bookIdField.setText(selectedBorrowing.getBookId());
                    
                    Button searchButton = (Button) root.lookup("#searchButton");
                    if (searchButton != null) {
                        searchButton.fire();
                    }
                }
                
                Stage stage = new Stage();
                stage.setTitle("Devolver Livro");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(borrowingsTable.getScene().getWindow());
                stage.setScene(new Scene(root));
                stage.showAndWait();
                
                
                loadBorrowings();
                
            } catch (IOException e) {
                showAlert("Erro", "Erro ao abrir formulário: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}