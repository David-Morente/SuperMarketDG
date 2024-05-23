/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.davidmorente.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.davidmorente.bean.Compra;
import org.davidmorente.bean.DetalleCompra;
import org.davidmorente.bean.Producto;
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
    
    
    public Main getEscenarioPrincipal() {
        return escenarioDetalleCompra;
    }
    
    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioDetalleCompra = escenarioPrincipal;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    @FXML
    public void clickRegresar(ActionEvent event){
        if (event.getSource() == btnRegresar){
            escenarioDetalleCompra.menuPrincipalView();
        }
    }
}
