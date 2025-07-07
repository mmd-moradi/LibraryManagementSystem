package com.library.service;

import com.library.dao.*;
import com.library.database.DatabaseConnection;
import com.library.model.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class LibraryDatabaseService {
    private final BookDAO bookDAO = new BookDAO();
    private UserDAO userDAO = new UserDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final BorrowingDAO borrowingDAO = new BorrowingDAO();

    public LibraryDatabaseService() {
    }
    



  public void deleteBook(Book book) {
    Connection conn = null;
    try {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);
        
        
        BorrowingDAO borrowingDAO = new BorrowingDAO();
        borrowingDAO.setConnection(conn);
        
        BookDAO bookDAO = new BookDAO();
        bookDAO.setConnection(conn);
        
        borrowingDAO.deleteByBookId(book.getBookId());
        bookDAO.delete(book);
        
        conn.commit();
    } catch (SQLException e) {
          try {
              if (conn != null) conn.rollback();
          } catch (SQLException ex) {
              System.err.println("Rollback failed: " + ex.getMessage());
          }
          throw new RuntimeException("Error deleting book: " + e.getMessage(), e);
      } finally {
          try {
              if (conn != null) conn.setAutoCommit(true);
          } catch (SQLException e) {
              System.err.println("Error resetting auto-commit: " + e.getMessage());
          }
      }
    } 

    public List<Book> getAllBooks() {
        return bookDAO.getAll();
    }
    
    public Book getBookById(String bookId) {
        Optional<Book> book = bookDAO.get(bookId);
        return book.orElse(null);
    }
    
    public List<Book> findBooksByTitle(String title) {
        return bookDAO.findByTitle(title);
    }
    
    public List<Book> findBooksByAuthor(String author) {
        return bookDAO.findByAuthor(author);
    }
    
    public List<Book> findAvailableBooks() {
        return bookDAO.findAvailableBooks();
    }
    
    public void addBook(Book book) {
        bookDAO.save(book);
    }
    
    public void updateBook(Book book) {
        bookDAO.update(book);
    }
    
    public void removeBook(Book book) {
        bookDAO.delete(book);
    }
    
    
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    public User getUserById(String userId) {
        return userDAO.getUserById(userId).orElse(null);
    }
    
    
    public void addUser(User user) {
        userDAO.addUser(user);
    }
    
    public void updateUser(User user) {
        userDAO.updateUser(user);
    }
    
    public void removeUser(User user) {
        userDAO.deleteUser(user);
    }
    
    public List<Account> getAllAccounts() {
        return accountDAO.getAll();
    }
    
    public Account getAccountById(String accountId) {
        Optional<Account> account = accountDAO.get(accountId);
        return account.orElse(null);
    }
    
    public Account findAccountByUsername(String username) {
        Optional<Account> account = accountDAO.findByUsername(username);
        return account.orElse(null);
    }
    
    public void addAccount(Account account) {
        accountDAO.save(account);
    }
    
    public void updateAccount(Account account) {
        accountDAO.update(account);
    }
    
    public void removeAccount(Account account) {
        accountDAO.delete(account);
    }
    
    
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        for (User user : userDAO.getAllUsers()) {
            if (user instanceof Student) {
                students.add((Student) user);
            }
        }
        return students;
    }
    
    
    public int getTotalBooks() {
        return bookDAO.getAll().size();
    }
    
    public int getAvailableBooks() {
        return bookDAO.getAvailableBookCount();
    }
    
    public int getBorrowedBooks() {
        return bookDAO.getBorrowedBookCount();
    }
    
    public int getTotalUsers() {
        return userDAO.getUserCount();
    }
    
    public int getOverdueBooks() {
        return bookDAO.getOverdueBookCount();
    }

    public void issueBook(Book book, Student student, LocalDate dueDate) {
      
      book.setStatus(BookStatus.BORROWED);
      bookDAO.update(book);
      
      
      Borrowing borrowing = new Borrowing(
          UUID.randomUUID().toString(),
          book.getBookId(),
          student.getUserId(),
          LocalDate.now(),
          dueDate,
          null,
          "ACTIVE"
      );
      borrowingDAO.save(borrowing);
    }

    public void returnBook(Book book) {
      
      book.setStatus(BookStatus.AVAILABLE);
      bookDAO.update(book);
      
      
      Borrowing borrowing = borrowingDAO.getActiveBorrowingForBook(book.getBookId());
      if (borrowing != null) {
          borrowing.setReturnDate(LocalDate.now());
          borrowing.setStatus("RETURNED");
          borrowingDAO.update(borrowing);
      }
    }
    
    public List<Borrowing> getActiveBorrowings() {
      return borrowingDAO.getActiveBorrowings();
    }
    
    public double calculateLateFee(Borrowing borrowing) {
        if (borrowing.getReturnDate() == null || 
            !borrowing.getReturnDate().isAfter(borrowing.getDueDate())) {
            return 0.0;
        }
        
        long daysLate = ChronoUnit.DAYS.between(
            borrowing.getDueDate(), 
            borrowing.getReturnDate()
        );
        return daysLate * 2.0; 
    }
    
    public List<Book> findBooksByCategory(String category) {
      return bookDAO.findByCategory(category);
    }
    
    
    public Borrowing getActiveBorrowingForBook(String bookId) {
        return borrowingDAO.getActiveBorrowingForBook(bookId);
    }

    public void updateBorrowing(Borrowing borrowing) {
        borrowingDAO.update(borrowing);
    }

    public List<Borrowing> getActiveBorrowingsByUser(String userId) {
        return borrowingDAO.getActiveBorrowingsByUser(userId);
    }
}