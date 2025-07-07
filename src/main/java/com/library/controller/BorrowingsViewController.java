package com.library.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.library.model.Borrowing;

public class BorrowingsViewController implements Initializable {

    @FXML
    private TableView<Borrowing> borrowedBooksTable;
    
    @FXML
    private TableColumn<Borrowing, String> borrowIdCol;
        
    @FXML
    private TableColumn<Borrowing, String> bookIdCol;  // Changed type to String
        
    @FXML
    private TableColumn<Borrowing, String> userIdCol;   // Changed type to String
    
    @FXML
    private TableColumn<Borrowing, LocalDate> borrowDateCol;
    
    @FXML
    private TableColumn<Borrowing, LocalDate> returnDateCol;
    
    @FXML
    private TableColumn<Borrowing, String> statusCol;

    private final ObservableList<Borrowing> borrowingList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize table columns
        borrowIdCol.setCellValueFactory(new PropertyValueFactory<>("borrowingId"));
        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        borrowDateCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Load sample data (replace with your actual data loading logic)
        // Set data to table
        borrowedBooksTable.setItems(borrowingList);
    }
    
    
    // Refresh method to reload data
    public void refreshData() {
        borrowingList.clear();
    }
}