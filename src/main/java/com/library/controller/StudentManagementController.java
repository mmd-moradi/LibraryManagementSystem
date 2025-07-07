package com.library.controller;

import com.library.model.Book;
import com.library.model.Borrowing;
import com.library.model.LibraryManagementSystem;
import com.library.model.Student;
import com.library.service.LibraryDatabaseService;

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
import java.util.stream.Collectors;

public class StudentManagementController {
    @FXML
    private TableView<Student> studentTable;
    
    @FXML
    private TableColumn<Student, String> idColumn;
    
    @FXML
    private TableColumn<Student, String> nameColumn;
    
    @FXML
    private TableColumn<Student, String> departmentColumn;
    
    @FXML
    private TableColumn<Student, Integer> booksColumn;
    
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
    private Button viewDetailsButton;
    
    @FXML
    private Label messageLabel;
    
    private LibraryManagementSystem librarySystem;
    private LibraryDatabaseService dbService = new LibraryDatabaseService();
    
    @FXML
    private void initialize() {
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        
        
        booksColumn.setCellValueFactory(cellData -> {
            List<Borrowing> borrowedBooks = dbService.getActiveBorrowingsByUser(cellData.getValue().getStudentId());
            int count = borrowedBooks != null ? borrowedBooks.size() : 0;
            return javafx.beans.binding.Bindings.createIntegerBinding(() -> count).asObject();
        });
        
        
        searchTypeComboBox.setItems(FXCollections.observableArrayList(
                "Nome", "ID", "Departamento"
        ));
        searchTypeComboBox.getSelectionModel().selectFirst();
        
        
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        viewDetailsButton.setDisable(true);
        
        
        studentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean studentSelected = newSelection != null;
            editButton.setDisable(!studentSelected);
            deleteButton.setDisable(!studentSelected);
            viewDetailsButton.setDisable(!studentSelected);
        });
    }
    
    public void setLibrarySystem(LibraryManagementSystem librarySystem) {
        this.librarySystem = librarySystem;
        loadStudents();
    }
    
    private void loadStudents() {
        if (librarySystem != null) {
            List<Student> students = librarySystem.getDatabase().getUsers().stream()
                    .filter(user -> user instanceof Student)
                    .map(user -> (Student) user)
                    .collect(Collectors.toList());
                    
            studentTable.setItems(FXCollections.observableArrayList(students));
        }
    }
    
    @FXML
    private void handleSearch() {
        if (librarySystem == null) return;
        
        String searchText = searchField.getText().trim().toLowerCase();
        String searchType = searchTypeComboBox.getValue();
        
        List<Student> allStudents = librarySystem.getDatabase().getUsers().stream()
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .collect(Collectors.toList());
        
        List<Student> results;
        
        if (searchText.isEmpty()) {
            results = allStudents;
        } else {
            switch (searchType) {
                case "Nome":
                    results = allStudents.stream()
                            .filter(s -> s.getName().toLowerCase().contains(searchText))
                            .collect(Collectors.toList());
                    break;
                case "ID":
                    results = allStudents.stream()
                            .filter(s -> s.getStudentId().toLowerCase().contains(searchText))
                            .collect(Collectors.toList());
                    break;
                case "Departamento":
                    results = allStudents.stream()
                            .filter(s -> s.getDepartment() != null && 
                                   s.getDepartment().toLowerCase().contains(searchText))
                            .collect(Collectors.toList());
                    break;
                default:
                    results = allStudents;
            }
        }
        
        studentTable.setItems(FXCollections.observableArrayList(results));
        messageLabel.setText("Encontrados " + results.size() + " estudantes");
    }
    
    @FXML
    private void handleAddStudent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/studentForm.fxml"));
            Parent studentForm = loader.load();
            
            StudentFormController controller = loader.getController();
            controller.setLibrarySystem(librarySystem);
            
            Stage stage = new Stage();
            stage.setTitle("Adicionar Estudante");
            stage.setScene(new Scene(studentForm));
            stage.showAndWait();
            
            
            loadStudents();
            
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Erro ao abrir o formulário: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleEditStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) return;
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/studentForm.fxml"));
            Parent studentForm = loader.load();
            
            StudentFormController controller = loader.getController();
            controller.setLibrarySystem(librarySystem);
            controller.setStudent(selectedStudent);
            
            Stage stage = new Stage();
            stage.setTitle("Editar Estudante");
            stage.setScene(new Scene(studentForm));
            stage.showAndWait();
            
            
            loadStudents();
            
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Erro ao abrir o formulário: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeleteStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) return;
        
        
        List<Borrowing> borrowedBooks = dbService.getActiveBorrowingsByUser(selectedStudent.getStudentId());
        if (borrowedBooks != null && !borrowedBooks.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Não é possível excluir");
            alert.setHeaderText("Estudante possui livros emprestados");
            alert.setContentText("Este estudante possui " + borrowedBooks.size() + 
                                " livro(s) emprestado(s). Devolva todos os livros antes de excluir.");
            alert.showAndWait();
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir " + selectedStudent.getName());
        alert.setContentText("Tem certeza que deseja excluir este estudante?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                librarySystem.getDatabase().removeUser(selectedStudent);
                loadStudents();
                messageLabel.setText("Estudante excluído com sucesso");
            }
        });
    }
    
    @FXML
    private void handleViewDetails() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) return;
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userProfile.fxml"));
            Parent profileView = loader.load();
            
            UserProfileController controller = loader.getController();
            controller.setLibrarySystem(librarySystem);
            controller.setUser(selectedStudent);
            
            Stage stage = new Stage();
            stage.setTitle("Perfil do Estudante");
            stage.setScene(new Scene(profileView));
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Erro ao abrir o perfil: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleReset() {
        searchField.clear();
        searchTypeComboBox.getSelectionModel().selectFirst();
        loadStudents();
        messageLabel.setText("");
    }
}