package com.library.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryDatabase {
    private List<Book> books;
    private List<User> users;
    private List<Account> accounts;
    
    public LibraryDatabase() {
        this.books = new ArrayList<>();
        this.users = new ArrayList<>();
        this.accounts = new ArrayList<>();
    }
    
    // Getters and setters
    public List<Book> getBooks() {
        return books;
    }
    
    public void setBooks(List<Book> books) {
        this.books = books;
    }
    
    public List<User> getUsers() {
        return users;
    }
    
    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    public List<Account> getAccounts() {
        return accounts;
    }
    
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
    
    // Book management methods
    public void addBook(Book book) {
        books.add(book);
    }
    
    public void removeBook(Book book) {
        books.remove(book);
    }
    
    public void updateBook(Book book) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getBookId().equals(book.getBookId())) {
                books.set(i, book);
                break;
            }
        }
    }
    
    public Book findBookById(String bookId) {
        for (Book book : books) {
            if (book.getBookId().equals(bookId)) {
                return book;
            }
        }
        return null;
    }
    
    public List<Book> findBooksByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public List<Book> findBooksByAuthor(String author) {
        return books.stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public List<Book> findAvailableBooks() {
        return books.stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }
    
    // User management methods
    public void addUser(User user) {
        users.add(user);
    }
    
    public void removeUser(User user) {
        users.remove(user);
    }
    
    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(user.getUserId())) {
                users.set(i, user);
                break;
            }
        }
    }
    
    public User findUserById(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
    
    public List<User> findUsersByName(String name) {
        return users.stream()
                .filter(user -> user.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    // Account management methods
    public void addAccount(Account account) {
        accounts.add(account);
    }
    
    public void removeAccount(Account account) {
        accounts.remove(account);
    }
    
    public void updateAccount(Account account) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountId().equals(account.getAccountId())) {
                accounts.set(i, account);
                break;
            }
        }
    }
    
    public Account findAccountById(String accountId) {
        for (Account account : accounts) {
            if (account.getAccountId().equals(accountId)) {
                return account;
            }
        }
        return null;
    }
    
    public Account findAccountByUsername(String username) {
        for (Account account : accounts) {
            if (account.getUsername().equals(username)) {
                return account;
            }
        }
        return null;
    }
    
    // Statistics methods
    public int getTotalBooks() {
        return books.size();
    }
    
    public int getAvailableBooks() {
        return (int) books.stream()
                .filter(Book::isAvailable)
                .count();
    }
    
    public int getBorrowedBooks() {
        return (int) books.stream()
                .filter(book -> book.getStatus() == BookStatus.BORROWED)
                .count();
    }
    
    public int getTotalUsers() {
        return users.size();
    }
    
    public int getOverdueBooks() {
        return (int) books.stream()
                .filter(Book::isOverdue)
                .count();
    }
}