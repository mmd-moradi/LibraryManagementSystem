package com.library.dao;

import com.library.model.Borrowing;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BorrowingDAO extends BaseDAO implements DAO<Borrowing> {

    @Override
    public Optional<Borrowing> get(String id) {
        String sql = "SELECT * FROM borrowings WHERE borrowingId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToBorrowing(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Borrowing> getAll() {
        List<Borrowing> borrowings = new ArrayList<>();
        String sql = "SELECT * FROM borrowings";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                borrowings.add(mapResultSetToBorrowing(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowings;
    }

    @Override
    public void save(Borrowing borrowing) {
        String sql = "INSERT INTO borrowings (borrowingId, bookId, userId, borrowDate, dueDate, returnDate, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, borrowing.getBorrowingId());
            stmt.setString(2, borrowing.getBookId());
            stmt.setString(3, borrowing.getUserId());
            stmt.setDate(4, Date.valueOf(borrowing.getBorrowDate()));
            stmt.setDate(5, Date.valueOf(borrowing.getDueDate()));
            stmt.setDate(6, borrowing.getReturnDate() != null ? Date.valueOf(borrowing.getReturnDate()) : null);
            stmt.setString(7, borrowing.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Borrowing borrowing) {
        String sql = "UPDATE borrowings SET bookId = ?, userId = ?, borrowDate = ?, dueDate = ?, returnDate = ?, status = ? WHERE borrowingId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, borrowing.getBookId());
            stmt.setString(2, borrowing.getUserId());
            stmt.setDate(3, Date.valueOf(borrowing.getBorrowDate()));
            stmt.setDate(4, Date.valueOf(borrowing.getDueDate()));
            stmt.setDate(5, borrowing.getReturnDate() != null ? Date.valueOf(borrowing.getReturnDate()) : null);
            stmt.setString(6, borrowing.getStatus());
            stmt.setString(7, borrowing.getBorrowingId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Borrowing borrowing) {
        String sql = "DELETE FROM borrowings WHERE borrowingId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, borrowing.getBorrowingId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteByBookId(String bookId) {
      try {
          String sql = "DELETE FROM borrowings WHERE bookId = ?"; 
          PreparedStatement stmt = connection.prepareStatement(sql);
          stmt.setString(1, bookId);
          stmt.executeUpdate();
      } catch (SQLException e) {
          System.err.println("Error deleting borrowings: " + e.getMessage());
          throw new RuntimeException("Error deleting borrowings", e);
      }
    }

    
    public List<Borrowing> getActiveBorrowingsByUser(String userId) {
        List<Borrowing> borrowings = new ArrayList<>();
        String sql = "SELECT * FROM borrowings WHERE userId = ? AND status = 'ACTIVE'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                borrowings.add(mapResultSetToBorrowing(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowings;
    }

    public Borrowing getActiveBorrowingForBook(String bookId) {
        String sql = "SELECT * FROM borrowings WHERE bookId = ? AND status = 'ACTIVE'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToBorrowing(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Borrowing> getActiveBorrowings() {
        List<Borrowing> borrowings = new ArrayList<>();
        String sql = "SELECT * FROM borrowings WHERE status = 'ACTIVE'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                borrowings.add(mapResultSetToBorrowing(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowings;
    }

    private Borrowing mapResultSetToBorrowing(ResultSet rs) throws SQLException {
        return new Borrowing(
            rs.getString("borrowingId"),
            rs.getString("bookId"),
            rs.getString("userId"),
            rs.getDate("borrowDate").toLocalDate(),
            rs.getDate("dueDate").toLocalDate(),
            rs.getDate("returnDate") != null ? rs.getDate("returnDate").toLocalDate() : null,
            rs.getString("status")
        );
    }
}