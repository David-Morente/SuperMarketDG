/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.davidmorente.controllers;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.davidmorente.bean.Compra;
import org.davidmorente.db.Conexion;
import org.davidmorente.system.Main;

/**
 *
 * @author david
 */
public class MenuComprasController implements Initializable{
    private Main escenarioCompras;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Compra> listaCompra;
    
    @FXML Button btnRegresar;
    @FXML private TextField txtNumeroD;
    @FXML private TextField txtDescripcionD;
    @FXML private DatePicker datePickerFecha;
    @FXML private TextField txtTotalD;
    @FXML private TableColumn colNumeroD;
    @FXML private TableColumn colFechaD;
    @FXML private TableColumn colDescripcionD;
    @FXML private TableColumn colTotalD;
    @FXML private TableView tblCompras;
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
        tblCompras.setItems(getCompra());
        colNumeroD.setCellValueFactory(new PropertyValueFactory<Compra, Integer>("numeroDocumento"));
        colFechaD.setCellValueFactory(new PropertyValueFactory<Compra, DatePicker>("fechaDocumento"));
        colDescripcionD.setCellValueFactory(new PropertyValueFactory<Compra, String>("descripcion"));
        colTotalD.setCellValueFactory(new PropertyValueFactory<Compra, Double>("totalDocumento"));
    }
    
    public void seleccionarElemento(){
        txtNumeroD.setText(String.valueOf(((Compra)tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
        datePickerFecha.setValue(this.convertirLocalDate(((Compra)tblCompras.getSelectionModel().getSelectedItem()).getFechaDocumento().toString()));
        txtDescripcionD.setText(((Compra)tblCompras.getSelectionModel().getSelectedItem()).getDescripcion());
        txtTotalD.setText(((Compra)tblCompras.getSelectionModel().getSelectedItem()).getTotalDocumento().toString());
    }
    
    public LocalDate convertirLocalDate(String fecha){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(fecha, formatter);
        return localDate;
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
        Compra registro = new Compra();
        
        LocalDate localDate = datePickerFecha.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date fecha = Date.from(instant);
        
        registro.setFechaDocumento(fecha);
        registro.setDescripcion(txtDescripcionD.getText());
        registro.setTotalDocumento(Double.parseDouble(txtTotalD.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCompras(?, ?, ?)}");
            procedimiento.setDate(1, java.sql.Date.valueOf( datePickerFecha.getValue() ));
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.setDouble(3, registro.getTotalDocumento());
            procedimiento.execute();
            this.cargarDatos();
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
                if(tblCompras.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar si va a ellminar registro",
                            "Eliminar compras", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCompras(?)}");
                                procedimiento.setInt(1, ((Compra)tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento());
                            procedimiento.execute();
                            listaCompra.remove(tblCompras.getSelectionModel().getSelectedItem());
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
                if(tblCompras.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/davidmorente/images/Editar.png"));
                    imgReporte.setImage(new Image("/org/davidmorente/images/Cancelar.png"));
                    tipoDeOperaciones = MenuComprasController.operaciones.ACTUALIZAR; 
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
                tipoDeOperaciones = MenuComprasController.operaciones.NINGUNO;
                cargarDatos();
            break;
        }
    }
    
    public void actualizar(){
        try{
            LocalDate localDate = datePickerFecha.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date fecha = Date.from(instant);
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarCompras(?, ?, ?, ?)}");
            Compra registro = (Compra)tblCompras.getSelectionModel().getSelectedItem();
            registro.setFechaDocumento(fecha);
            registro.setDescripcion(txtDescripcionD.getText());
            registro.setTotalDocumento(Double.parseDouble(txtTotalD.getText()));
            procedimiento.setInt(1, registro.getNumeroDocumento());
            procedimiento.setDate(2, java.sql.Date.valueOf( datePickerFecha.getValue() ));
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.setDouble(4, registro.getTotalDocumento());
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
        txtNumeroD.setEditable(false);
        txtDescripcionD.setEditable(false);
        datePickerFecha.setEditable(false);
        txtTotalD.setEditable(false);
    }
    
    public void activarControles(){
        txtNumeroD.setEditable(false);
        txtDescripcionD.setEditable(true);
        datePickerFecha.setEditable(true);
        txtTotalD.setEditable(true);
    }
    
    public void limpiarControles(){
        txtNumeroD.clear();
        txtDescripcionD.clear();
        datePickerFecha.setValue(LocalDate.now());
        txtTotalD.clear();
    }    
        
    public Main getEscenarioPrincipal() {
        return escenarioCompras;
    }
    
    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioCompras = escenarioPrincipal;
    }
    
    @FXML
    public void clickRegresar(ActionEvent event){
         if (event.getSource() == btnRegresar){
        escenarioCompras.menuPrincipalView();
        }
    }
}

