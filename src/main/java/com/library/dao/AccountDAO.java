package com.library.dao;

import com.library.model.Account;
import com.library.model.AccountStatus;
import com.library.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDAO extends BaseDAO implements DAO<Account> {
    private final UserDAO userDAO;

    public AccountDAO() {
        super();
        this.userDAO = new UserDAO();
    }

    @Override
    public Optional<Account> get(String id) {
        try {
            String sql = "SELECT * FROM accounts WHERE account_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Account account = mapResultSetToAccount(rs);
                return Optional.of(account);
            }
        } catch (SQLException e) {
            System.err.println("Error getting account: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Account> getAll() {
        List<Account> accounts = new ArrayList<>();
        try {
            String sql = "SELECT * FROM accounts";
            ResultSet rs = connection.createStatement().executeQuery(sql);
            
            while (rs.next()) {
                Account account = mapResultSetToAccount(rs);
                accounts.add(account);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all accounts: " + e.getMessage());
        }
        return accounts;
    }

    @Override
    public void save(Account account) {
        try {
            String sql = "INSERT INTO accounts (account_id, username, password, status, creation_date, last_login, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            setAccountParameters(stmt, account);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving account: " + e.getMessage());
        }
    }

    @Override
    public void update(Account account) {
        try {
            String sql = "UPDATE accounts SET username = ?, password = ?, status = ?, creation_date = ?, last_login = ?, user_id = ? WHERE account_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            stmt.setString(3, account.getStatus().toString());
            
            if (account.getCreationDate() != null) {
                stmt.setString(4, account.getCreationDate().toString());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }
            
            if (account.getLastLogin() != null) {
                stmt.setString(5, account.getLastLogin().toString());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }
            
            stmt.setString(6, account.getUser().getUserId());
            stmt.setString(7, account.getAccountId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating account: " + e.getMessage());
        }
    }

    @Override
    public void delete(Account account) {
        try {
            String sql = "DELETE FROM accounts WHERE account_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, account.getAccountId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting account: " + e.getMessage());
        }
    }

    public Optional<Account> findByUsername(String username) {
        try {
            String sql = "SELECT * FROM accounts WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Account account = mapResultSetToAccount(rs);
                return Optional.of(account);
            }
        } catch (SQLException e) {
            System.err.println("Error finding account by username: " + e.getMessage());
        }
        return Optional.empty();
    }

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setAccountId(rs.getString("account_id"));
        account.setUsername(rs.getString("username"));
        account.setPassword(rs.getString("password"));
        account.setStatus(AccountStatus.valueOf(rs.getString("status")));
        
        String creationDateStr = rs.getString("creation_date");
        if (creationDateStr != null) {
            account.setCreationDate(LocalDateTime.parse(creationDateStr));
        }
        
        String lastLoginStr = rs.getString("last_login");
        if (lastLoginStr != null) {
            account.setLastLogin(LocalDateTime.parse(lastLoginStr));
        }
        
        String userId = rs.getString("user_id");
        Optional<User> user = userDAO.getUserById(userId);
        user.ifPresent(account::setUser);
        
        return account;
    }

    private void setAccountParameters(PreparedStatement stmt, Account account) throws SQLException {
        stmt.setString(1, account.getAccountId());
        stmt.setString(2, account.getUsername());
        stmt.setString(3, account.getPassword());
        stmt.setString(4, account.getStatus().toString());
        
        if (account.getCreationDate() != null) {
            stmt.setString(5, account.getCreationDate().toString());
        } else {
            stmt.setNull(5, Types.VARCHAR);
        }
        
        if (account.getLastLogin() != null) {
            stmt.setString(6, account.getLastLogin().toString());
        } else {
            stmt.setNull(6, Types.VARCHAR);
        }
        
        stmt.setString(7, account.getUser().getUserId());
    }
}