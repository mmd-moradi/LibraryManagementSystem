package com.library.model;

import java.util.ArrayList;
import java.util.List;

public class LibraryManagementSystem {
    private String libraryName;
    private LibraryDatabase database;
    private List<Librarian> librarians;
    private User currentUser;
    
    public LibraryManagementSystem(String libraryName) {
        this.libraryName = libraryName;
        this.database = new LibraryDatabase();
        this.librarians = new ArrayList<>();
    }
    
    // Getters and setters
    public String getLibraryName() {
        return libraryName;
    }
    
    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }
    
    public LibraryDatabase getDatabase() {
        return database;
    }
    
    public void setDatabase(LibraryDatabase database) {
        this.database = database;
    }
    
    public List<Librarian> getLibrarians() {
        return librarians;
    }
    
    public void setLibrarians(List<Librarian> librarians) {
        this.librarians = librarians;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    
    // System operations
    public boolean login(String username, String password) {
        Account account = database.findAccountByUsername(username);
        if (account != null && account.login(password)) {
            currentUser = account.getUser();
            return true;
        }
        return false;
    }
    
    public void logout() {
        if (currentUser != null && currentUser.getAccount() != null) {
            currentUser.getAccount().logout();
        }
        currentUser = null;
    }
    
    public void addLibrarian(Librarian librarian) {
        librarians.add(librarian);
        database.addUser(librarian);
    }
    
    // Book management operations delegated to librarians
    public void addBook(Book book, Librarian librarian) {
        if (isAuthorized(librarian)) {
            librarian.addBook(book, database);
        }
    }
    
    public void issueBook(Book book, Student student, Librarian librarian) {
        if (isAuthorized(librarian)) {
            librarian.issueBook(book, student);
        }
    }
    
    // Helper methods
    private boolean isAuthorized(User user) {
        return user != null && (user instanceof Librarian);
    }
    
    // Statistics methods
    public int getTotalBooks() {
        return database.getTotalBooks();
    }
    
    public int getAvailableBooks() {
        return database.getAvailableBooks();
    }
    
    public int getBorrowedBooks() {
        return database.getBorrowedBooks();
    }
    
    public int getTotalUsers() {
        return database.getTotalUsers();
    }
    
    public int getOverdueBooks() {
        return database.getOverdueBooks();
    }
}