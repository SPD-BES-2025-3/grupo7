module com.grupo7.petshop {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.grupo7.petshop.controller to javafx.fxml;
    opens com.grupo7.petshop.model to javafx.base;
    exports com.grupo7.petshop;
}
