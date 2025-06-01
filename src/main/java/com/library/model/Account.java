package com.library.model;

import java.time.LocalDateTime;

public class Account {
    private String accountId;
    private String username;
    private String password;
    private LocalDateTime lastLogin;
    private AccountStatus status;
    private User user;
    
    public Account() {
        this.status = AccountStatus.ACTIVE;
    }
    
    public Account(String accountId, String username, String password) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.status = AccountStatus.ACTIVE;
    }
    
    // Getters and setters
    public String getAccountId() {
        return accountId;
    }
    
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public AccountStatus getStatus() {
        return status;
    }
    
    public void setStatus(AccountStatus status) {
        this.status = status;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            user.setAccount(this);
        }
    }
    
    // Account-specific methods
    public boolean login(String enteredPassword) {
        boolean isValid = status == AccountStatus.ACTIVE && password.equals(enteredPassword);
        if (isValid) {
            lastLogin = LocalDateTime.now();
        }
        return isValid;
    }
    
    public void logout() {
        // Perform logout operations
    }
    
    public void suspend() {
        this.status = AccountStatus.SUSPENDED;
    }
    
    public void activate() {
        this.status = AccountStatus.ACTIVE;
    }
    
    public void close() {
        this.status = AccountStatus.CLOSED;
    }
    
    public boolean isActive() {
        return status == AccountStatus.ACTIVE;
    }
    
    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", username='" + username + '\'' +
                ", status=" + status +
                '}';
    }
}