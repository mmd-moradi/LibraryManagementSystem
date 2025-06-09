package com.library.controller;

import com.library.model.Employee;
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
    
    @FXML
    private void initialize() {
        positionComboBox.setItems(FXCollections.observableArrayList(
                "Bibliotecário", "Bibliotecário Assistente", "Auxiliar", "Gerente", "Administrador", "Suporte de TI", "Outro"
        ));
        
        dateHiredPicker.setValue(LocalDate.now());
        
        if (!isEditMode) {
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
            if (nameField.getText().isEmpty() || emailField.getText().isEmpty()) {
                messageLabel.setText("Nome e Email são campos obrigatórios");
                return;
            }
            
            if (positionComboBox.getValue() == null) {
                messageLabel.setText("Por favor, selecione um cargo");
                return;
            }
            
            double salary = 0;
            if (!salaryField.getText().isEmpty()) {
                try {
                    salary = Double.parseDouble(salaryField.getText());
                    if (salary < 0) {
                        messageLabel.setText("Salário não pode ser negativo");
                        return;
                    }
                } catch (NumberFormatException e) {
                    messageLabel.setText("Salário deve ser um número válido");
                    return;
                }
            }
            
            if (employee == null) {
                employee = new Employee();
            }
            
            employee.setUserId(userIdField.getText());
            employee.setEmployeeId(employeeIdField.getText());
            employee.setName(nameField.getText());
            employee.setEmail(emailField.getText());
            employee.setPhoneNumber(phoneField.getText());
            employee.setAddress(addressField.getText());
            employee.setPosition(positionComboBox.getValue());
            employee.setSalary(salary);
            employee.setDateHired(dateHiredPicker.getValue());
            
            closeForm();
            
        } catch (Exception e) {
            messageLabel.setText("Erro: " + e.getMessage());
        }
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
}