package com.example.registroempleados;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistroEmpleadosApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        System.out.println(
                RegistroEmpleadosApplication.class.getResource("views/empleado-view.fxml")
        );

        FXMLLoader fxmlLoader = new FXMLLoader(
                RegistroEmpleadosApplication.class.getResource("views/empleado-view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Registro de Empleados");
        stage.setScene(scene);
        stage.show();
    }
}