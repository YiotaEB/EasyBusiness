/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eb_managementapp.UI.Forms;

import eb_managementapp.DB.ConnectionCreator;
import eb_managementapp.UI.Forms.SetUpForm;
import java.util.Vector;
import static eb_managementapp.EB_ManagementApp.setUpForm;
import eb_managementapp.UI.Components.JTableUtilities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.table.DefaultTableModel;

public class SuppliersForm extends javax.swing.JFrame {

    Vector<String> supplierNumbers;
    Vector<String> supplierNames;
    Vector<String> supplierIDs;
    Vector<String> suppliesNames;
    Vector<String> suppliesPrice;
    Vector<String> suppliesQuantity;
    Vector<String> columnNames;

    final String TITLE = "Add Supplies";

    public SuppliersForm() {
        initComponents();

        supplierNumbers = new Vector<>();
        supplierNames = new Vector<>();
        supplierIDs = new Vector<>();
        suppliesNames = new Vector<>();
        suppliesPrice = new Vector<>();
        suppliesQuantity = new Vector<>();
        columnNames = new Vector<>();

        //COUNTRIES SELECTION COMBOBOX
        try {
            //Select Statment to choose countries
            ConnectionCreator connectionCreator = new ConnectionCreator();
            Connection connection = connectionCreator.connect();

            Statement getCountryStatement = connection.createStatement();
            String qr = " Select Name From Countries";
            ResultSet rs = getCountryStatement.executeQuery(qr);

            countryComboBox.removeAllItems();
            // iterate through the java resultset
            while (rs.next()) {
                String typeName = rs.getString("Name");
                countryComboBox.addItem(typeName);
            }
            getCountryStatement.close();

        } catch (SQLException ex) {
            Logger.getLogger(SuppliersForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        updateSuppliersComboBox();

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

        tableSuppliersPanel = new javax.swing.JPanel();
        suppliersScrollPane = new javax.swing.JScrollPane();
        suppliersTable = new javax.swing.JTable();
        viewSuppliersButton = new javax.swing.JButton();
        supplierPanel = new javax.swing.JPanel();
        supplierNameLabel = new javax.swing.JLabel();
        supplierNameTextField = new javax.swing.JTextField();
        supplierTelephoneLabel = new javax.swing.JLabel();
        supplierTelephoneTextField = new javax.swing.JTextField();
        supplierAddressLabel = new javax.swing.JLabel();
        supplierAddressTextField = new javax.swing.JTextField();
        supplierCityLabel = new javax.swing.JLabel();
        supplierCityTextField = new javax.swing.JTextField();
        supplierCountryLabel = new javax.swing.JLabel();
        countryComboBox = new javax.swing.JComboBox<>();
        addSuppliersButton = new javax.swing.JButton();
        suppliesPanel = new javax.swing.JPanel();
        supplierLabel = new javax.swing.JLabel();
        supplierComboBox = new javax.swing.JComboBox<>();
        suppliesNameLabel = new javax.swing.JLabel();
        suppliesNameTextField = new javax.swing.JTextField();
        quantityLabel = new javax.swing.JLabel();
        quantitySpinner = new javax.swing.JSpinner();
        priceLabel = new javax.swing.JLabel();
        priceTextField = new javax.swing.JTextField();
        addSuppliesButton = new javax.swing.JButton();
        buttonPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        tableSuppliersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "List of Suppliers", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13), new java.awt.Color(153, 153, 153))); // NOI18N

        suppliersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        suppliersTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        suppliersTable.setAutoscrolls(false);
        suppliersTable.getTableHeader().setReorderingAllowed(false);
        suppliersScrollPane.setViewportView(suppliersTable);

        viewSuppliersButton.setText("View Suppliers");
        viewSuppliersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewSuppliersButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tableSuppliersPanelLayout = new javax.swing.GroupLayout(tableSuppliersPanel);
        tableSuppliersPanel.setLayout(tableSuppliersPanelLayout);
        tableSuppliersPanelLayout.setHorizontalGroup(
            tableSuppliersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tableSuppliersPanelLayout.createSequentialGroup()
                .addGroup(tableSuppliersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tableSuppliersPanelLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(suppliersScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tableSuppliersPanelLayout.createSequentialGroup()
                        .addContainerGap(260, Short.MAX_VALUE)
                        .addComponent(viewSuppliersButton)))
                .addContainerGap())
        );
        tableSuppliersPanelLayout.setVerticalGroup(
            tableSuppliersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tableSuppliersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(suppliersScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(viewSuppliersButton)
                .addContainerGap())
        );

        supplierPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Suppliers", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13), new java.awt.Color(153, 153, 153))); // NOI18N

        supplierNameLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        supplierNameLabel.setText("Name:");

        supplierNameTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        supplierTelephoneLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        supplierTelephoneLabel.setText("Telephone:");

        supplierTelephoneTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierTelephoneTextFieldActionPerformed(evt);
            }
        });

        supplierAddressLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        supplierAddressLabel.setText("Address:");

        supplierAddressTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierAddressTextFieldActionPerformed(evt);
            }
        });

        supplierCityLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        supplierCityLabel.setText("City:");

        supplierCityTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierCityTextFieldActionPerformed(evt);
            }
        });

        supplierCountryLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        supplierCountryLabel.setText("Country:");

        countryComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        countryComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countryComboBoxActionPerformed(evt);
            }
        });

        addSuppliersButton.setText("Add Supplier");
        addSuppliersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSuppliersButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout supplierPanelLayout = new javax.swing.GroupLayout(supplierPanel);
        supplierPanel.setLayout(supplierPanelLayout);
        supplierPanelLayout.setHorizontalGroup(
            supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(supplierPanelLayout.createSequentialGroup()
                .addGroup(supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(supplierPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(supplierPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(supplierCityLabel)
                                    .addComponent(supplierAddressLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(supplierAddressTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                                    .addComponent(supplierCityTextField)))
                            .addGroup(supplierPanelLayout.createSequentialGroup()
                                .addGroup(supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, supplierPanelLayout.createSequentialGroup()
                                        .addComponent(supplierNameLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                    .addGroup(supplierPanelLayout.createSequentialGroup()
                                        .addComponent(supplierTelephoneLabel)
                                        .addGap(10, 10, 10)))
                                .addGroup(supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(supplierTelephoneTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                                    .addComponent(supplierNameTextField)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, supplierPanelLayout.createSequentialGroup()
                        .addGap(0, 25, Short.MAX_VALUE)
                        .addGroup(supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, supplierPanelLayout.createSequentialGroup()
                                .addComponent(supplierCountryLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(countryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(addSuppliersButton, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        supplierPanelLayout.setVerticalGroup(
            supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(supplierPanelLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierNameLabel)
                    .addComponent(supplierNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierTelephoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(supplierTelephoneLabel))
                .addGap(11, 11, 11)
                .addGroup(supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierAddressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(supplierAddressLabel))
                .addGap(10, 10, 10)
                .addGroup(supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierCityLabel)
                    .addComponent(supplierCityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(countryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(supplierCountryLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addSuppliersButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        suppliesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Supplies", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13), new java.awt.Color(153, 153, 153))); // NOI18N

        supplierLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        supplierLabel.setText("Sypplier:");

        supplierComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        supplierComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierComboBoxActionPerformed(evt);
            }
        });

        suppliesNameLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        suppliesNameLabel.setText("Name:");

        suppliesNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppliesNameTextFieldActionPerformed(evt);
            }
        });

        quantityLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        quantityLabel.setText("Quantity:");

        priceLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        priceLabel.setText("Price: (€)");

        priceTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceTextFieldActionPerformed(evt);
            }
        });

        addSuppliesButton.setText("Add Supplies");
        addSuppliesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSuppliesButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout suppliesPanelLayout = new javax.swing.GroupLayout(suppliesPanel);
        suppliesPanel.setLayout(suppliesPanelLayout);
        suppliesPanelLayout.setHorizontalGroup(
            suppliesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(suppliesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(suppliesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(suppliesPanelLayout.createSequentialGroup()
                        .addGroup(suppliesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(supplierLabel)
                            .addComponent(suppliesNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(suppliesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(supplierComboBox, 0, 153, Short.MAX_VALUE)
                            .addComponent(suppliesNameTextField)))
                    .addGroup(suppliesPanelLayout.createSequentialGroup()
                        .addGroup(suppliesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, suppliesPanelLayout.createSequentialGroup()
                                .addComponent(priceLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(suppliesPanelLayout.createSequentialGroup()
                                .addComponent(quantityLabel)
                                .addGap(12, 12, 12)))
                        .addGroup(suppliesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(quantitySpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                            .addComponent(priceTextField))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addSuppliesButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        suppliesPanelLayout.setVerticalGroup(
            suppliesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, suppliesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(suppliesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(supplierLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(suppliesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(suppliesNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(suppliesNameLabel))
                .addGroup(suppliesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(suppliesPanelLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(addSuppliesButton))
                    .addGroup(suppliesPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(suppliesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(quantitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(quantityLabel))
                        .addGap(11, 11, 11)
                        .addGroup(suppliesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(priceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(priceLabel)))))
        );

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        nextButton.setText("Next ->");
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nextButton)
                .addContainerGap())
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(nextButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(supplierPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(suppliesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tableSuppliersPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(supplierPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(suppliesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tableSuppliersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void viewSuppliersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewSuppliersButtonActionPerformed

        columnNames.clear();
        columnNames.add("#");
        columnNames.add("Supplier Name");
        columnNames.add("Supplies Name");
        columnNames.add("Price");
        columnNames.add("Quantity");
        Vector<Vector<String>> data = new Vector<>();

        try {
            //SELECT From ProductType
            ConnectionCreator connectionCreator = new ConnectionCreator();
            Connection connection = connectionCreator.connect();

            Statement getSupplierListStatement = connection.createStatement();
            String query = " SELECT \n"
                    + "	Suppliers.Name AS SupplierName,\n"
                    + "  Supplies.Name AS SupplyName,\n"
                    + "  Supplies.Price, Supplies.Quantity\n"
                    + "FROM \n"
                    + "	Supplies, Suppliers\n"
                    + "WHERE Supplies.SupplierID = Suppliers.ID";
            ResultSet rs = getSupplierListStatement.executeQuery(query);

            Integer i = 0;

            data.clear();
            supplierNumbers.clear();
            supplierNames.clear();
            suppliesPrice.clear();
            suppliesQuantity.clear();

            while (rs.next()) {
                i++;
                supplierNumbers.add(i.toString());
                supplierNames.add(rs.getString("SupplierName"));
                suppliesNames.add(rs.getString("SupplyName"));
                suppliesPrice.add("€ " + rs.getString("Price"));
                suppliesQuantity.add(rs.getString("Quantity"));
            }
            getSupplierListStatement.close();

        } catch (SQLException ex) {
            Logger.getLogger(AddProductsForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < supplierNumbers.size(); i++) {
            Vector<String> row = new Vector<>();
            row.add(supplierNumbers.get(i));
            row.add(supplierNames.get(i));
            row.add(suppliesNames.get(i));
            row.add(suppliesPrice.get(i));
            row.add(suppliesQuantity.get(i));
            data.add(row);
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        suppliersTable.setModel(model);

        JTableUtilities.setJTableColumnsWidth(suppliersTable, suppliersTable.getWidth(), 10, 30, 27, 15, 18);
    }//GEN-LAST:event_viewSuppliersButtonActionPerformed

    private void supplierTelephoneTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierTelephoneTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierTelephoneTextFieldActionPerformed

    private void supplierAddressTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierAddressTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierAddressTextFieldActionPerformed

    private void countryComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countryComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_countryComboBoxActionPerformed

    private void suppliesNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppliesNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_suppliesNameTextFieldActionPerformed

    private void priceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_priceTextFieldActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.setVisible(false);
        setUpForm = new SetUpForm();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void addSuppliersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSuppliersButtonActionPerformed
        ConnectionCreator connectionCreator = new ConnectionCreator();
        Connection connection = connectionCreator.connect();

        String queryInsertSuppliers = " insert into Suppliers (Name,Telephone,City,Address,CountryID)"
                + "values ('" + supplierNameTextField.getText() + "','" + supplierTelephoneTextField.getText() + "','" + supplierCityTextField.getText() + "', '" + supplierAddressTextField.getText() + "',0)";

        try {
            //Create insert preparedstatement for administrator
            PreparedStatement prepareSupplierStatement = connection.prepareStatement(queryInsertSuppliers);
            prepareSupplierStatement.execute();

            showMessageDialog(null, "Supplier Added -->" + supplierNameTextField.getText());

        } catch (SQLException ex) {
            Logger.getLogger(SuppliersForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        setVisible(true);
        countryComboBox.setSelectedIndex(0);
        supplierNameTextField.setText("");
        supplierAddressTextField.setText("");
        supplierCityTextField.setText("");
        supplierTelephoneTextField.setText("");

        updateSuppliersComboBox();
    }//GEN-LAST:event_addSuppliersButtonActionPerformed

    private void supplierCityTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierCityTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierCityTextFieldActionPerformed

    private void addSuppliesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSuppliesButtonActionPerformed
        ConnectionCreator connectionCreator = new ConnectionCreator();
        Connection connection = connectionCreator.connect();

        String queryInsertSupplies = " insert into Supplies (Name,SupplierID,Quantity,Price)"
                + " values ('" + suppliesNameTextField.getText() + "'," + (supplierIDs.get(supplierComboBox.getSelectedIndex())) + ", " + quantitySpinner.getValue() + "," + priceTextField.getText() + ")";

        System.out.println(queryInsertSupplies);

        try {
            //Create insert preparedstatement for administrator
            PreparedStatement prepareSuppliesStatement = connection.prepareStatement(queryInsertSupplies);
            prepareSuppliesStatement.execute();

            showMessageDialog(null, "Supplies Added -->" + suppliesNameTextField.getText());

        } catch (SQLException ex) {
            Logger.getLogger(SuppliersForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        setVisible(true);
        suppliesNameTextField.setText("");
        quantitySpinner.setValue(0);
        priceTextField.setText("");
    }//GEN-LAST:event_addSuppliesButtonActionPerformed

    private void supplierComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierComboBoxActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nextButtonActionPerformed

    public void updateSuppliersComboBox() {
        try {
            ConnectionCreator connectionCreator = new ConnectionCreator();
            Connection connection = connectionCreator.connect();

            Statement getSupplierStatement = connection.createStatement();
            String qr2 = " Select ID,Name From Suppliers";
            ResultSet rs2 = getSupplierStatement.executeQuery(qr2);

            supplierComboBox.removeAllItems();
            supplierIDs.clear();
            while (rs2.next()) {
                supplierIDs.add(rs2.getString("ID"));
                supplierComboBox.addItem(rs2.getString("Name"));
            }
            getSupplierStatement.close();

        } catch (SQLException ex) {
            Logger.getLogger(SuppliersForm.class.getName()).log(Level.SEVERE, null, ex);
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
            java.util.logging.Logger.getLogger(SuppliersForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SuppliersForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SuppliersForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SuppliersForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SuppliersForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addSuppliersButton;
    private javax.swing.JButton addSuppliesButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox<String> countryComboBox;
    private javax.swing.JButton nextButton;
    private javax.swing.JLabel priceLabel;
    private javax.swing.JTextField priceTextField;
    private javax.swing.JLabel quantityLabel;
    private javax.swing.JSpinner quantitySpinner;
    private javax.swing.JLabel supplierAddressLabel;
    private javax.swing.JTextField supplierAddressTextField;
    private javax.swing.JLabel supplierCityLabel;
    private javax.swing.JTextField supplierCityTextField;
    private javax.swing.JComboBox<String> supplierComboBox;
    private javax.swing.JLabel supplierCountryLabel;
    private javax.swing.JLabel supplierLabel;
    private javax.swing.JLabel supplierNameLabel;
    private javax.swing.JTextField supplierNameTextField;
    private javax.swing.JPanel supplierPanel;
    private javax.swing.JLabel supplierTelephoneLabel;
    private javax.swing.JTextField supplierTelephoneTextField;
    private javax.swing.JScrollPane suppliersScrollPane;
    private javax.swing.JTable suppliersTable;
    private javax.swing.JLabel suppliesNameLabel;
    private javax.swing.JTextField suppliesNameTextField;
    private javax.swing.JPanel suppliesPanel;
    private javax.swing.JPanel tableSuppliersPanel;
    private javax.swing.JButton viewSuppliersButton;
    // End of variables declaration//GEN-END:variables
}
