package com.library.dao;

import com.library.database.DatabaseConnection;
import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.Student;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAO extends BaseDAO implements DAO<Book> {
    
    public BookDAO() {
        super();
    }
    
    @Override
    public Optional<Book> get(String id) {
        try {
            String sql = "SELECT * FROM books WHERE book_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Book book = mapResultSetToBook(rs);
                return Optional.of(book);
            }
        } catch (SQLException e) {
            System.err.println("Error getting book: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Book> getAll() {
        List<Book> books = new ArrayList<>();
        try {
            String sql = "SELECT * FROM books";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            
            while (rs.next()) {
                Book book = mapResultSetToBook(rs);
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all books: " + e.getMessage());
        }
        return books;
    }


    @Override
    public void save(Book book) {
        try {
            String sql = "INSERT INTO books (book_id, title, author, isbn, category, " +
                        "publication_year, status) " +  
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, book.getBookId());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setString(4, book.getISBN());
            stmt.setString(5, book.getCategory());
            stmt.setInt(6, book.getPublicationYear());
            stmt.setString(7, book.getStatus().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving book: " + e.getMessage());
        }
    }

    @Override
    public void update(Book book) {
        try {
            String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, category = ?, " +
                        "publication_year = ?, status = ? " +  
                        "WHERE book_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getISBN());
            stmt.setString(4, book.getCategory());
            stmt.setInt(5, book.getPublicationYear());
            stmt.setString(6, book.getStatus().toString());
            stmt.setString(7, book.getBookId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
        }
    }

    @Override
    public void delete(Book book) {
        try {
            String sql = "DELETE FROM books WHERE book_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, book.getBookId());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting book failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting book: " + e.getMessage(), e);
        }
    }

    public List<Book> findByTitle(String title) {
        List<Book> books = new ArrayList<>();
        try {
            String sql = "SELECT * FROM books WHERE LOWER(title) LIKE ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, "%" + title.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Book book = mapResultSetToBook(rs);
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Error finding books by title: " + e.getMessage());
        }
        return books;
    }

    public List<Book> findByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        try {
            String sql = "SELECT * FROM books WHERE LOWER(author) LIKE ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, "%" + author.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Book book = mapResultSetToBook(rs);
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Error finding books by author: " + e.getMessage());
        }
        return books;
    }

    public List<Book> findAvailableBooks() {
        List<Book> books = new ArrayList<>();
        try {
            String sql = "SELECT * FROM books WHERE status = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, BookStatus.AVAILABLE.toString());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Book book = mapResultSetToBook(rs);
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Error finding available books: " + e.getMessage());
        }
        return books;
    }

    public int getAvailableBookCount() {
        try {
            String sql = "SELECT COUNT(*) FROM books WHERE status = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, BookStatus.AVAILABLE.toString());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting available books: " + e.getMessage());
        }
        return 0;
    }

    public int getBorrowedBookCount() {
        try {
            String sql = "SELECT COUNT(*) FROM books WHERE status = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, BookStatus.BORROWED.toString());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting borrowed books: " + e.getMessage());
        }
        return 0;
    }

    public int getOverdueBookCount() {
        try {
            String sql = "SELECT COUNT(*) FROM books WHERE status = ? AND due_date < ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, BookStatus.BORROWED.toString());
            stmt.setString(2, LocalDate.now().toString());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting overdue books: " + e.getMessage());
        }
        return 0;
    }

    public List<Book> getBorrowedBooksByStudent(String studentId) {
        List<Book> books = new ArrayList<>();
        try {
            String sql = "SELECT * FROM books WHERE borrowed_by = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Book book = mapResultSetToBook(rs);
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Error finding borrowed books: " + e.getMessage());
        }
        return books;
    }


    private void setBookParameters(PreparedStatement stmt, Book book) throws SQLException {
        stmt.setString(1, book.getBookId());
        stmt.setString(2, book.getTitle());
        stmt.setString(3, book.getAuthor());
        stmt.setString(4, book.getISBN());
        stmt.setString(5, book.getCategory());
        stmt.setInt(6, book.getPublicationYear());
        stmt.setString(7, book.getStatus().toString());
        
    }
    
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book(
            rs.getString("book_id"), 
            rs.getString("title"),
            rs.getString("author"),
            rs.getString("isbn"),
            rs.getString("category"),
            rs.getInt("publication_year"), 
            BookStatus.valueOf(rs.getString("status"))
        );
        return book;
    }

  public List<Book> findByCategory(String category) {
    List<Book> books = new ArrayList<>();
    String sql = "SELECT * FROM books WHERE LOWER(category) LIKE LOWER(?)";
    
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, "%" + category + "%");
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            books.add(mapResultSetToBook(rs));
        }
    } catch (SQLException e) {
        System.err.println("Error finding books by category: " + e.getMessage());
    }
    return books;
  }
}