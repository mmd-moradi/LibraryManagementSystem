package com.library.model;

import com.library.service.LibraryDatabaseService;
import java.util.List;

public class LibraryDatabase {
    private LibraryDatabaseService databaseService;
    
    public LibraryDatabase() {
        this.databaseService = new LibraryDatabaseService();
    }
    
    
    public List<Book> getBooks() {
        return databaseService.getAllBooks();
    }
    
    public void setBooks(List<Book> books) {
        
        List<Book> existingBooks = getBooks();
        for (Book book : existingBooks) {
            databaseService.removeBook(book);
        }
        for (Book book : books) {
            databaseService.addBook(book);
        }
    }
    
    public void addBook(Book book) {
        databaseService.addBook(book);
    }
    
    public void removeBook(Book book) {
        databaseService.removeBook(book);
    }
    
    public void updateBook(Book book) {
        databaseService.updateBook(book);
    }
    
    public Book findBookById(String bookId) {
        return databaseService.getBookById(bookId);
    }
    
    public List<Book> findBooksByTitle(String title) {
        return databaseService.findBooksByTitle(title);
    }
    
    public List<Book> findBooksByAuthor(String author) {
        return databaseService.findBooksByAuthor(author);
    }
    
    public List<Book> findAvailableBooks() {
        return databaseService.findAvailableBooks();
    }
    
    
    public List<User> getUsers() {
        return databaseService.getAllUsers();
    }
    
    public void setUsers(List<User> users) {
        
        List<User> existingUsers = getUsers();
        for (User user : existingUsers) {
            databaseService.removeUser(user);
        }
        for (User user : users) {
            databaseService.addUser(user);
        }
    }
    
    public void addUser(User user) {
        databaseService.addUser(user);
    }
    
    public void removeUser(User user) {
        databaseService.removeUser(user);
    }
    
    public void updateUser(User user) {
        databaseService.updateUser(user);
    }
    
    public User findUserById(String userId) {
        return databaseService.getUserById(userId);
    }
    
    
    
    
    
    
    public List<Account> getAccounts() {
        return databaseService.getAllAccounts();
    }
    
    public void setAccounts(List<Account> accounts) {
        
        List<Account> existingAccounts = getAccounts();
        for (Account account : existingAccounts) {
            databaseService.removeAccount(account);
        }
        for (Account account : accounts) {
            databaseService.addAccount(account);
        }
    }
    
    public void addAccount(Account account) {
        databaseService.addAccount(account);
    }
    
    public void removeAccount(Account account) {
        databaseService.removeAccount(account);
    }
    
    public void updateAccount(Account account) {
        databaseService.updateAccount(account);
    }
    
    public Account findAccountById(String accountId) {
        return databaseService.getAccountById(accountId);
    }
    
    public Account findAccountByUsername(String username) {
        return databaseService.findAccountByUsername(username);
    }
    
    
    public int getTotalBooks() {
        return databaseService.getTotalBooks();
    }
    
    public int getAvailableBooks() {
        return databaseService.getAvailableBooks();
    }
    
    public int getBorrowedBooks() {
        return databaseService.getBorrowedBooks();
    }
    
    public int getTotalUsers() {
        return databaseService.getTotalUsers();
    }
    
    public int getOverdueBooks() {
        return databaseService.getOverdueBooks();
    }
}