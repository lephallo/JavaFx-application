module com.example.vehivlesproject {
        requires javafx.controls;
        requires javafx.fxml;
    requires java.sql;
    requires kernel;
    requires layout;
    requires java.desktop;
    requires io;




        opens com.example.vehivlesproject to javafx.fxml;
        exports com.example.vehivlesproject;
        }