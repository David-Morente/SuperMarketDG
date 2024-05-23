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
import org.davidmorente.bean.Compra;
import org.davidmorente.bean.DetalleCompra;
import org.davidmorente.bean.Producto;
import org.davidmorente.db.Conexion;
import org.davidmorente.system.Main;

/**
 *
 * @author david
 */
public class MenuDetalleCompraController implements Initializable {
    private Main escenarioDetalleCompra;

    
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<DetalleCompra> listaDetalleCompra;
    private ObservableList<Producto> listaProductos;
    private ObservableList<Compra> listaCompra;
    
    @FXML Button btnRegresar;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReportes;
    @FXML private ImageView imgAgregar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
     // ELEMENTOS
    @FXML private TextField txtCodigoDC;
    @FXML private TextField txtCosto;
    @FXML private TextField txtUnidad;
    @FXML private ComboBox<Producto> cbProductos;
    @FXML private ComboBox<Compra> cbCompras;
    
    @FXML private TableView tblDetalle;
    @FXML private TableColumn colCodigoDC;
    @FXML private TableColumn colCosto;
    @FXML private TableColumn colUnidad;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colNumeroDocumento;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarProductos();
        cargarCompras();
        cargarDetalle();
    }
    
    public void seleccionarElemento(){
        txtCodigoDC.setText(String.valueOf(((DetalleCompra)tblDetalle.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra()));
        txtCosto.setText(String.valueOf(((DetalleCompra)tblDetalle.getSelectionModel().getSelectedItem()).getCostoUnitario()));
        txtUnidad.setText(String.valueOf(((DetalleCompra)tblDetalle.getSelectionModel().getSelectedItem()).getUnidad()));
        
        //int indexP = ((Producto)tblDetalle.getSelectionModel().getSelectedItem()).getProveedor() - 1;
        //cbProductos.getSelectionModel().select(indexP);
        
        int indexTP = ((DetalleCompra)tblDetalle.getSelectionModel().getSelectedItem()).getNumeroDocumento() - 1;
        cbCompras.getSelectionModel().select(indexTP);
    }
    
    public ObservableList<DetalleCompra> getDetalleCompras(){
        ArrayList<DetalleCompra> Lista = new ArrayList<>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarDetalleCompra()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                Lista.add(new DetalleCompra (resultado.getInt("codigoDetalleCompra"),
                    resultado.getDouble("costoUnitario"),                                        
                    resultado.getInt("unidad"),
                    resultado.getString("codigoProducto"),
                    resultado.getInt("numeroDocumento")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDetalleCompra = FXCollections.observableArrayList(Lista);
    }
    
    
    public ObservableList<Compra> getCompra(){
        ArrayList<Compra> Lista = new ArrayList<>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCompras()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                Lista.add(new Compra (resultado.getInt("numeroDocumento"),
                                        resultado.getDate("fechaDocumento"),                                        
                                        resultado.getString("descripcion"),
                                        resultado.getDouble("totalDocumento")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCompra = FXCollections.observableArrayList(Lista);
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
    
    public void cargarProductos() {
        cbProductos.setItems(getProductos());
    }
    
    public void cargarCompras() {
        cbCompras.setItems(getCompra());
    }
    
    public void cargarDetalle() {
        tblDetalle.setItems(getDetalleCompras());
        colCodigoDC.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("codigoDetalleCompra"));
        colCosto.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Double>("costoUnitario"));
        colUnidad.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("unidad"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<DetalleCompra, String>("codigoProducto"));
        colNumeroDocumento.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("numeroDocumento"));
        
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
        DetalleCompra registro = new DetalleCompra();
        
        registro.setCostoUnitario(Double.parseDouble(txtCosto.getText()));
        registro.setUnidad(Integer.parseInt(txtUnidad.getText()));
        registro.setCodigoProducto(cbProductos.getSelectionModel().getSelectedItem().getCodigoProducto());
        registro.setNumeroDocumento(cbCompras.getSelectionModel().getSelectedItem().getNumeroDocumento());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarDetalleCompra(?, ?, ?, ?)}");
            procedimiento.setDouble(1, registro.getCostoUnitario());
            procedimiento.setInt(2, registro.getUnidad());
            procedimiento.setString(3, registro.getCodigoProducto());
            procedimiento.setInt(4, registro.getNumeroDocumento());
            
            procedimiento.execute();
            cargarDetalle();
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
                            "Eliminar detalle compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarDetalleCompra(?)}");
                            procedimiento.setInt(1, ((DetalleCompra)tblDetalle.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra());
                            procedimiento.execute();
                            listaDetalleCompra.remove(tblDetalle.getSelectionModel().getSelectedItem());
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
                    tipoDeOperaciones = MenuDetalleCompraController.operaciones.ACTUALIZAR; 
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
                tipoDeOperaciones = MenuDetalleCompraController.operaciones.NINGUNO;
                cargarProductos();
            break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarProductos(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            DetalleCompra registro = (DetalleCompra)tblDetalle.getSelectionModel().getSelectedItem();
            registro.setCodigoDetalleCompra(Integer.parseInt(txtCodigoDC.getText()));
            registro.setCostoUnitario(Double.parseDouble(txtCosto.getText()));
            registro.setUnidad(Integer.parseInt(txtUnidad.getText()));
            registro.setCodigoProducto(cbProductos.getSelectionModel().getSelectedItem().getCodigoProducto());
            registro.setNumeroDocumento(cbCompras.getSelectionModel().getSelectedItem().getNumeroDocumento());
            procedimiento.setInt(1, registro.getCodigoDetalleCompra());
            procedimiento.setDouble(2, registro.getCostoUnitario());
            procedimiento.setInt(3, registro.getUnidad());
            procedimiento.setString(4, registro.getCodigoProducto());
            procedimiento.setInt(5, registro.getNumeroDocumento());
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
        txtCodigoDC.setEditable(false);
        txtCosto.setEditable(false);
        txtUnidad.setEditable(false);
        cbProductos.setEditable(false);
        cbCompras.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoDC.setEditable(false);
        txtCosto.setEditable(true);
        txtUnidad.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoDC.clear();
        txtCosto.clear();
        txtUnidad.clear();
    }
    
    public Main getEscenarioPrincipal() {
        return escenarioDetalleCompra;
    }
    
    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioDetalleCompra = escenarioPrincipal;
    }
    
    @FXML
    public void clickRegresar(ActionEvent event){
        if (event.getSource() == btnRegresar){
            escenarioDetalleCompra.menuPrincipalView();
        }
    }
}
