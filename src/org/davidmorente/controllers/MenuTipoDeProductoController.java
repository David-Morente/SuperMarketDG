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
import org.davidmorente.bean.TipoDeProducto;
import org.davidmorente.db.Conexion;
import org.davidmorente.system.Main;

/**
 *
 * @author david
 */
public class MenuTipoDeProductoController implements Initializable{
    private Main escenarioTipoDeProducto;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<TipoDeProducto> listaTipoDeProducto;
    
    @FXML Button btnRegresar;
    
    @FXML private TextField txtCodigoP;
    @FXML private TextField txtDescripcionTP;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colDescripcion;
    @FXML private TableView tblTipoDeProducto;
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
        tblTipoDeProducto.setItems(getTipoDeProducto());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<TipoDeProducto, Integer>("codigoTipoProducto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoDeProducto, String>("descripcion"));
    }
    
    public void seleccionarElemento(){
        txtCodigoP.setText(String.valueOf(((TipoDeProducto)tblTipoDeProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
        txtDescripcionTP.setText(((TipoDeProducto)tblTipoDeProducto.getSelectionModel().getSelectedItem()).getDescripcion());
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
        return listaTipoDeProducto = FXCollections.observableArrayList(Lista);
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
        TipoDeProducto registro = new TipoDeProducto();
        registro.setCodigoTipoProducto(Integer.parseInt(txtCodigoP.getText()));
        registro.setDescripcion(txtDescripcionTP.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTipoProducto(?, ?)}");
            procedimiento.setInt(1, registro.getCodigoTipoProducto());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
            listaTipoDeProducto.add(registro);
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
                if(tblTipoDeProducto.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar si va a ellminar registro",
                            "Eliminar tipo de producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTipoDeProducto(?)}");
                            procedimiento.setInt(1, ((TipoDeProducto)tblTipoDeProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
                            procedimiento.execute();
                            listaTipoDeProducto.remove(tblTipoDeProducto.getSelectionModel().getSelectedItem());
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
                if(tblTipoDeProducto.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/davidmorente/images/Editar.png"));
                    imgReporte.setImage(new Image("/org/davidmorente/images/Cancelar.png"));
                    txtCodigoP.setEditable(false);
                    tipoDeOperaciones = MenuTipoDeProductoController.operaciones.ACTUALIZAR; 
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
                tipoDeOperaciones = MenuTipoDeProductoController.operaciones.NINGUNO;
                cargarDatos();
            break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarTipoDeProducto(?, ?)}");
            TipoDeProducto registro = (TipoDeProducto)tblTipoDeProducto.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcionTP.getText());
            procedimiento.setInt(1, registro.getCodigoTipoProducto());
            procedimiento.setString(2, registro.getDescripcion());
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
        txtCodigoP.setEditable(false);
        txtDescripcionTP.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoP.setEditable(true);
        txtDescripcionTP.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoP.clear();
        txtDescripcionTP.clear();
    }
    
    public Main getEscenarioPrincipal() {
        return escenarioTipoDeProducto;
    }
    
    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioTipoDeProducto = escenarioPrincipal;
    }
    
    @FXML
    public void clickRegresar(ActionEvent event){
         if (event.getSource() == btnRegresar){
        escenarioTipoDeProducto.menuPrincipalView();
        }
    }
}
