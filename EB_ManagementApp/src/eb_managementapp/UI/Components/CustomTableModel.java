/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eb_managementapp.UI.Components;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author panay
 */
public class CustomTableModel extends DefaultTableModel{
    
     @Override
    public boolean isCellEditable(int row, int column) {
       if(column == 0){
           return false;
       }
       else{
           return true;
       }
    }
}
