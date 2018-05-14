/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eb_managementapp.UI.Forms;

import Utilities.HTTPConnection;
import static eb_managementapp.EB_ManagementApp.setUpForm;
import eb_managementapp.Entities.Customers;
import eb_managementapp.Entities.Products;
import eb_managementapp.UI.Components.CheckboxGroup;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import org.json.JSONArray;
import org.json.JSONObject;

public class AddCustomerProductsForm extends javax.swing.JFrame {

    final String TITLE = "Add Customer Products";
    private ArrayList<Products> productList;
    private ArrayList<Customers> customerList;

    private CheckboxGroup productCheckBoxGroup;

    public AddCustomerProductsForm() {
        initComponents();

        ImageIcon imageIcon = new ImageIcon("C:\\Users\\panay\\Desktop\\EasyBusiness\\EB_ManagementApp\\src\\eb_managementapp\\UI\\Images\\mini_logo.fw.png");
        setIconImage(imageIcon.getImage());
        
        getCustomers();
        getProducts();

        pack();
        setLocationRelativeTo(null);

        //---
        setTitle(TITLE);
        setVisible(true);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        productPanel = new javax.swing.JPanel();
        customerLabel = new javax.swing.JLabel();
        customerComboBox = new javax.swing.JComboBox<>();
        sizeLabel = new javax.swing.JLabel();
        addProductButton = new javax.swing.JButton();
        buttonPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        productPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Products", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13), new java.awt.Color(153, 153, 153))); // NOI18N

        customerLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        customerLabel.setText("Customer:");

        customerComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerComboBoxActionPerformed(evt);
            }
        });

        sizeLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        sizeLabel.setText("Select Products:");

        addProductButton.setText("Add Product");
        addProductButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProductButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout productPanelLayout = new javax.swing.GroupLayout(productPanel);
        productPanel.setLayout(productPanelLayout);
        productPanelLayout.setHorizontalGroup(
            productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sizeLabel)
                    .addComponent(customerLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(customerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
            .addGroup(productPanelLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(addProductButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        productPanelLayout.setVerticalGroup(
            productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(customerLabel)
                    .addComponent(customerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sizeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 156, Short.MAX_VALUE)
                .addComponent(addProductButton)
                .addContainerGap())
        );

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        nextButton.setText("Next >");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cancelButton)
                .addGap(18, 18, 18)
                .addComponent(nextButton)
                .addContainerGap())
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(nextButton))
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buttonPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(productPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(productPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(7, 7, 7)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setVisible(false);
        setUpForm = new SetUpForm(this);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void addProductButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProductButtonActionPerformed
        addCustomerProducts();
    }//GEN-LAST:event_addProductButtonActionPerformed

    private void customerComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerComboBoxActionPerformed


    }//GEN-LAST:event_customerComboBoxActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        setVisible(false);
        setUpForm = new SetUpForm(this);
    }//GEN-LAST:event_nextButtonActionPerformed

    private void addCustomerProducts() {

        int customerID = customerList.get(customerComboBox.getSelectedIndex()).getID();
        
        ArrayList<Integer> productIDs = new ArrayList();
        for (int i = 0; i < productCheckBoxGroup.getCheckBoxes().size(); i++) {
            JCheckBox checkBox = productCheckBoxGroup.getCheckBoxes().get(i);
            if (checkBox.isSelected()) {
                productIDs.add(productList.get(i).getID());
            }
        }
        
        boolean success = true;
        
        for (int i = 0; i < productIDs.size(); i++) {

            //Make the call:
            String addProductionJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Customerproducts", "Create",
                    "SessionID=aa&ID=0&CustomerID=" + customerID + "&ProductID=" + productIDs.get(i));
            
            try {
                JSONObject jsonObject = new JSONObject(addProductionJSON);
                final String status = jsonObject.getString("Status");
                final String title = jsonObject.getString("Title");
                final String message = jsonObject.getString("Message");

                if (!status.equals("OK")) {
                    success = false;
                }
                
                

                if (status.equals(HTTPConnection.RESPONSE_ERROR)) {
                } else if (status.equals(HTTPConnection.RESPONSE_OK)) {
                    //Reset fields:
                    setVisible(true);
                    customerComboBox.setSelectedIndex(0);
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(success){
        showMessageDialog(null, "Items added Succesfully", "Added Items", JOptionPane.PLAIN_MESSAGE);
        }
        else{
        showMessageDialog(null, "Failed to add items", "Items not added", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void getProducts() {
        productList = new ArrayList<>();

        //Get customers from api
        String productsJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Products", "GetMultiple", "SessionID=aa");
        try {
            JSONObject jsonObject = new JSONObject(productsJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                JSONArray dataArray = jsonObject.getJSONArray("Data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject currentItem = dataArray.getJSONObject(i);

                    int id = currentItem.getInt("ID");
                    String name = currentItem.getString("Name");
                    float price = currentItem.getFloat("Price");
                    int quantity = currentItem.getInt("QuantityInStock");
                    int productSizeID = currentItem.getInt("ProductSizeID");
                    int productTypeID = currentItem.getInt("ProductTypeID");
                    int productSuppliesID = currentItem.getInt("ProductSuppliesID");

                    Products c = new Products(id, name, price, quantity, productSizeID, productTypeID, productSuppliesID);
                    productList.add(c);

                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] productListString = new String[productList.size()];

        for (int i = 0; i < productList.size(); i++) {
            productListString[i] = productList.get(i).getName();
        }

        productCheckBoxGroup = new CheckboxGroup(productListString);//String
        productCheckBoxGroup.setBounds(new Rectangle(new Point(125, 70), productCheckBoxGroup.getPreferredSize()));
        productPanel.add(productCheckBoxGroup);
    }

    public void getCustomers() {
        customerList = new ArrayList<>();

        customerComboBox.removeAllItems();

        //Get customers from api
        String customersJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Customers", "GetMultiple", "SessionID=aa");
        try {
            JSONObject jsonObject = new JSONObject(customersJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                JSONArray dataArray = jsonObject.getJSONArray("Data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject currentItem = dataArray.getJSONObject(i);

                    int id = currentItem.getInt("ID");
                    String name = currentItem.getString("Name");
                    int countryID = currentItem.getInt("CountryID");
                    String city = currentItem.getString("City");
                    String telephone = currentItem.getString("Telephone");
                    String address = currentItem.getString("Address");
                    int customerProductsID = currentItem.getInt("CustomerProductsID");

                    Customers customer = new Customers(id, name, countryID, city, telephone, address, customerProductsID);
                    customerList.add(customer);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < customerList.size(); i++) {
            customerComboBox.addItem(customerList.get(i).getName());
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddCustomerProductsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddCustomerProductsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddCustomerProductsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddCustomerProductsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddCustomerProductsForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addProductButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox<String> customerComboBox;
    private javax.swing.JLabel customerLabel;
    private javax.swing.JButton nextButton;
    private javax.swing.JPanel productPanel;
    private javax.swing.JLabel sizeLabel;
    // End of variables declaration//GEN-END:variables
}
