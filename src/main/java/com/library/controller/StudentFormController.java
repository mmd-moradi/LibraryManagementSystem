package com.library.controller;

import com.library.model.LibraryManagementSystem;
import com.library.model.Student;
import com.library.service.LibraryDatabaseService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StudentFormController {
    @FXML
    private Text formTitleText;
    
    @FXML
    private TextField userIdField;
    
    @FXML
    private TextField studentIdField;
    
    @FXML
    private TextField nameField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField phoneField;
    
    @FXML
    private TextField addressField;
    
    @FXML
    private ComboBox<String> departmentComboBox;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Label messageLabel;
    
    private Student student;
    private boolean isEditMode = false;
    private LibraryManagementSystem librarySystem;
    private LibraryDatabaseService dbService = new LibraryDatabaseService();
    
    public void setLibrarySystem(LibraryManagementSystem librarySystem) {
        this.librarySystem = librarySystem;
    }
    
    @FXML
    private void initialize() {
        departmentComboBox.setItems(FXCollections.observableArrayList(
            "Ciência da Computação", "Matemática", "Física", "Química",
            "Biologia", "Engenharia", "Administração", "Artes", "Humanidades",
            "Medicina", "Direito", "Economia", "Educação", "Psicologia", "Outro"
        ));
        
        // Only generate IDs for new students
        if (student == null) {
            userIdField.setText("U" + String.format("%03d", (int)(Math.random() * 1000)));
            studentIdField.setText("S" + String.format("%03d", (int)(Math.random() * 1000)));
        }
    }
    
    public void setStudent(Student student) {
        this.student = student;
        this.isEditMode = true;
        
        formTitleText.setText("Editar Estudante");
        
        userIdField.setText(student.getUserId());
        studentIdField.setText(student.getStudentId());
        nameField.setText(student.getName());
        emailField.setText(student.getEmail());
        phoneField.setText(student.getPhoneNumber());
        addressField.setText(student.getAddress());
        departmentComboBox.getSelectionModel().select(student.getDepartment());
        
        userIdField.setDisable(true);
        studentIdField.setDisable(true);
    }
    
    @FXML
    private void handleSave() {
        try {
            // Validate required fields
            if (nameField.getText().isEmpty()) {
                showAlert("Erro", "Nome é obrigatório");
                return;
            }
            
            if (emailField.getText().isEmpty() || !isValidEmail(emailField.getText())) {
                showAlert("Erro", "Email inválido");
                return;
            }
            
            if (departmentComboBox.getValue() == null) {
                showAlert("Erro", "Selecione um departamento");
                return;
            }
            
            // Create or update student
            if (student == null) {
                student = new Student();
                student.setUserId(userIdField.getText());
                student.setStudentId(studentIdField.getText());
            }
            
            student.setName(nameField.getText());
            student.setEmail(emailField.getText());
            student.setPhoneNumber(phoneField.getText());
            student.setAddress(addressField.getText());
            student.setDepartment(departmentComboBox.getValue());
            
            // Save to database
            if (isEditMode) {
                dbService.updateUser(student);
            } else {
                dbService.addUser(student);
            }
            
            closeForm();
            
        } catch (Exception e) {
            showAlert("Erro", "Ocorreu um erro: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleCancel() {
        student = null;
        closeForm();
    }
    
    private boolean isValidEmail(String email) {
      return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private void closeForm() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    public Student getStudent() {
        return student;
    }

    private void showAlert(String title, String message) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(title);
      alert.setHeaderText(null);
      alert.setContentText(message);
      alert.showAndWait();
    }
}