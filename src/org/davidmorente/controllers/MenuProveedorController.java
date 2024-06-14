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
import java.util.HashMap;
import java.util.Map;
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

import org.davidmorente.bean.Proveedores;
import org.davidmorente.db.Conexion;
import org.davidmorente.report.GenerarReportes;
import org.davidmorente.system.Main;

/**
 * FXML Controller class
 *
 * @author david
 */
public class MenuProveedorController implements Initializable {
    private Main escenarioProveedor;
    private ObservableList<Proveedores> listaProveedores;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    
    
    @FXML private TextField txtDireccionP;
    @FXML private TextField txtPaginaWeb;
    @FXML private TextField txtCodigoP;
    @FXML private TextField txtNitP;
    @FXML private TextField txtNombreP;
    @FXML private TextField txtApellidoP;
    @FXML private TextField txtContactoP;
    @FXML private TextField txtRazonSocial;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReportes;
    @FXML private Button btnRegresar;
    @FXML private TableView tblProveedores;
    @FXML private TableColumn colCodigoP;
    @FXML private TableColumn colNombreP;
    @FXML private TableColumn colApellidoP;
    @FXML private TableColumn colNit;
    @FXML private TableColumn colDireccionP;
    @FXML private TableColumn colContactoP;
    @FXML private TableColumn colPaginaWeb;
    @FXML private TableColumn colRazonSocial;
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
        tblProveedores.setItems(getProveedores());
        colCodigoP.setCellValueFactory(new PropertyValueFactory<Proveedores, Integer>("codigoProveedor"));
        colNit.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("NITProveedor"));
        colNombreP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("nombreProveedor"));
        colApellidoP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("apellidoProveedor"));
        colDireccionP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("direccionProveedor"));
        colContactoP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("contactoPrincipal"));
        colPaginaWeb.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("paginaWebProveedor"));
        colRazonSocial.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("razonSocial")); 
    }
    
    public void SeleccionarElemento(){
        txtCodigoP.setText(String.valueOf(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
        txtNitP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getNITProveedor());
        txtNombreP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getNombreProveedor());
        txtApellidoP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getApellidoProveedor());
        txtDireccionP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getDireccionProveedor());
        txtRazonSocial.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getRazonSocial());
        txtContactoP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getContactoPrincipal());        
        txtPaginaWeb.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getPaginaWebProveedor());              
    
    }
    
    public ObservableList<Proveedores> getProveedores() {
    ArrayList <Proveedores> lista = new ArrayList(); 
    
    try {
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarProveedores()}");
        ResultSet resultado = procedimiento.executeQuery();
        while (resultado.next()) {
            lista.add(new Proveedores(resultado.getInt("codigoProveedor"),
                                       resultado.getString("NITProveedor"),
                                       resultado.getString("nombreProveedor"),
                                       resultado.getString("apellidoProveedor"),
                                       resultado.getString("direccionProveedor"),
                                       resultado.getString("razonSocial"),
                                       resultado.getString("contactoPrincipal"),
                                       resultado.getString("paginaWebProveedor")
                                       
            ));
        }
            
        }catch (Exception e){
            e.printStackTrace();
        }
    
    
    
    return listaProveedores = FXCollections.observableArrayList(lista);
    }
    
    @FXML
    public void agregar() {
    switch (tipoDeOperaciones) {
        case NINGUNO:
            activarControles();
            btnAgregar.setText("Guardar");
            btnEliminar.setText("Eliminar");
            btnEditar.setDisable(true);
            btnReportes.setDisable(true);
            tipoDeOperaciones = operaciones.ACTUALIZAR;
            break;
        default:
            case ACTUALIZAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
        }
    }
    
    
    public void guardar(){
    Proveedores registro = new Proveedores();
    registro.setCodigoProveedor(Integer.parseInt(txtCodigoP.getText()));
    registro.setNITProveedor((txtNitP.getText()));
    registro.setNombreProveedor((txtNombreP.getText()));
    registro.setApellidoProveedor((txtApellidoP.getText()));
    registro.setDireccionProveedor((txtDireccionP.getText()));
    registro.setRazonSocial((txtRazonSocial.getText()));
    registro.setContactoPrincipal((txtContactoP.getText()));
    registro.setPaginaWebProveedor((txtPaginaWeb.getText()));
    
    try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarProveedores(?, ?, ?, ?, ?, ?, ?, ?)}");
        procedimiento.setInt(1, registro.getCodigoProveedor());
        procedimiento.setString(2, registro.getNITProveedor());
        procedimiento.setString(3, registro.getNombreProveedor());
        procedimiento.setString(4, registro.getApellidoProveedor());
        procedimiento.setString(5, registro.getDireccionProveedor());
        procedimiento.setString(6, registro.getRazonSocial());
        procedimiento.setString(7, registro.getContactoPrincipal());
        procedimiento.setString(8, registro.getPaginaWebProveedor());
        procedimiento.execute();
        listaProveedores.add(registro);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void Eliminar(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                imgAgregar.setImage(new Image("/org/davidmorente/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/davidmorente/image/Eliminar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if(tblProveedores.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar si eliminar el registro", "Eliminar Proveedor", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarProveedores(?)} ");
                            procedimiento.setInt(1, ((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            listaProveedores.remove(tblProveedores.getSelectionModel().getSelectedItem());
                        
                        
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento");
        
        }
    }
    
    @FXML
    public void editar(){
        switch (tipoDeOperaciones){
            case NINGUNO:
                if(tblProveedores.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/davidmorente/images/Editar.png"));
                    imgReporte.setImage(new Image("/org/davidmorente/images/Cancelar.png"));
                    txtCodigoP.setEditable(false);
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarProveedores(?, ?, ?, ?, ?, ?, ?, ?)}");
            Proveedores registro = (Proveedores)tblProveedores.getSelectionModel().getSelectedItem();
            registro.setNITProveedor(txtNitP.getText());
            registro.setNombreProveedor(txtNombreP.getText());
            registro.setApellidoProveedor(txtApellidoP.getText());
            registro.setDireccionProveedor(txtDireccionP.getText());
            registro.setRazonSocial(txtRazonSocial.getText());
            registro.setContactoPrincipal(txtContactoP.getText());
            registro.setPaginaWebProveedor(txtPaginaWeb.getText());
           
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getNITProveedor());
            procedimiento.setString(3, registro.getNombreProveedor());
            procedimiento.setString(4, registro.getApellidoProveedor());
            procedimiento.setString(5, registro.getDireccionProveedor());
            procedimiento.setString(6, registro.getRazonSocial());
            procedimiento.setString(7, registro.getContactoPrincipal());
            procedimiento.setString(8, registro.getPaginaWebProveedor());
            procedimiento.execute();
        }catch(Exception e){
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
                if(tblProveedores.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar si va a ellminar registro",
                            "Eliminar proveedor", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarProveedor(?)}");
                            procedimiento.setInt(1, ((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            procedimiento.execute();
                            listaProveedores.remove(tblProveedores.getSelectionModel().getSelectedItem());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    
                }else
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
    public void report(){
        switch (tipoDeOperaciones) {
            case NINGUNO:
                imprimirReporte();
            break;    
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
    
    public void imprimirReporte(){
         Map parametros = new HashMap();
         parametros.put("CodigoProveedor", null);
         GenerarReportes.mostrarReportes("ReporteProovedor.jasper", "Reporte de Proveedor", parametros);
        
    }
    
    public void desactivarControles(){
        txtCodigoP.setEditable(false);
        txtNombreP.setEditable(false);
        txtApellidoP.setEditable(false);
        txtDireccionP.setEditable(false);
        txtPaginaWeb.setEditable(false);
        txtNitP.setEditable(false);
        txtContactoP.setEditable(false);
        txtRazonSocial.setEditable(false);

        
    }
    
     public void activarControles(){
        txtCodigoP.setEditable(true);
        txtNombreP.setEditable(true);
        txtApellidoP.setEditable(true);
        txtDireccionP.setEditable(true);
        txtPaginaWeb.setEditable(true);
        txtNitP.setEditable(true);
        txtContactoP.setEditable(true);
        txtRazonSocial.setEditable(true);
        
     }
     
     public void limpiarControles(){
        txtCodigoP.clear();
        txtNombreP.clear();
        txtApellidoP.clear();
        txtDireccionP.clear();
        txtPaginaWeb.clear();
        txtNitP.clear();
        txtContactoP.clear();
        txtRazonSocial.clear();
     }
     
    public Main getEscenarioProveedor() {
        return escenarioProveedor;
    }

    public void setEscenarioProveedor(Main escenarioProveedor) {
        this.escenarioProveedor = escenarioProveedor;
    } 
    
    @FXML
    public void clickRegresar(ActionEvent event){
        if (event.getSource() == btnRegresar){
            escenarioProveedor.menuPrincipalView();
        }
    }
    
}
