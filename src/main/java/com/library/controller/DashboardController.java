package com.library.controller;

import com.library.librarymanagementsystem.App;
import com.library.model.Student;
import com.library.model.User;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardController {
    @FXML
    private TabPane tabPane;
    
    @FXML
    private Text totalBooksLabel;
    
    @FXML
    private Text borrowedBooksLabel;
    
    @FXML
    private Text totalUsersLabel;
    
    @FXML
    private Text overdueBooksLabel;
    
    @FXML
    private Text currentUserLabel;
    
    @FXML
    private Text statusLabel;
    
    @FXML
    private Text welcomeSubtext;
    
    @FXML
    private TableView<ActivityData> recentActivitiesTable;
    
    @FXML
    private TableColumn<ActivityData, String> dateColumn;
    
    @FXML
    private TableColumn<ActivityData, String> actionColumn;
    
    @FXML
    private TableColumn<ActivityData, String> detailsColumn;
    
    @FXML
    private TableColumn<ActivityData, String> userColumn;
    
    private User loggedInUser;
    
    // Class to hold activity data
    public static class ActivityData {
        private final LocalDateTime dateTime;
        private final String action;
        private final String details;
        private final String user;
        
        public ActivityData(LocalDateTime dateTime, String action, String details, String user) {
            this.dateTime = dateTime;
            this.action = action;
            this.details = details;
            this.user = user;
        }
        
        public LocalDateTime getDateTime() { return dateTime; }
        public String getAction() { return action; }
        public String getDetails() { return details; }
        public String getUser() { return user; }
    }
    
    @FXML
    private void initialize() {
        // Set up the table columns
        dateColumn.setCellValueFactory(data -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return new SimpleStringProperty(data.getValue().getDateTime().format(formatter));
        });
        
        actionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAction()));
        detailsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDetails()));
        userColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUser()));
        
        // Empty table - no data
        recentActivitiesTable.setItems(FXCollections.observableArrayList());
        
        // Create demo user 
        createDemoUser();
        
        // Set current date
        welcomeSubtext.setText("Data atual: 09/06/2025");
    }
    
    private void createDemoUser() {
        Student student = new Student();
        student.setUserId("U001");
        student.setStudentId("S001");
        student.setName("mmd-moradi"); // Updated to match login username
        student.setEmail("mmd-moradi@exemplo.com");
        student.setPhoneNumber("(11) 98765-4321");
        student.setAddress("Av. Paulista, 123 - São Paulo, SP");
        student.setDepartment("Ciência da Computação");
        
        this.loggedInUser = student;
        currentUserLabel.setText("mmd-moradi"); // Updated to match login username
    }
    
    @FXML
    private void handleLogout() {
        try {
            App.setRoot("login");
        } catch (IOException e) {
            statusLabel.setText("Erro ao fazer logout: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleExit() {
        Platform.exit();
    }
    
    @FXML
    private void showAllBooks() {
        openTab("Livros", "books_view");
    }
    
    @FXML
    private void showAddBook() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/book_form.fxml"));
            Scene scene = new Scene(loader.load());
            
            Stage stage = new Stage();
            stage.setTitle("Adicionar Livro");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            BookFormController controller = loader.getController();
            if (controller.getBook() != null) {
                statusLabel.setText("Livro adicionado com sucesso");
            }
            
        } catch (IOException e) {
            statusLabel.setText("Erro ao abrir formulário: " + e.getMessage());
        }
    }
    
    @FXML
    private void showSearchBooks() {
        statusLabel.setText("Recurso 'Pesquisar Livros' ainda não implementado");
    }
    
    @FXML
    private void showAllUsers() {
        openTab("Usuários", "users_view");
    }
    
    @FXML
    private void showAddUser() {
        statusLabel.setText("Recurso 'Adicionar Usuário' ainda não implementado");
    }
    
    @FXML
    private void showSearchUsers() {
        statusLabel.setText("Recurso 'Pesquisar Usuários' ainda não implementado");
    }
    
    @FXML
    private void showIssueBook() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/issue_book.fxml"));
            Scene scene = new Scene(loader.load());
            
            Stage stage = new Stage();
            stage.setTitle("Emprestar Livro");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
        } catch (IOException e) {
            statusLabel.setText("Erro ao abrir formulário de empréstimo: " + e.getMessage());
        }
    }
    
    @FXML
    private void showReturnBook() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/return_book.fxml"));
            Scene scene = new Scene(loader.load());
            
            Stage stage = new Stage();
            stage.setTitle("Devolver Livro");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
        } catch (IOException e) {
            statusLabel.setText("Erro ao abrir formulário de devolução: " + e.getMessage());
        }
    }
    
    @FXML
    private void showCurrentBorrows() {
        statusLabel.setText("Recurso 'Empréstimos Atuais' ainda não implementado");
    }
    
    @FXML
    private void showOverdueBooks() {
        openTab("Livros Atrasados", "overdue_books_report");
    }

    @FXML
    private void showPopularBooks() {
        openTab("Livros Populares", "popular_books_report");
    }
    
    @FXML
    private void showUserProfile() {
        if (loggedInUser != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user_profile.fxml"));
                Scene scene = new Scene(loader.load());
                
                UserProfileController controller = loader.getController();
                controller.setUser(loggedInUser);
                
                Stage stage = new Stage();
                stage.setTitle("Perfil do Usuário");
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                
            } catch (IOException e) {
                statusLabel.setText("Erro ao abrir perfil do usuário: " + e.getMessage());
            }
        } else {
            statusLabel.setText("Nenhum usuário logado");
        }
    }
    
    @FXML
    private void showAbout() {
        statusLabel.setText("Sistema de Gerenciamento de Biblioteca v1.0");
    }
    

    private void openTab(String title, String fxmlName) {
        try {
            for (Tab tab : tabPane.getTabs()) {
                if (tab.getText().equals(title)) {
                    tabPane.getSelectionModel().select(tab);
                    return;
                }
            }
            
            Tab tab = new Tab(title);
            tab.setContent(FXMLLoader.load(getClass().getResource("/fxml/" + fxmlName + ".fxml")));
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            
        } catch (IOException e) {
            statusLabel.setText("Erro ao carregar aba: " + e.getMessage());
        }
    }

}