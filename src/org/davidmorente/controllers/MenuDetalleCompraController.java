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
import javafx.scene.image.ImageView;
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
