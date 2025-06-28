module com.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    
    opens com.library.librarymanagementsystem to javafx.fxml;
    opens com.library.controller to javafx.fxml;
    opens com.library.model to javafx.base;
    opens com.library.dao to com.google.gson;
    
    exports com.library.librarymanagementsystem;
}