/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eb_managementapp.UI.Components;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Platform;

public class AlertBox {
    
    String title;
    String headerMessage;
    String infoMessage;
    
    public AlertBox(String title, String headerMessage, String infoMessage) {
        this.title = title;
        this.headerMessage = headerMessage;
        this.infoMessage = infoMessage;
    }
    
    public AlertBox(String title, String infoMessage) {
        this.title = title;
        this.headerMessage = null;
        this.infoMessage = infoMessage;
    }
    
    public void show() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }
    
}

