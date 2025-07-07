package com.library.controller;

import com.library.model.AccountStatus;
import com.library.model.Employee;
import com.library.model.LibraryManagementSystem;
import com.library.model.Student;
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

public class UsersViewController implements DashboardController.BaseController {
    @FXML
    private TextField searchField;
    
    @FXML
    private ComboBox<String> userTypeComboBox;
    
    @FXML
    private TableView<User> usersTable;
    
    @FXML
    private TableColumn<User, String> idColumn;
    
    @FXML
    private TableColumn<User, String> nameColumn;
    
    @FXML
    private TableColumn<User, String> typeColumn;
    
    @FXML
    private TableColumn<User, String> emailColumn;
    
    @FXML
    private TableColumn<User, String> phoneColumn;
    
    @FXML
    private TableColumn<User, AccountStatus> statusColumn;
    
    private ObservableList<User> usersList = FXCollections.observableArrayList();

    private LibraryManagementSystem librarySystem;
    private LibraryDatabaseService dbService;
    private User loggedInUser;
    
    @FXML
    private void initialize() {
        userTypeComboBox.setItems(FXCollections.observableArrayList(
                "Todos", "Estudante", "Funcionário", "Bibliotecário"));
        userTypeComboBox.getSelectionModel().selectFirst();
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        statusColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getAccount() != null) {
                return javafx.beans.binding.Bindings.createObjectBinding(
                        () -> cellData.getValue().getAccount().getStatus());
            }
            return javafx.beans.binding.Bindings.createObjectBinding(
                        () -> AccountStatus.ACTIVE);
        });
        
        loadDummyData();
        
        usersTable.setItems(usersList);
    }
    
    private void loadDummyData() {
        Student student1 = new Student("U001", "João Silva", "joao@exemplo.com", "555-1234", "Rua do Campus 123", "S001", "Ciência da Computação");
        Student student2 = new Student("U002", "Maria Oliveira", "maria@exemplo.com", "555-5678", "Av. Universitária 456", "S002", "Biologia");
        
        Employee employee1 = new Employee();
        employee1.setUserId("U003");
        employee1.setName("Carlos Pereira");
        employee1.setEmail("carlos@biblioteca.com");
        employee1.setPhoneNumber("555-9012");
        employee1.setAddress("Rua da Biblioteca 789");
        employee1.setEmployeeId("E001");
        employee1.setPosition("Bibliotecário");
        
        usersList.addAll(student1, student2, employee1);
    }
    
    @FXML
    private void handleClear() {
        searchField.clear();
        userTypeComboBox.getSelectionModel().selectFirst();
        usersTable.setItems(usersList);
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        String userType = userTypeComboBox.getValue();
        
        List<User> allUsers = dbService.getAllUsers();
        ObservableList<User> filteredList = FXCollections.observableArrayList();
        
        for (User user : allUsers) {
            boolean typeMatch = "Todos".equals(userType) || 
                               user.getDisplayUserType().equals(userType);
            
            boolean textMatch = searchText.isEmpty() ||
                    user.getName().toLowerCase().contains(searchText) ||
                    user.getUserId().toLowerCase().contains(searchText) ||
                    user.getEmail().toLowerCase().contains(searchText);
            
            if (typeMatch && textMatch) {
                filteredList.add(user);
            }
        }
        
        usersTable.setItems(filteredList);
    }
    
    @FXML
    private void handleAddStudent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/student_form.fxml"));
            Parent root = loader.load();
            
            StudentFormController controller = loader.getController();
            
            Stage stage = new Stage();
            stage.setTitle("Adicionar Estudante");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(usersTable.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            // Refresh user list
            loadUsers();
            
        } catch (IOException e) {
            showAlert("Erro", "Erro ao abrir formulário: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleAddEmployee() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/employee_form.fxml"));
            Parent root = loader.load();
            
            EmployeeFormController controller = loader.getController();
            
            Stage stage = new Stage();
            stage.setTitle("Adicionar Funcionário");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(usersTable.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            // Refresh user list
            loadUsers();
            
        } catch (IOException e) {
            showAlert("Erro", "Erro ao abrir formulário: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Erro", "Selecione um usuário para editar");
            return;
        }
        
        try {
            if (selectedUser instanceof Student) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/student_form.fxml"));
                Parent root = loader.load();
                
                StudentFormController controller = loader.getController();
                controller.setStudent((Student) selectedUser);
                
                Stage stage = new Stage();
                stage.setTitle("Editar Estudante");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(usersTable.getScene().getWindow());
                stage.setScene(new Scene(root));
                stage.showAndWait();
                
            } else if (selectedUser instanceof Employee) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/employee_form.fxml"));
                Parent root = loader.load();
                
                EmployeeFormController controller = loader.getController();
                controller.setEmployee((Employee) selectedUser);
                
                Stage stage = new Stage();
                stage.setTitle("Editar Funcionário");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(usersTable.getScene().getWindow());
                stage.setScene(new Scene(root));
                stage.showAndWait();
            }
            
            // Refresh user list
            loadUsers();
            
        } catch (IOException e) {
            showAlert("Erro", "Erro ao abrir formulário: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeleteUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Erro", "Selecione um usuário para excluir");
            return;
        }
        
        // Confirm deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir " + selectedUser.getName());
        alert.setContentText("Tem certeza que deseja excluir este usuário?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    dbService.removeUser(selectedUser);
                    loadUsers();
                    showAlert("Sucesso", "Usuário excluído com sucesso");
                } catch (Exception e) {
                    showAlert("Erro", "Erro ao excluir usuário: " + e.getMessage());
                }
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void setLibrarySystem(LibraryManagementSystem librarySystem) {
        this.librarySystem = librarySystem;
        this.dbService = new LibraryDatabaseService();
        loadUsers();
    }

    private void loadUsers() {
      List<User> users = dbService.getAllUsers();
      usersList.setAll(users);
    }

    public void setLoggedInUser(User user) {
      this.loggedInUser = user;
    }

}