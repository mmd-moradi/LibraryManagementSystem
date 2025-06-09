package com.library.controller;

import com.library.model.Account;
import com.library.model.AccountStatus;
import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.Student;
import com.library.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserProfileController {
    @FXML
    private Label userIdLabel;
    
    @FXML
    private Label nameLabel;
    
    @FXML
    private Label userTypeLabel;
    
    @FXML
    private Label emailLabel;
    
    @FXML
    private Label phoneLabel;
    
    @FXML
    private Label addressLabel;
    
    @FXML
    private Label usernameLabel;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private Label lastLoginLabel;
    
    @FXML
    private TableView<Book> borrowedBooksTable;
    
    @FXML
    private TableColumn<Book, String> bookIdColumn;
    
    @FXML
    private TableColumn<Book, String> titleColumn;
    
    @FXML
    private TableColumn<Book, String> issueDateColumn;
    
    @FXML
    private TableColumn<Book, String> dueDateColumn;
    
    @FXML
    private TableColumn<Book, String> statusColumn;
    
    @FXML
    private TableView<ActivityRecord> activityHistoryTable;
    
    @FXML
    private TableColumn<ActivityRecord, String> dateColumn;
    
    @FXML
    private TableColumn<ActivityRecord, String> activityColumn;
    
    @FXML
    private TableColumn<ActivityRecord, String> detailsColumn;
    
    private User currentUser;
    private ObservableList<Book> borrowedBooks = FXCollections.observableArrayList();
    private ObservableList<ActivityRecord> activities = FXCollections.observableArrayList();
    
    public static class ActivityRecord {
        private LocalDateTime date;
        private String activity;
        private String details;
        
        public ActivityRecord(LocalDateTime date, String activity, String details) {
            this.date = date;
            this.activity = activity;
            this.details = details;
        }
        
        public LocalDateTime getDate() {
            return date;
        }
        
        public String getActivity() {
            return activity;
        }
        
        public String getDetails() {
            return details;
        }
    }
    
    @FXML
    private void initialize() {
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        
        issueDateColumn.setCellValueFactory(cellData -> {
            LocalDate borrowDate = cellData.getValue().getBorrowDate();
            if (borrowDate != null) {
                return new SimpleStringProperty(borrowDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            return new SimpleStringProperty("N/A");
        });
        
        dueDateColumn.setCellValueFactory(cellData -> {
            LocalDate dueDate = cellData.getValue().getDueDate();
            if (dueDate != null) {
                return new SimpleStringProperty(dueDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            return new SimpleStringProperty("N/A");
        });
        
        statusColumn.setCellValueFactory(cellData -> {
            BookStatus status = cellData.getValue().getStatus();
            switch (status) {
                case BORROWED:
                    return new SimpleStringProperty("Emprestado");
                case OVERDUE:
                    return new SimpleStringProperty("Atrasado");
                default:
                    return new SimpleStringProperty(status.toString());
            }
        });
        
        dateColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDate().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        
        activityColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getActivity()));
        
        detailsColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDetails()));
        
        borrowedBooksTable.setItems(borrowedBooks);
        activityHistoryTable.setItems(activities);
    }
    
    public void setUser(User user) {
        this.currentUser = user;
        
        // Preencher informações do usuário
        userIdLabel.setText(user.getUserId());
        nameLabel.setText(user.getName());
        userTypeLabel.setText(getPortugueseUserType(user.getUserType()));
        emailLabel.setText(user.getEmail());
        phoneLabel.setText(user.getPhoneNumber());
        addressLabel.setText(user.getAddress());
        
        // Preencher informações da conta
        Account account = user.getAccount();
        if (account != null) {
            usernameLabel.setText(account.getUsername());
            statusLabel.setText(getPortugueseAccountStatus(account.getStatus()));
            
            LocalDateTime lastLogin = account.getLastLogin();
            if (lastLogin != null) {
                lastLoginLabel.setText(lastLogin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            } else {
                lastLoginLabel.setText("Nunca");
            }
        } else {
            usernameLabel.setText("N/A");
            statusLabel.setText("N/A");
            lastLoginLabel.setText("N/A");
        }
        
        // Carregar livros emprestados (apenas para estudantes)
        if (user instanceof Student) {
            Student student = (Student) user;
            if (student.getBorrowedBooks() != null) {
                borrowedBooks.addAll(student.getBorrowedBooks());
            }
        }
        
        // Carregar dados de atividade de exemplo
        loadSampleActivityData();
    }
    
    private String getPortugueseUserType(String userType) {
        switch (userType) {
            case "Student": return "Estudante";
            case "Employee": return "Funcionário";
            case "Librarian": return "Bibliotecário";
            default: return userType;
        }
    }
    
    private String getPortugueseAccountStatus(AccountStatus status) {
        switch (status) {
            case ACTIVE: return "Ativo";
            case SUSPENDED: return "Suspenso";
            case CLOSED: return "Fechado";
            default: return status.toString();
        }
    }
    
    private void loadSampleActivityData() {
        LocalDateTime now = LocalDateTime.now();
        
        activities.add(new ActivityRecord(now.minusDays(30), "Login", "Login bem-sucedido"));
        activities.add(new ActivityRecord(now.minusDays(28), "Empréstimo", "Emprestado: O Grande Gatsby"));
        activities.add(new ActivityRecord(now.minusDays(21), "Devolução", "Devolvido: O Grande Gatsby"));
        activities.add(new ActivityRecord(now.minusDays(15), "Empréstimo", "Emprestado: 1984"));
        activities.add(new ActivityRecord(now.minusDays(10), "Renovação", "Renovado: 1984 por mais 14 dias"));
        activities.add(new ActivityRecord(now.minusDays(1), "Login", "Login bem-sucedido"));
    }
    
    @FXML
    private void handleEditProfile() {
        // Esta funcionalidade será implementada posteriormente
        showNotImplementedMessage("Editar Perfil");
    }
    
    @FXML
    private void handleChangePassword() {
        // Esta funcionalidade será implementada posteriormente
        showNotImplementedMessage("Alterar Senha");
    }
    
    @FXML
    private void handleClose() {
        Stage stage = (Stage) userIdLabel.getScene().getWindow();
        stage.close();
    }
    
    private void showNotImplementedMessage(String feature) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Recurso não implementado");
        alert.setHeaderText(null);
        alert.setContentText("O recurso '" + feature + "' será implementado em uma versão futura.");
        alert.showAndWait();
    }
}