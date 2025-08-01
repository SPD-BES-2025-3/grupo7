module com.grupo7.petshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires ormlite.jdbc;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;

    opens com.grupo7.petshop.model to javafx.base, ormlite.jdbc;
    opens com.grupo7.petshop.controller to javafx.fxml;
    exports com.grupo7.petshop;
}
