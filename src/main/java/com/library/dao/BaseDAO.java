package com.library.dao;

import com.library.database.DatabaseConnection;
import java.sql.Connection;

public abstract class BaseDAO {
    protected Connection connection;
    
    public BaseDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public void setConnection(Connection connection) {
      this.connection = connection;
    }
}