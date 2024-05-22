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
import org.davidmorente.bean.Clientes;
import org.davidmorente.db.Conexion;
import org.davidmorente.system.Main;

/**
 *
 * @author informatica
 */
public class MenuClienteControllers implements Initializable {
    private Main escenarioPrincipalCliente;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Clientes> listaClientes;
    
    @FXML Button btnRegresar;
    @FXML private TextField txtDireccionC;
    @FXML private TextField txtCorreoC;
    @FXML private TextField txtCodigoC;
    @FXML private TextField txtNit;
    @FXML private TextField txtNombreC;
    @FXML private TextField txtApellidoC;
    @FXML private TextField txtTelefonoC;
    @FXML private TableView tblClientes;
    @FXML private TableColumn colCodigoC;
    @FXML private TableColumn colNombreC;
    @FXML private TableColumn colApellidoC;
    @FXML private TableColumn colNit;
    @FXML private TableColumn colDireccionC;
    @FXML private TableColumn colTelefonoC;
    @FXML private TableColumn colCorreoC;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReportes;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarDatos();
    }    

    public void cargarDatos(){
        tblClientes.setItems(getClientes());
        colCodigoC.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("codigoCliente"));
        colNit.setCellValueFactory(new PropertyValueFactory<Clientes, String>("NITCliente"));
        colNombreC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombreCliente"));
        colApellidoC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("apellidoCliente"));
        colDireccionC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccionCliente"));
        colTelefonoC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefonoCliente"));
        colCorreoC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("correoCliente"));
    }
    
    public void seleccionarElemento(){
        txtCodigoC.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente()));
        txtNit.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getNITCliente());
        txtNombreC.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getNombreCliente());
        txtApellidoC.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getApellidoCliente());
        txtDireccionC.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getDireccionCliente());
        txtTelefonoC.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getTelefonoCliente());
        txtCorreoC.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCorreoCliente());
    }
    
    public ObservableList<Clientes> getClientes(){
        ArrayList<Clientes> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarClientes()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                Lista.add(new Clientes (resultado.getInt("codigoCliente"),
                                        resultado.getString("NITCliente"),
                                        resultado.getString("nombreCliente"),
                                        resultado.getString("apellidoCliente"),
                                        resultado.getString("direccionCliente"),
                                        resultado.getString("telefonoCliente"),
                                        resultado.getString("correoCliente")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaClientes = FXCollections.observableArrayList(Lista);
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
        Clientes registro = new Clientes();
        registro.setCodigoCliente(Integer.parseInt(txtCodigoC.getText()));
        registro.setNITCliente(txtNit.getText());
        registro.setNombreCliente(txtNombreC.getText());
        registro.setApellidoCliente(txtApellidoC.getText());
        registro.setDireccionCliente(txtDireccionC.getText());
        registro.setTelefonoCliente(txtTelefonoC.getText());
        registro.setCorreoCliente(txtCorreoC.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarClientes(?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getCodigoCliente());
            procedimiento.setString(3, registro.getNombreCliente());
            procedimiento.setString(4, registro.getApellidoCliente());
            procedimiento.setString(2, registro.getNITCliente());
            procedimiento.setString(6, registro.getTelefonoCliente());
            procedimiento.setString(5, registro.getDireccionCliente());
            procedimiento.setString(7, registro.getCorreoCliente());
            procedimiento.execute();
            listaClientes.add(registro);
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
                if(tblClientes.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar si va a ellminar registro",
                            "Eliminar clientes", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCLientes(?)}");
                            procedimiento.setInt(1, ((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente());
                            procedimiento.execute();
                            listaClientes.remove(tblClientes.getSelectionModel().getSelectedItem());
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
                if(tblClientes.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/davidmorente/images/Editar.png"));
                    imgReporte.setImage(new Image("/org/davidmorente/images/Cancelar.png"));
                    txtCodigoC.setEditable(false);
                    tipoDeOperaciones = operaciones.ACTUALIZAR; 
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
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
            break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarClientes(?, ?, ?, ?, ?, ?, ?)}");
            Clientes registro = (Clientes)tblClientes.getSelectionModel().getSelectedItem();
            registro.setNombreCliente(txtNombreC.getText());
            registro.setApellidoCliente(txtApellidoC.getText());
            registro.setNITCliente(txtNit.getText());
            registro.setTelefonoCliente(txtTelefonoC.getText());
            registro.setDireccionCliente(txtDireccionC.getText());
            registro.setCorreoCliente(txtCorreoC.getText());
            procedimiento.setInt(1, registro.getCodigoCliente());
            procedimiento.setString(3, registro.getNombreCliente());
            procedimiento.setString(4, registro.getApellidoCliente());
            procedimiento.setString(2, registro.getNITCliente());
            procedimiento.setString(6, registro.getTelefonoCliente());
            procedimiento.setString(5, registro.getDireccionCliente());
            procedimiento.setString(7, registro.getCorreoCliente());
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
        txtCodigoC.setEditable(false);
        txtTelefonoC.setEditable(false);
        txtDireccionC.setEditable(false);
        txtNit.setEditable(false);
        txtNombreC.setEditable(false);
        txtApellidoC.setEditable(false);
        txtCorreoC.setEditable(false); 
    }
    
    public void activarControles(){
        txtCodigoC.setEditable(true);
        txtTelefonoC.setEditable(true);
        txtDireccionC.setEditable(true);
        txtNit.setEditable(true);
        txtNombreC.setEditable(true);
        txtApellidoC.setEditable(true);
        txtCorreoC.setEditable(true); 
    }
    
    public void limpiarControles(){
        txtCodigoC.clear();
        txtTelefonoC.clear();
        txtDireccionC.clear();
        txtNit.clear();
        txtNombreC.clear();
        txtApellidoC.clear();
        txtCorreoC.clear();
    }
    
    public Main getEscenarioPrincipal() {
        return escenarioPrincipalCliente;
    }
    
    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipalCliente = escenarioPrincipal;
    }
    
    @FXML
    public void clickRegresar(ActionEvent event){
         if (event.getSource() == btnRegresar){
        escenarioPrincipalCliente.menuPrincipalView();
        }
    }

}
