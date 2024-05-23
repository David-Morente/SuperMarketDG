/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.davidmorente.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.davidmorente.system.Main;

/**
 *
 * @author informatica
 */
public class MenuPrincipalControllers implements Initializable {
    private Main escenarioPrincipal;
    @FXML MenuItem btnMenuclientes;
    @FXML MenuItem btnProveedor;
    @FXML MenuItem btnTipoProducto;
    @FXML MenuItem btnMenuCargoE;
    @FXML MenuItem btnMenuCompras;
    @FXML MenuItem btnMenuProductos;
    @FXML MenuItem btnMenuDetalleCompra;
    @FXML MenuItem btnProgramador;
    @FXML private TextField txtDireccionP;
    @FXML private TextField txtWebP;
    @FXML private TextField txtCodigoP;
    @FXML private TextField txtNitP;
    @FXML private TextField txtNombreP;
    @FXML private TextField txtApellidoP;
    @FXML private TextField txtContactoP;
    @FXML private TextField txtRazonSocial;    
    @FXML private TableView tblProveedor;
    
    @Override
    public void initialize (URL location, ResourceBundle resources)  {

    }

    public Main getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Main escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
        
    @FXML
    public void clicClientes (ActionEvent event)throws IOException{
        if (event.getSource() == btnMenuclientes){
            escenarioPrincipal.menuClientesView();
        }
    }
    
    @FXML
    public void clicProgramador (ActionEvent event)throws IOException{
        if (event.getSource() == btnProgramador){
            escenarioPrincipal.verVentanaProgramador();
        }
    }
    
    @FXML
    public void clicProveedor (ActionEvent event)throws IOException{
        if (event.getSource() == btnProveedor){
            escenarioPrincipal.verVentanaProveedor();
        }
    }
    
    @FXML
    public void clicTipoP (ActionEvent event)throws IOException{
        if (event.getSource() == btnTipoProducto){
            escenarioPrincipal.tipoDeProducto();
        }
    }
    
    @FXML
    public void clicCompra (ActionEvent event)throws IOException{
        if (event.getSource() == btnMenuCompras){
            escenarioPrincipal.Compras();
        }
    }
    
    @FXML
    public void clicCargoE (ActionEvent event)throws IOException{
        if (event.getSource() == btnMenuCargoE){
            escenarioPrincipal.cargoEmpleado();
        }
    }
    
    @FXML
    public void clicProducto (ActionEvent event)throws IOException{
        if (event.getSource() == btnMenuProductos){
            escenarioPrincipal.Productos();
        }
    }
    
    @FXML
    public void clicDetalleProducto (ActionEvent event)throws IOException{
        if (event.getSource() == btnMenuDetalleCompra){
            escenarioPrincipal.DetalleCompra();
        }
    }
}
