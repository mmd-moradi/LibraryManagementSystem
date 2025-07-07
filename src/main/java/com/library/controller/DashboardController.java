package com.library.controller;

import com.library.model.*;
import com.library.service.LibraryDatabaseService;
import com.library.librarymanagementsystem.App;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {
    
    @FXML private Text welcomeSubtext;
    @FXML private Text totalBooksLabel;
    @FXML private Text borrowedBooksLabel;
    @FXML private Text totalUsersLabel;
    @FXML private Text overdueBooksLabel;
    @FXML private Text currentUserLabel;
    @FXML private Text statusLabel;
    @FXML private TabPane tabPane;
  
    
    private LibraryManagementSystem librarySystem;
    private LibraryDatabaseService dbService;
    private User loggedInUser;
        
    
    private final static String BOOKS_VIEW_FXML = "books_view.fxml";
    private final static String BORROWINGS_VIEW_FXML = "borrowings_view.fxml";
    private final static String BOOK_FORM_FXML = "book_form.fxml";
    private final static String USERS_VIEW_FXML = "users_view.fxml";
    private final static String STUDENT_FORM_FXML = "student_form.fxml";
    private final static String EMPLOYEE_FORM_FXML = "employee_form.fxml";
    private final static String ISSUE_BOOK_FXML = "issue_book.fxml";
    private final static String RETURN_BOOK_FXML = "return_book.fxml";
    private final static String OVERDUE_BOOKS_REPORT_FXML = "overdue_books_report.fxml";
    private final static String POPULAR_BOOKS_REPORT_FXML = "popular_books_report.fxml";
    private final static String USER_PROFILE_FXML = "user_profile.fxml";
    
    @FXML
    private void initialize() {
        System.out.println("Initializing dashboard controller...");
        
        try {
            this.dbService = new LibraryDatabaseService();
            
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            welcomeSubtext.setText("Data atual: " + LocalDate.now().format(formatter));
            
            
            
            statusLabel.setText("Sistema inicializado com sucesso");
        } catch (Exception e) {
            System.err.println("Error initializing dashboard controller: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    public void setLibrarySystem(LibraryManagementSystem librarySystem) {
        this.librarySystem = librarySystem;
    }
    
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        currentUserLabel.setText(user.getName());
        
        
        loadDashboardData();
    }
    
    private void loadDashboardData() {
      try {
          if (dbService == null) {
              dbService = new LibraryDatabaseService();
          }
          
          
          int totalBooks = dbService.getTotalBooks();
          int borrowedBooks = dbService.getBorrowedBooks();
          int totalUsers = dbService.getTotalUsers();
          int overdueBooks = dbService.getOverdueBooks();
          
          totalBooksLabel.setText(String.valueOf(totalBooks));
          borrowedBooksLabel.setText(String.valueOf(borrowedBooks));
          totalUsersLabel.setText(String.valueOf(totalUsers));
          overdueBooksLabel.setText(String.valueOf(overdueBooks));
          
          System.out.println("Dashboard data loaded successfully");
      } catch (Exception e) {
          System.err.println("Error loading dashboard data: " + e.getMessage());
          e.printStackTrace();
      }
  }
    
    
    @FXML
    private void showUserProfile() {
        System.out.println("Showing user profile");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + USER_PROFILE_FXML));
            Parent profileView = loader.load();
            
            UserProfileController controller = loader.getController();
            controller.setUser(loggedInUser);
            
            Stage stage = new Stage();
            stage.setTitle("Perfil do Usuário");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tabPane.getScene().getWindow());
            stage.setScene(new Scene(profileView));
            stage.showAndWait();
            
            setStatus("Perfil visualizado");
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erro ao abrir perfil: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleLogout() {
        System.out.println("Logging out user");
        try {
            
            
            App.setRoot("login");
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erro ao fazer logout: " + e.getMessage());
        }
    }
    
    
    @FXML
    private void showAllBooks() {
        System.out.println("Showing all books");
        setStatus("Carregando todos os livros...");
        openTab("Todos os Livros", BOOKS_VIEW_FXML, BooksViewController.class);
    }
    
    @FXML
    private void showAddBook() {
        System.out.println("Showing add book form");
        setStatus("Formulário para adicionar livro");
        openTab("Adicionar Livro", BOOK_FORM_FXML, BookFormController.class);
    }
    
    @FXML
    private void showSearchBooks() {
        System.out.println("Showing book search");
        setStatus("Pesquisa de livros");
        openTab("Pesquisar Livros", BOOKS_VIEW_FXML, BooksViewController.class); 
    }
    
    
    @FXML
    private void showAllUsers() {
        System.out.println("Showing all users");
        setStatus("Carregando todos os usuários...");
        openTab("Todos os Usuários", USERS_VIEW_FXML, UsersViewController.class);
    }
    
    @FXML
    private void showAddUser() {
        System.out.println("Showing add user form");
        setStatus("Formulário para adicionar usuário");
        
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Adicionar Usuário");
        alert.setHeaderText("Qual tipo de usuário deseja adicionar?");
        alert.setContentText("Escolha o tipo de usuário:");
        
        ButtonType btStudent = new ButtonType("Estudante");
        ButtonType btEmployee = new ButtonType("Funcionário");
        ButtonType btCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(btStudent, btEmployee, btCancel);
        
        alert.showAndWait().ifPresent(response -> {
            if (response == btStudent) {
                openTab("Adicionar Estudante", STUDENT_FORM_FXML, StudentFormController.class);
            } else if (response == btEmployee) {
                openTab("Adicionar Funcionário", EMPLOYEE_FORM_FXML, EmployeeFormController.class);
            }
        });
    }
    
    @FXML
    private void showSearchUsers() {
        System.out.println("Showing user search");
        setStatus("Pesquisa de usuários");
        openTab("Pesquisar Usuários", USERS_VIEW_FXML, UsersViewController.class); 
    }
    
    
    @FXML
    private void showIssueBook() {
        System.out.println("Showing issue book form");
        setStatus("Emprestar livro");
        openTab("Emprestar Livro", ISSUE_BOOK_FXML, IssueBookController.class);
    }
    
    @FXML
    private void showReturnBook() {
        System.out.println("Showing return book form");
        setStatus("Devolver livro");
        openTab("Devolver Livro", RETURN_BOOK_FXML, ReturnBookController.class);
    }
    
    @FXML
    private void showCurrentBorrows() {
        System.out.println("Showing current borrows");
        setStatus("Carregando empréstimos atuais...");
        openTab("Empréstimos Atuais", "borrowings_view.fxml", BorrowingsViewController.class);
    }
    
    
    @FXML
    private void showOverdueBooks() {
        System.out.println("Showing overdue books");
        setStatus("Carregando livros atrasados...");
        openTab("Livros Atrasados", OVERDUE_BOOKS_REPORT_FXML, OverdueReportController.class);
    }
    
    @FXML
    private void showPopularBooks() {
        System.out.println("Showing popular books");
        setStatus("Carregando livros populares...");
        openTab("Livros Populares", POPULAR_BOOKS_REPORT_FXML, PopularBooksController.class);
    }
    
    
    @FXML
    private void showAbout() {
        System.out.println("Showing about dialog");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sobre");
        alert.setHeaderText("Sistema de Gerenciamento de Biblioteca");
        alert.setContentText("Versão 1.0.0\n\nDesenvolvido como parte de um projeto acadêmico.\n\n© 2023 Equipe de Desenvolvimento");
        alert.showAndWait();
        setStatus("Informações sobre o sistema exibidas");
    }
    
    
    private <T> void openTab(String title, String fxmlName, Class<T> controllerClass) {
        openTab(title, fxmlName, controllerClass, null);
    }
    
    
    
    private <T> void openTab(String title, String fxmlName, Class<T> controllerClass, 
                            ControllerCallback<T> callback) {
      try {
          
          for (Tab tab : new ArrayList<>(tabPane.getTabs())) {
              if (tab.getText().equals(title)) {
                  tabPane.getTabs().remove(tab);
              }
          }
          
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlName));
          Parent content = loader.load();
          
          Tab tab = new Tab(title);
          tab.setContent(content);
          tab.setClosable(true);
          
          tabPane.getTabs().add(tab);
          tabPane.getSelectionModel().select(tab);
          
          
          Object controller = loader.getController();
          if (controller instanceof BaseController) {
              BaseController baseController = (BaseController) controller;
              baseController.setLibrarySystem(librarySystem);
              baseController.setLoggedInUser(loggedInUser);
          }
          
          
          if (callback != null && controllerClass.isInstance(controller)) {
              callback.setup(controllerClass.cast(controller));
          }
          
          
          
      } catch (IOException e) {
          showError("Erro ao abrir " + title + ": " + e.getMessage());
      }
    }
    
    private void setStatus(String status) {
        statusLabel.setText(status);
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    
    public static class ActivityRecord {
        private String date;
        private String action;
        private String details;
        private String user;
        
        public ActivityRecord(String date, String action, String details, String user) {
            this.date = date;
            this.action = action;
            this.details = details;
            this.user = user;
        }
        
        public String getDate() { return date; }
        public String getAction() { return action; }
        public String getDetails() { return details; }
        public String getUser() { return user; }
    }
    
    
    @FunctionalInterface
    private interface ControllerCallback<T> {
        void setup(T controller);
    }
    
    
    public interface BaseController {
        void setLibrarySystem(LibraryManagementSystem system);
        void setLoggedInUser(User user);
    }

    public void refreshDashboardData() {
      loadDashboardData();
    }
}