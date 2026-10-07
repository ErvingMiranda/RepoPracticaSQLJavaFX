package com.example.registroempleados.controller;

import com.example.registroempleados.database.DatabaseConnection;
import com.example.registroempleados.model.Empleado;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class EmpleadoController {
    @FXML
    private TextField txtNombres;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtCedula;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCargo;
    @FXML
    private ComboBox<String> cmbDepartamento;
    @FXML
    private TextField txtSalario;
    @FXML
    private DatePicker dpFechaContratacion;
    @FXML
    private ComboBox<String> cmbEstado;
    @FXML
    private ComboBox<String> cmbConsulta;

    @FXML
    private TableView<Empleado> tblEmpleados;
    @FXML
    private TableColumn<Empleado, Integer> colId;
    @FXML
    private TableColumn<Empleado, String> colNombres;
    @FXML
    private TableColumn<Empleado, String> colApellidos;
    @FXML
    private TableColumn<Empleado, String> colCedula;
    @FXML
    private TableColumn<Empleado, String> colCorreo;
    @FXML
    private TableColumn<Empleado, String> colTelefono;
    @FXML
    private TableColumn<Empleado, String> colCargo;
    @FXML
    private TableColumn<Empleado, String> colDepartamento;
    @FXML
    private TableColumn<Empleado, Double> colSalario;
    @FXML
    private TableColumn<Empleado, Date> colFechaContratacion;
    @FXML
    private TableColumn<Empleado, String> colEstado;

    private final ObservableList<Empleado> listaEmpleados = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarTabla();
        configurarCombobox();
        cargarEmpleados();

        // Cada vez que se selecciona una fila del TableView, obtenemos ese objeto
        tblEmpleados.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        cargarEmpleadoSeleccionado(newValue);
                    }
                });
    }

    //Encargado de mapear los datos en los campos
    private void cargarEmpleadoSeleccionado(Empleado empleado) {
        txtNombres.setText(empleado.getNombres());
        txtApellidos.setText(empleado.getApellidos());
        txtCedula.setText(empleado.getCedula());
        txtCorreo.setText(empleado.getCorreo());
        txtTelefono.setText(empleado.getTelefono());
        txtCargo.setText(empleado.getCargo());
        cmbDepartamento.setValue(empleado.getDepartamento());
        txtSalario.setText(String.valueOf(empleado.getSalario()));
        dpFechaContratacion.setValue(
                empleado.getFechaContratacion().toLocalDate()
        );
        cmbEstado.setValue(empleado.getEstado());
    }

    //Definir el tipo de dato que se escribe en cada celda del tableview
    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        colDepartamento.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        colSalario.setCellValueFactory(new PropertyValueFactory<>("salario"));
        colFechaContratacion.setCellValueFactory(new PropertyValueFactory<>("fechaContratacion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    //Método encargado de cargar datos por defecto de los combobox
    private void configurarCombobox() {
        cmbDepartamento.getItems().clear();
        cmbDepartamento.getItems().addAll("Tecnología", "Contabilidad", "Recursos Humanos", "Ventas");

        cmbEstado.getItems().clear();
        cmbEstado.getItems().addAll("Activo", "Inactivo");

        cmbConsulta.getItems().clear();
        cmbConsulta.getItems().addAll(
                "Todos los empleados",
                "Empleados activos",
                "Departamento: Tecnología",
                "Salario mayor a 25000",
                "Salario de mayor a menor",
                "Apellido de A-Z"
        );
        cmbConsulta.setValue("Todos los empleados");
    }

    //Carga en el TableView los empleados según la consulta seleccionada
    @FXML
    private void cargarEmpleados() {
        listaEmpleados.clear();

        String sqlTodos = "SELECT * FROM empleado";
        String sqlActivos = "SELECT * FROM empleado WHERE estado = 'Activo'";
        String sqlDepartamento = "SELECT * FROM empleado WHERE departamento = 'Tecnología'";
        String sqlSalarioMayor = "SELECT * FROM empleado WHERE salario > 25000 ORDER BY salario DESC";
        String sqlOrdenSalario = "SELECT * FROM empleado ORDER BY salario DESC";
        String sqlOrdenApellido = "SELECT * FROM empleado ORDER BY apellidos ASC";

        String sql = sqlTodos;

        switch (cmbConsulta.getValue()) {
            case "Empleados activos":
                sql = sqlActivos;
                break;
            case "Departamento: Tecnología":
                sql = sqlDepartamento;
                break;
            case "Salario mayor a 25000":
                sql = sqlSalarioMayor;
                break;
            case "Salario de mayor a menor":
                sql = sqlOrdenSalario;
                break;
            case "Apellido de A-Z":
                sql = sqlOrdenApellido;
                break;
        }

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Empleado empleado = new Empleado();
                empleado.setId(resultSet.getInt("id"));
                empleado.setNombres(resultSet.getString("nombres"));
                empleado.setApellidos(resultSet.getString("apellidos"));
                empleado.setCedula(resultSet.getString("cedula"));
                empleado.setCorreo(resultSet.getString("correo"));
                empleado.setTelefono(resultSet.getString("telefono"));
                empleado.setCargo(resultSet.getString("cargo"));
                empleado.setDepartamento(resultSet.getString("departamento"));
                empleado.setSalario(resultSet.getDouble("salario"));
                empleado.setFechaContratacion(resultSet.getDate("fecha_contratacion"));
                empleado.setEstado(resultSet.getString("estado"));
                listaEmpleados.add(empleado);
            }

            tblEmpleados.setItems(listaEmpleados);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Restablece la consulta y muestra todos los empleados
    @FXML
    private void actualizarTabla() {
        if ("Todos los empleados".equals(cmbConsulta.getValue())) {
            cargarEmpleados();
        } else {
            cmbConsulta.setValue("Todos los empleados");
        }
    }

    @FXML
    private void guardarEmpleado() {
        if(!validarCampos()){
            return;
        }

        //Consulta SQL a ejecutar
        String sql = "INSERT INTO empleado (nombres, apellidos, cedula, correo, telefono, cargo, departamento, salario, fecha_contratacion, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setString(1, txtNombres.getText());
            statement.setString(2, txtApellidos.getText());
            statement.setString(3, txtCedula.getText());

            if(txtCorreo.getText().isBlank()){
                statement.setNull(4, Types.VARCHAR);
            } else {
                statement.setString(4, txtCorreo.getText());
            }

            statement.setString(5, txtTelefono.getText());
            statement.setString(6, txtCargo.getText());
            statement.setString(7, cmbDepartamento.getValue());
            statement.setDouble(8, Double.parseDouble(txtSalario.getText()));
            statement.setDate(9, Date.valueOf(dpFechaContratacion.getValue()));
            statement.setString(10, cmbEstado.getValue());
            statement.executeUpdate();

            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Registro almacenado",
                    "Empleado registrado",
                    "El empleado se ha almacenado exitosamente"
            );

            limpiarCampos();
            cargarEmpleados();
        }

        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private boolean validarCampos() {
        if (txtNombres.getText().isBlank() || txtApellidos.getText().isBlank() || txtCedula.getText().isBlank() ||
                txtCargo.getText().isBlank() || cmbDepartamento.getValue() == null || txtSalario.getText().isBlank() ||
                dpFechaContratacion.getValue() == null || cmbEstado.getValue() == null) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Campos incompletos",
                    "Faltan datos",
                    "Debe completar todos los campos obligatorios antes de guardar."
            );
            return false;
        }

        try {
            double salario = Double.parseDouble(txtSalario.getText());

            if(salario < 0){
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Salario inválido",
                        "Valor incorrecto",
                        "El salario no puede ser negativo."
                );
                return false;
            }
        } catch (NumberFormatException ex) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Salario inválido",
                    "Valor incorrecto",
                    "El salario debe ser un valor numérico."
            );
            return false;
        }

        return true;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String encabezado, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void limpiarCampos() {
        txtNombres.clear();
        txtApellidos.clear();
        txtCedula.clear();
        txtCorreo.clear();
        txtTelefono.clear();
        txtCargo.clear();
        txtSalario.clear();
        cmbDepartamento.getSelectionModel().clearSelection();
        dpFechaContratacion.setValue(null);
        cmbEstado.getSelectionModel().clearSelection();
    }

    @FXML
    private void actualizarEmpleado() {
        Empleado empleadoSeleccionado = tblEmpleados.getSelectionModel().getSelectedItem();

        if(empleadoSeleccionado == null) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Selección requerida",
                    "No hay un empleado seleccionado",
                    "Seleccione un empleado de la tabla"
            );
            return;
        }

        if(!validarCampos()) {
            return;
        }

        String sql = "UPDATE empleado SET nombres = ?, apellidos = ?, cedula = ?, correo = ?, telefono = ?, cargo = ?, departamento = ?, salario = ?, fecha_contratacion = ?, estado = ? WHERE id = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setString(1, txtNombres.getText());
            statement.setString(2, txtApellidos.getText());
            statement.setString(3, txtCedula.getText());

            if(txtCorreo.getText().isBlank()){
                statement.setNull(4, Types.VARCHAR);
            } else {
                statement.setString(4, txtCorreo.getText());
            }

            statement.setString(5, txtTelefono.getText());
            statement.setString(6, txtCargo.getText());
            statement.setString(7, cmbDepartamento.getValue());
            statement.setDouble(8, Double.parseDouble(txtSalario.getText()));
            statement.setDate(9, Date.valueOf(dpFechaContratacion.getValue()));
            statement.setString(10, cmbEstado.getValue());
            statement.setInt(11, empleadoSeleccionado.getId());

            int filasActualizadas = statement.executeUpdate();

            if(filasActualizadas > 0) {
                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Registro actualizado",
                        "Actualización completada",
                        "El empleado fue actualizado exitosamente"
                );
                limpiarCampos();
                cargarEmpleados();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void eliminarEmpleado() {
        Empleado empleadoSeleccionado = tblEmpleados.getSelectionModel().getSelectedItem();

        if(empleadoSeleccionado == null) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Selección requerida",
                    "No hay un empleado seleccionado",
                    "Seleccione un empleado de la tabla"
            );
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de que desea eliminar el registro de empleado?");

        if(confirmacion.showAndWait().isEmpty() || confirmacion.getResult() != ButtonType.OK) {
            return;
        }

        String sql = "DELETE FROM empleado WHERE id = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setInt(1, empleadoSeleccionado.getId());

            int filasEliminadas = statement.executeUpdate();

            if(filasEliminadas > 0) {
                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Registro eliminado",
                        "Eliminación completada",
                        "El empleado fue eliminado exitosamente"
                );
                limpiarCampos();
                cargarEmpleados();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}