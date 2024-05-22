/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.davidmorente.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.davidmorente.system.Main;

/**
 *
 * @author informatica
 */
public class ProgramadorControllers implements Initializable{
    private Main escenarioProgramador;
    @FXML private Button btnRegresar2;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
 
    public Main getEscenarioProgramador() {
        return escenarioProgramador;
    }
 
    public void setEscenarioProgramador(Main escenarioProgramador) {
        this.escenarioProgramador = escenarioProgramador;
    }
    
    @FXML
    public void clickMenuPrincipal(ActionEvent event){
        if(event.getSource() == btnRegresar2){
            escenarioProgramador.menuPrincipalView();
        }
    }
}
