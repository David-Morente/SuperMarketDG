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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.davidmorente.bean.Cargo;
import org.davidmorente.db.Conexion;
import org.davidmorente.system.Main;

/**
 *
 * @author david
 */
public class MenuCargoController implements Initializable{
    private Main escenarioCargoEmpleado;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Cargo> listaCargoEmpleado;
    
    @FXML Button btnRegresar;
    @FXML private TextField txtCodigoE;
    @FXML private TextField txtNombreE;
    @FXML private TextField txtDescripcionE;
    @FXML private TableColumn colCodigoE;
    @FXML private TableColumn colNombreE;
    @FXML private TableColumn colDescripcionE;
    @FXML private TableView tblCargoEmpleado;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReportes;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblCargoEmpleado.setItems(getCargo());
        colCodigoE.setCellValueFactory(new PropertyValueFactory<Cargo, Integer>("codigoCargoEmpleado"));
        colNombreE.setCellValueFactory(new PropertyValueFactory<Cargo, String>("nombreCargo"));
        colDescripcionE.setCellValueFactory(new PropertyValueFactory<Cargo, String>("descripcionCargo"));
    }
    
    public void seleccionarElemento(){
        txtCodigoE.setText(String.valueOf(((Cargo)tblCargoEmpleado.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado()));
        txtNombreE.setText(((Cargo)tblCargoEmpleado.getSelectionModel().getSelectedItem()).getNombreCargo());
        txtDescripcionE.setText(((Cargo)tblCargoEmpleado.getSelectionModel().getSelectedItem()).getDescripcionCargo());
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
    
    public void agregar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(true);
                btnReportes.setDisable(true);
                imgAgregar.setImage(new Image("/org/davidmorente/images/Guardar.png"));
                imgEliminar.setImage(new Image("/org/davidmorente/images/Cancelar.png"));
                tipoDeOperaciones = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                imgAgregar.setImage(new Image("/org/davidmorente/images/Agregar.png"));
                imgEliminar.setImage(new Image("/org/davidmorente/images/Eliminar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    
    public void guardar(){
        Cargo registro = new Cargo();
        registro.setCodigoCargoEmpleado(Integer.parseInt(txtCodigoE.getText()));
        registro.setNombreCargo(txtNombreE.getText());
        registro.setDescripcionCargo(txtDescripcionE.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCargoEmpleado(?, ?, ?)}");
            procedimiento.setInt(1, registro.getCodigoCargoEmpleado());
            procedimiento.setString(2, registro.getNombreCargo());
            procedimiento.setString(3, registro.getDescripcionCargo());
            procedimiento.execute();
            listaCargoEmpleado.add(registro);
        }catch(Exception e)
        {
            
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                imgAgregar.setImage(new Image("/org/davidmorente/images/Agregar.png"));
                imgEliminar.setImage(new Image("/org/davidmorente/images/Eliminar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
            break;
            default:
                if(tblCargoEmpleado.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar si va a ellminar registro",
                            "Eliminar clientes", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCargoEmpleado(?)}");
                            procedimiento.setInt(1, ((Cargo)tblCargoEmpleado.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
                            procedimiento.execute();
                            listaCargoEmpleado.remove(tblCargoEmpleado.getSelectionModel().getSelectedItem());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    
                }else
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
    public void editar(){
        switch (tipoDeOperaciones){
            case NINGUNO:
                if(tblCargoEmpleado.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/davidmorente/images/Editar.png"));
                    imgReporte.setImage(new Image("/org/davidmorente/images/Cancelar.png"));
                    txtCodigoE.setEditable(false);
                    tipoDeOperaciones = MenuCargoController.operaciones.ACTUALIZAR; 
                }else
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar algun elemento");
            break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReportes.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/davidmorente/images/Agregar.png"));
                imgReporte.setImage(new Image("/org/davidmorente/images/Reportes.png"));
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = MenuCargoController.operaciones.NINGUNO;
                cargarDatos();
            break;
        }
    }
    
     public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarCargoEmpleado(?, ?, ?)}");
            Cargo registro = (Cargo)tblCargoEmpleado.getSelectionModel().getSelectedItem();
            registro.setNombreCargo(txtNombreE.getText());
            registro.setDescripcionCargo(txtDescripcionE.getText());
            procedimiento.setInt(1, registro.getCodigoCargoEmpleado());
            procedimiento.setString(2, registro.getDescripcionCargo());
            procedimiento.setString(3, registro.getNombreCargo());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void report(){
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                btnEditar.setText("Editar");
                btnReportes.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/davidmorente/images/Agregar.png"));
                imgReporte.setImage(new Image("/org/davidmorente/images/Reportes.png"));
                desactivarControles();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void desactivarControles(){
        txtCodigoE.setEditable(false);
        txtNombreE.setEditable(false);
        txtDescripcionE.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoE.setEditable(true);
        txtNombreE.setEditable(true);
        txtDescripcionE.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoE.clear();
        txtNombreE.clear();
        txtDescripcionE.clear();
    }
    
    public Main getEscenarioPrincipal() {
        return escenarioCargoEmpleado;
    }
    
    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioCargoEmpleado = escenarioPrincipal;
    }
    
    @FXML
    public void clickRegresar(ActionEvent event){
         if (event.getSource() == btnRegresar){
        escenarioCargoEmpleado.menuPrincipalView();
        }
    }
}
