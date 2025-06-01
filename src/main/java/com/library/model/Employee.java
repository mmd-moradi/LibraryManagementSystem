package com.library.model;

import java.time.LocalDate;

public class Employee extends User {
    private String employeeId;
    private String position;
    private LocalDate dateHired;
    private double salary;
    
    public Employee() {
        // Default constructor
    }
    
    public Employee(String userId, String name, String email, String phoneNumber, 
                   String address, String employeeId, String position, 
                   LocalDate dateHired, double salary) {
        super(userId, name, email, phoneNumber, address);
        this.employeeId = employeeId;
        this.position = position;
        this.dateHired = dateHired;
        this.salary = salary;
    }
    
    // Getters and setters
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public LocalDate getDateHired() {
        return dateHired;
    }
    
    public void setDateHired(LocalDate dateHired) {
        this.dateHired = dateHired;
    }
    
    public double getSalary() {
        return salary;
    }
    
    public void setSalary(double salary) {
        this.salary = salary;
    }
    
    @Override
    public String getUserType() {
        return "Employee";
    }
    
    @Override
    public String toString() {
        return "Employee{" +
                "name='" + getName() + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}