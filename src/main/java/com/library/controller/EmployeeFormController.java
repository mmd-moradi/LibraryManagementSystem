package com.library.controller;

import com.library.model.Employee;
import com.library.service.LibraryDatabaseService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EmployeeFormController {
    @FXML
    private Text formTitleText;
    
    @FXML
    private TextField userIdField;
    
    @FXML
    private TextField employeeIdField;
    
    @FXML
    private TextField nameField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField phoneField;
    
    @FXML
    private TextField addressField;
    
    @FXML
    private ComboBox<String> positionComboBox;
    
    @FXML
    private TextField salaryField;
    
    @FXML
    private DatePicker dateHiredPicker;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Label messageLabel;
    
    private Employee employee;
    private boolean isEditMode = false;
    private LibraryDatabaseService dbService = new LibraryDatabaseService();
    
    @FXML
    private void initialize() {
        positionComboBox.setItems(FXCollections.observableArrayList(
            "Bibliotecário", "Bibliotecário Assistente", "Auxiliar", "Gerente", 
            "Administrador", "Suporte de TI", "Outro"
        ));
        
        dateHiredPicker.setValue(LocalDate.now());
        
        // Only generate IDs for new employees
        if (employee == null) {
            userIdField.setText("U" + String.format("%03d", (int)(Math.random() * 1000)));
            employeeIdField.setText("E" + String.format("%03d", (int)(Math.random() * 1000)));
        }
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
        this.isEditMode = true;
        
        formTitleText.setText("Editar Funcionário");
        
        userIdField.setText(employee.getUserId());
        employeeIdField.setText(employee.getEmployeeId());
        nameField.setText(employee.getName());
        emailField.setText(employee.getEmail());
        phoneField.setText(employee.getPhoneNumber());
        addressField.setText(employee.getAddress());
        positionComboBox.getSelectionModel().select(employee.getPosition());
        
        if (employee.getSalary() > 0) {
            salaryField.setText(String.valueOf(employee.getSalary()));
        }
        
        if (employee.getDateHired() != null) {
            dateHiredPicker.setValue(employee.getDateHired());
        }
        
        userIdField.setDisable(true);
        employeeIdField.setDisable(true);
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
            
            if (positionComboBox.getValue() == null) {
                showAlert("Erro", "Selecione um cargo");
                return;
            }
            
            // Validate salary
            double salary = 0;
            if (!salaryField.getText().isEmpty()) {
                try {
                    salary = Double.parseDouble(salaryField.getText());
                    if (salary <= 0) {
                        showAlert("Erro", "Salário deve ser maior que zero");
                        return;
                    }
                } catch (NumberFormatException e) {
                    showAlert("Erro", "Salário deve ser um número válido");
                    return;
                }
            }
            
            // Create or update employee
            if (employee == null) {
                employee = new Employee();
                employee.setUserId(userIdField.getText());
                employee.setEmployeeId(employeeIdField.getText());
            }
            
            employee.setName(nameField.getText());
            employee.setEmail(emailField.getText());
            employee.setPhoneNumber(phoneField.getText());
            employee.setAddress(addressField.getText());
            employee.setPosition(positionComboBox.getValue());
            employee.setSalary(salary);
            employee.setDateHired(dateHiredPicker.getValue());
            
            // Save to database
            if (isEditMode) {
                dbService.updateUser(employee);
            } else {
                dbService.addUser(employee);
            }
            
            closeForm();
            
        } catch (Exception e) {
            showAlert("Erro", "Ocorreu um erro: " + e.getMessage());
        }
    }
    
    private boolean isValidEmail(String email) {
      return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    @FXML
    private void handleCancel() {
        employee = null;
        closeForm();
    }
    
    private void closeForm() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    public Employee getEmployee() {
        return employee;
    }

    private void showAlert(String title, String message) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(title);
      alert.setHeaderText(null);
      alert.setContentText(message);
      alert.showAndWait();
    }
}