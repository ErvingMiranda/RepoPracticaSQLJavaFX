module com.example.registroempleados.registroempleadosfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.registroempleados.registroempleadosfx to javafx.fxml;
    exports com.example.registroempleados.registroempleadosfx;
    exports com.example.registroempleados;
    opens com.example.registroempleados to javafx.fxml;
}