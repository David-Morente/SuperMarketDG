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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.davidmorente.bean.DetalleFactura;
import org.davidmorente.bean.Factura;
import org.davidmorente.bean.Producto;
import org.davidmorente.db.Conexion;
import org.davidmorente.system.Main;

/**
 *
 * @author david
 */
public class MenuDetalleFacturaController implements Initializable{
    private Main escenarioDetalleFactura;
    
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<DetalleFactura> listaDetalleFactura;
    private ObservableList<Factura> listaFactura;
    private ObservableList<Producto> listaProductos;
    
    @FXML Button btnRegresar;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReportes;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    @FXML private TextField txtCodigoF;
    @FXML private TextField txtPrecioF;
    @FXML private TextField txtCantidadF;
    @FXML private ComboBox<Factura> cbFacturas;
    @FXML private ComboBox<Producto> cbProductos;
    
    @FXML private TableView tblDetalle;
    @FXML private TableColumn colDetalleF;
    @FXML private TableColumn colPrecioF;
    @FXML private TableColumn colCantidadF;
    @FXML private TableColumn colNumeroF;
    @FXML private TableColumn colCodigoF;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarProductos();
        cargarFacturas();
        cargarDetalleFactura();
    }
    
        public void seleccionarElemento(){

            txtCodigoF.setText(String.valueOf(((DetalleFactura)tblDetalle.getSelectionModel().getSelectedItem()).getCodigoDetalleFactura()));
            txtPrecioF.setText(String.valueOf(((DetalleFactura)tblDetalle.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
            txtCantidadF.setText(String.valueOf(((DetalleFactura)tblDetalle.getSelectionModel().getSelectedItem()).getCantidad()));

            int numeroFactura = ((DetalleFactura)tblDetalle.getSelectionModel().getSelectedItem()).getNumeroFactura();
            for (Factura factura : cbFacturas.getItems()) {
                if (factura.getNumeroFactura()== numeroFactura) {
                    cbFacturas.getSelectionModel().select(factura);
                    break;
                }
            }

            String codigoProducto = ((DetalleFactura)tblDetalle.getSelectionModel().getSelectedItem()).getCodigoProducto();

            for (Producto producto : cbProductos.getItems()) {
                if (producto.getCodigoProducto().equals(codigoProducto)) {
                    cbProductos.getSelectionModel().select(producto);
                    break;
                }
            }
        }
    
    public ObservableList<DetalleFactura> getDetalleFactura(){
            ArrayList<DetalleFactura> Lista = new ArrayList<>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarDetalleFactura()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                Lista.add(new DetalleFactura (resultado.getInt("codigoDetalleFactura"),
                    resultado.getDouble("precioUnitario"),                                        
                    resultado.getInt("cantidad"),
                    resultado.getInt("numeroFactura"),
                    resultado.getString("codigoProducto")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDetalleFactura = FXCollections.observableArrayList(Lista);
    }
    
    
    public ObservableList<Factura> getFactura(){
        ArrayList<Factura> Lista = new ArrayList<>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarFactura()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                Lista.add(new Factura (resultado.getInt("numeroFactura"),                            
                                        resultado.getString("estado"),
                                        resultado.getDouble("totalFactura"),
                                        resultado.getDate("fechaFactura")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaFactura = FXCollections.observableArrayList(Lista);
    }
    
    public ObservableList<Producto> getProductos() {
    ArrayList <Producto> lista = new ArrayList(); 
    
    try {
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarProductos()}");
        ResultSet resultado = procedimiento.executeQuery();
        while (resultado.next()) {
            
            Producto producto = new Producto(
                resultado.getString("codigoProducto"),
                resultado.getString("descripcionProducto"),
                Double.parseDouble(resultado.getString("precioUnitario")),
                Double.parseDouble(resultado.getString("precioDocena")),
                Double.parseDouble(resultado.getString("precioMayor")),
                resultado.getString("imagenProducto"),
                Integer.parseInt(resultado.getString("existencia")),
                Integer.parseInt(resultado.getString("tipoProducto")),
                Integer.parseInt(resultado.getString("proveedor"))
            );
            lista.add(producto);
            
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaProductos = FXCollections.observableArrayList(lista);
    }
    
    public void cargarFacturas() {
        cbFacturas.setItems(getFactura());
    }
    
    public void cargarProductos() {
        cbProductos.setItems(getProductos());
    }
    
    public void cargarDetalleFactura() {
        tblDetalle.setItems(getDetalleFactura());
        colDetalleF.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("codigoDetalleFactura"));
        colPrecioF.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Double>("precioUnitario"));
        colCantidadF.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("cantidad"));
        colNumeroF.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("numeroFactura"));
        colCodigoF.setCellValueFactory(new PropertyValueFactory<DetalleFactura, String>("codigoProducto"));
        
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
        DetalleFactura registro = new DetalleFactura();
        
        registro.setPrecioUnitario(Double.parseDouble(txtPrecioF.getText()));
        registro.setCantidad(Integer.parseInt(txtCantidadF.getText()));
        registro.setNumeroFactura(cbFacturas.getSelectionModel().getSelectedItem().getNumeroFactura());
        registro.setCodigoProducto(cbProductos.getSelectionModel().getSelectedItem().getCodigoProducto());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarDetalleFactura(?, ?, ?, ?)}");
            procedimiento.setDouble(1, registro.getPrecioUnitario());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.setInt(3, registro.getNumeroFactura());
            procedimiento.setString(4, registro.getCodigoProducto());
            
            procedimiento.execute();
            cargarDetalleFactura();
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
                if(tblDetalle.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar si va a ellminar registro",
                            "Eliminar detalle factura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarDetalleFactura(?)}");
                            procedimiento.setInt(1, ((DetalleFactura)tblDetalle.getSelectionModel().getSelectedItem()).getCodigoDetalleFactura());
                            procedimiento.execute();
                            listaDetalleFactura.remove(tblDetalle.getSelectionModel().getSelectedItem());
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
                if(tblDetalle.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/davidmorente/images/Editar.png"));
                    imgReporte.setImage(new Image("/org/davidmorente/images/Cancelar.png"));
                    tipoDeOperaciones = MenuDetalleFacturaController.operaciones.ACTUALIZAR; 
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
                tipoDeOperaciones = MenuDetalleFacturaController.operaciones.NINGUNO;
                cargarProductos();
            break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarDetalleFactura(?, ?, ?, ?, ?)}");
            DetalleFactura registro = (DetalleFactura)tblDetalle.getSelectionModel().getSelectedItem();
            registro.setCodigoDetalleFactura(Integer.parseInt(txtCodigoF.getText()));
            registro.setPrecioUnitario(Double.parseDouble(txtPrecioF.getText()));
            registro.setCantidad(Integer.parseInt(txtCantidadF.getText()));
            registro.setNumeroFactura(cbFacturas.getSelectionModel().getSelectedItem().getNumeroFactura());
            registro.setCodigoProducto(cbProductos.getSelectionModel().getSelectedItem().getCodigoProducto());
            procedimiento.setInt(1, registro.getCodigoDetalleFactura());
            procedimiento.setDouble(2, registro.getPrecioUnitario());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.setInt(4, registro.getNumeroFactura());
            procedimiento.setString(5, registro.getCodigoProducto());
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
                cargarProductos();
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void desactivarControles(){
        txtCodigoF.setEditable(false);
        txtPrecioF.setEditable(false);
        txtCantidadF.setEditable(false);
        cbFacturas.setEditable(false);
        cbProductos.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoF.setEditable(false);
        txtPrecioF.setEditable(true);
        txtCantidadF.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoF.clear();
        txtPrecioF.clear();
        txtCantidadF.clear();
    }
    
    public Main getEscenarioPrincipal() {
        return escenarioDetalleFactura;
    }
    
    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioDetalleFactura = escenarioPrincipal;
    }
    
    @FXML
    public void clickRegresar(ActionEvent event){
        if (event.getSource() == btnRegresar){
            escenarioDetalleFactura.menuPrincipalView();
        }
    }
}