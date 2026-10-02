module com.example.registroempleados.registroempleadosfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;

    exports com.example.registroempleados;

    opens com.example.registroempleados to javafx.fxml;
    opens com.example.registroempleados.controller to javafx.fxml;
    opens com.example.registroempleados.model to javafx.base;
}