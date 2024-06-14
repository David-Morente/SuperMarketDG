/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.davidmorente.controllers;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.davidmorente.bean.Cargo;
import org.davidmorente.bean.Empleado;
import org.davidmorente.db.Conexion;
import org.davidmorente.system.Main;

/**
 *
 * @author david
 */
public class MenuEmpleadosController implements Initializable{
    private Main escenarioEmpleados;
    
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Empleado> listaEmpleado;
    private ObservableList<Cargo> listaCargoEmpleado;
    
    @FXML Button btnRegresar;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReportes;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    @FXML private TextField txtCodigoE;
    @FXML private TextField txtNombreE;
    @FXML private TextField txtApellidoE;
    @FXML private TextField txtSueldoE;
    @FXML private TextField txtDireccionE;
    @FXML private TextField txtTurnoE;
    @FXML private ComboBox<Cargo> cbCargoEmpleado;
    
    @FXML private TableView tblEmpleado;
    @FXML private TableColumn colCodigoE;
    @FXML private TableColumn colNombreE;
    @FXML private TableColumn colApellidoE;
    @FXML private TableColumn colSueldoE;
    @FXML private TableColumn colDireccionE;
    @FXML private TableColumn colTurnoE;
    @FXML private TableColumn colCodigoC;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    
    }
    
    public void cargarDatos(){
        tblEmpleado.setItems(getEmpleado());
        colCodigoE.setCellValueFactory(new PropertyValueFactory<Cargo, Integer>("codigoEmpleado"));
        colNombreE.setCellValueFactory(new PropertyValueFactory<Cargo, String>("nombreEmpleado"));
        colApellidoE.setCellValueFactory(new PropertyValueFactory<Cargo, String>("apellidoEmpleado"));
        colSueldoE.setCellValueFactory(new PropertyValueFactory<Cargo, Double>("sueldo"));
        colDireccionE.setCellValueFactory(new PropertyValueFactory<Cargo, String>("direccionEmpleado"));
        colTurnoE.setCellValueFactory(new PropertyValueFactory<Cargo, String>("turno"));
        colCodigoC.setCellValueFactory(new PropertyValueFactory<Cargo, Integer>("codigoCargoEmpleado"));
    }
    
    
    
    public ObservableList<Empleado> getEmpleado(){
        ArrayList<Empleado> Lista = new ArrayList<>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarEmpleado()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                Lista.add(new Empleado (resultado.getInt("codigoEmpleado"),
                                        resultado.getString("nombreEmpleado"),
                                        resultado.getString("apellidoEmpleado"),
                                        resultado.getDouble("sueldo"),
                                        resultado.getString("direccionEmpleado"),
                                        resultado.getString("turno"),
                                        resultado.getInt("codigoCargoEmpleado")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(Lista);
    }
    
    public ObservableList<Cargo> getCargo(){
        ArrayList<Cargo> Lista = new ArrayList<>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCargoEmpleado()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                Lista.add(new Cargo (resultado.getInt("codigoCargoEmpleado"),
                                        resultado.getString("nombreCargo"),
                                        resultado.getString("descripcionCargo")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCargoEmpleado = FXCollections.observableArrayList(Lista);
    }
    
    public void desactivarControles(){
        txtCodigoE.setEditable(false);
        txtNombreE.setEditable(false);
        colApellidoE.setEditable(false);
        txtSueldoE.setEditable(false);
        txtDireccionE.setEditable(false);
        txtDireccionE.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoE.setEditable(true);
        txtNombreE.setEditable(true);
        colDireccionE.setEditable(true);
    }
    
    
}
