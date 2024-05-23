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
import org.davidmorente.bean.Producto;
import org.davidmorente.bean.Proveedores;
import org.davidmorente.bean.TipoDeProducto;
import org.davidmorente.db.Conexion;
import org.davidmorente.system.Main;

/**
 *
 * @author david
 */
public class MenuProductosController implements Initializable {
    private Main escenarioProductos;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Producto> listaProductos;
    private ObservableList<Proveedores> listaProveedores;
    private ObservableList<TipoDeProducto> listaTipoDeProductos;
    
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
    @FXML private TextField txtCodigoP;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPrecioU;
    @FXML private TextField txtPrecioD;
    @FXML private TextField txtPrecioM;
    @FXML private TextField txtImagen;
    @FXML private TextField txtExistencia;
    @FXML private ComboBox<Proveedores> cbProveedores;
    @FXML private ComboBox<TipoDeProducto> cbTipoDeProductos;
    
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodigoP;
    @FXML private TableColumn colDescripcion;
    @FXML private TableColumn colPrecioU;
    @FXML private TableColumn colPrecioD;
    @FXML private TableColumn colPrecioM;
    @FXML private TableColumn colImagen;
    @FXML private TableColumn colExistencia;
    @FXML private TableColumn colTipoDeProducto;
    @FXML private TableColumn colProveedor;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //cargarDatos();
        cargarProductos();
        cargarTipoDeProductos();
        cargarProveedores();
        
    }
    
    public void seleccionarElemento(){
        txtCodigoP.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
        txtDescripcion.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto());
        txtPrecioU.setText(String.valueOf((((Producto)tblProductos.getSelectionModel().getSelectedItem()).getPrecioUnitario())));
        txtPrecioD.setText(String.valueOf((((Producto)tblProductos.getSelectionModel().getSelectedItem()).getPrecioDocena())));
        txtPrecioM.setText(String.valueOf((((Producto)tblProductos.getSelectionModel().getSelectedItem()).getPrecioMayor())));
        txtImagen.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getImagenProducto());
        txtExistencia.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getExistencia()));
        
        int indexP = ((Producto)tblProductos.getSelectionModel().getSelectedItem()).getProveedor() - 1;
        cbProveedores.getSelectionModel().select(indexP);
        
        int indexTP = ((Producto)tblProductos.getSelectionModel().getSelectedItem()).getTipoProducto() - 1;
        cbTipoDeProductos.getSelectionModel().select(indexTP);
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
    
    public ObservableList<TipoDeProducto> getTipoDeProducto(){
        ArrayList<TipoDeProducto> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTipoDeProducto()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                Lista.add(new TipoDeProducto (resultado.getInt("codigoTipoProducto"),
                                        resultado.getString("descripcion")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoDeProductos = FXCollections.observableArrayList(Lista);
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
    
    public void cargarProveedores() {
        cbProveedores.setItems(getProveedores());
    }
    
    public void cargarTipoDeProductos() {
        cbTipoDeProductos.setItems(getTipoDeProducto());
    }
    
    public void cargarProductos() {
        tblProductos.setItems(getProductos());
        colCodigoP.setCellValueFactory(new PropertyValueFactory<Producto, String>("codigoProducto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Producto, String>("descripcionProducto"));
        colPrecioU.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioUnitario"));
        colPrecioD.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioDocena"));
        colPrecioM.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioMayor"));
        colImagen.setCellValueFactory(new PropertyValueFactory<Producto, String>("imagenProducto"));
        colExistencia.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("existencia"));
        colTipoDeProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("tipoProducto"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<Producto, String>("proveedor"));
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
        Producto registro = new Producto();
        
        registro.setCodigoProducto(txtCodigoP.getText());
        registro.setDescripcionProducto(txtDescripcion.getText());
        registro.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
        registro.setPrecioDocena(Double.parseDouble(txtPrecioD.getText()));
        registro.setPrecioMayor(Double.parseDouble(txtPrecioM.getText()));
        registro.setImagenProducto(txtImagen.getText());
        registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
        registro.setTipoProducto(cbTipoDeProductos.getSelectionModel().getSelectedItem().getCodigoTipoProducto());
        registro.setProveedor(cbProveedores.getSelectionModel().getSelectedItem().getCodigoProveedor());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarProductos(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.setDouble(3, registro.getPrecioUnitario());
            procedimiento.setDouble(4, registro.getPrecioDocena());
            procedimiento.setDouble(5, registro.getPrecioMayor());
            procedimiento.setString(6, registro.getImagenProducto());
            procedimiento.setInt(7, registro.getExistencia());
            procedimiento.setInt(8, registro.getTipoProducto());
            procedimiento.setInt(9, registro.getProveedor());
            
            procedimiento.execute();
            listaProductos.add(registro);
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
                if(tblProductos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar si va a ellminar registro",
                            "Eliminar productos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarProductos(?)}");
                            procedimiento.setString(1, ((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            procedimiento.execute();
                            listaProductos.remove(tblProductos.getSelectionModel().getSelectedItem());
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
                if(tblProductos.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/davidmorente/images/Editar.png"));
                    imgReporte.setImage(new Image("/org/davidmorente/images/Cancelar.png"));
                    tipoDeOperaciones = MenuProductosController.operaciones.ACTUALIZAR; 
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
                tipoDeOperaciones = MenuProductosController.operaciones.NINGUNO;
                cargarProductos();
            break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarProductos(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            Producto registro = (Producto)tblProductos.getSelectionModel().getSelectedItem();
            registro.setCodigoProducto(txtCodigoP.getText());
            registro.setDescripcionProducto(txtDescripcion.getText());
            registro.setPrecioUnitario(Double.parseDouble(txtPrecioU.getText()));
            registro.setPrecioDocena(Double.parseDouble(txtPrecioD.getText()));
            registro.setPrecioMayor(Double.parseDouble(txtPrecioM.getText()));
            registro.setImagenProducto(txtImagen.getText());
            procedimiento.setString(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.setDouble(3, registro.getPrecioUnitario());
            procedimiento.setDouble(4, registro.getPrecioDocena());
            procedimiento.setDouble(5, registro.getPrecioMayor());
            procedimiento.setString(6, registro.getImagenProducto());
            procedimiento.setInt(7, registro.getExistencia());
            procedimiento.setInt(8, registro.getTipoProducto());
            procedimiento.setInt(9, registro.getProveedor());
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
        txtCodigoP.setEditable(false);
        txtDescripcion.setEditable(false);
        txtPrecioU.setEditable(false);
        txtPrecioD.setEditable(false);
        txtPrecioM.setEditable(false);
        txtImagen.setEditable(false);
        txtExistencia.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoP.setEditable(false);
        txtDescripcion.setEditable(true);
        txtPrecioU.setEditable(true);
        txtPrecioD.setEditable(true);
        txtPrecioM.setEditable(true);
        txtImagen.setEditable(true);
        txtExistencia.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoP.clear();
        txtDescripcion.clear();
        txtPrecioU.clear();
        txtPrecioD.clear();
        txtPrecioM.clear();
        txtImagen.clear();
        txtExistencia.clear();
    }
    
    public Main getEscenarioPrincipal() {
        return escenarioProductos;
    }
    
    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioProductos = escenarioPrincipal;
    }
    
    @FXML
    public void clickRegresar(ActionEvent event){
        if (event.getSource() == btnRegresar){
            escenarioProductos.menuPrincipalView();
        }
    }
}
