module com.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens com.library.librarymanagementsystem to javafx.fxml;
    opens com.library.controller to javafx.fxml;
    opens com.library.model to javafx.base;
    
    exports com.library.librarymanagementsystem;
}