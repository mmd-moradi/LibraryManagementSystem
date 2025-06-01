package com.library.model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private String studentId;
    private String department;
    private List<Book> borrowedBooks;
    
    public Student() {
        this.borrowedBooks = new ArrayList<>();
    }
    
    public Student(String userId, String name, String email, String phoneNumber, 
                  String address, String studentId, String department) {
        super(userId, name, email, phoneNumber, address);
        this.studentId = studentId;
        this.department = department;
        this.borrowedBooks = new ArrayList<>();
    }
    
    // Getters and setters
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
    
    public void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
    
    // Student-specific methods
    public void borrowBook(Book book) {
        if (book.isAvailable() && borrowedBooks.size() < 5) {
            book.borrowBook(this, 14); // Students can borrow for 14 days
            borrowedBooks.add(book);
        }
    }
    
    public void returnBook(Book book) {
        if (borrowedBooks.contains(book)) {
            book.returnBook();
            borrowedBooks.remove(book);
        }
    }
    
    public int getNumberOfBorrowedBooks() {
        return borrowedBooks.size();
    }
    
    public List<Book> getOverdueBooks() {
        List<Book> overdueBooks = new ArrayList<>();
        for (Book book : borrowedBooks) {
            if (book.isOverdue()) {
                overdueBooks.add(book);
            }
        }
        return overdueBooks;
    }
    
    @Override
    public String getUserType() {
        return "Student";
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "name='" + getName() + '\'' +
                ", studentId='" + studentId + '\'' +
                ", department='" + department + '\'' +
                ", borrowedBooks=" + borrowedBooks.size() +
                '}';
    }
}