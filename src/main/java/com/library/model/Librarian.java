package com.library.model;

import java.time.LocalDate;

public class Librarian extends Employee {
    private String specialization;
    
    public Librarian() {
        // Default constructor
    }
    
    public Librarian(String userId, String name, String email, String phoneNumber, 
                    String address, String employeeId, String position, 
                    LocalDate dateHired, double salary, String specialization) {
        super(userId, name, email, phoneNumber, address, employeeId, position, dateHired, salary);
        this.specialization = specialization;
    }
    
    // Getters and setters
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    // Librarian-specific methods
    public void addBook(Book book, LibraryDatabase database) {
        database.addBook(book);
    }
    
    public void removeBook(Book book, LibraryDatabase database) {
        database.removeBook(book);
    }
    
    public void issueBook(Book book, Student student) {
        if (book.isAvailable()) {
            student.borrowBook(book);
        }
    }
    
    public void returnBook(Book book, Student student) {
        student.returnBook(book);
    }
    
    public void updateBookInfo(Book book, LibraryDatabase database) {
        database.updateBook(book);
    }
    
    public void registerNewUser(User user, LibraryDatabase database) {
        database.addUser(user);
    }
    
    @Override
    public String getUserType() {
        return "Librarian";
    }
    
    @Override
    public String toString() {
        return "Librarian{" +
                "name='" + getName() + '\'' +
                ", employeeId='" + getEmployeeId() + '\'' +
                ", specialization='" + specialization + '\'' +
                '}';
    }
}