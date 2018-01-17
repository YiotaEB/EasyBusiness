package eb_managementapp.UI.Forms;

import eb_managementapp.UI.Forms.SetUpForm;
import static eb_managementapp.EB_ManagementApp.setUpForm;

public class AddUsersForm extends javax.swing.JFrame {
 
    final String TITLE = "Easy Business - Add Employees";

    /**
     * Creates new form AddUsersForm
     */
    public AddUsersForm() {
        initComponents();
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

        buttonGroup = new javax.swing.ButtonGroup();
        EmployeeDetailsPanel = new javax.swing.JPanel();
        EmployeeNameLabel = new javax.swing.JLabel();
        EmployeeLastNameLabel = new javax.swing.JLabel();
        NameTextField = new javax.swing.JTextField();
        LastNameTextField = new javax.swing.JTextField();
        EmailTextField = new javax.swing.JTextField();
        EmployeeEmailLabel = new javax.swing.JLabel();
        CompanyNameLabel34 = new javax.swing.JLabel();
        CompanyNameTextField36 = new javax.swing.JTextField();
        CompanyNameLabel29 = new javax.swing.JLabel();
        CompanyNameLabel31 = new javax.swing.JLabel();
        CompanyNameTextField32 = new javax.swing.JTextField();
        CompanyNameLabel30 = new javax.swing.JLabel();
        CompanyNameTextField33 = new javax.swing.JTextField();
        DateOfBirthPicker1 = new org.jdesktop.swingx.JXDatePicker();
        UsernameLabel = new javax.swing.JLabel();
        EmploymentDetailsPanel = new javax.swing.JPanel();
        PositionLabel = new javax.swing.JLabel();
        UserTypeComboBox = new javax.swing.JComboBox<>();
        FullTimeRadioButton = new javax.swing.JRadioButton();
        FullTimeRadioButton1 = new javax.swing.JRadioButton();
        HireDateLabel = new javax.swing.JLabel();
        DateOfHirePicker = new org.jdesktop.swingx.JXDatePicker();
        ButtonPanel = new javax.swing.JPanel();
        AddNewEmployeesButton = new javax.swing.JButton();
        ViewEmployeesButton = new javax.swing.JButton();
        CancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Easy Business - Add Employees");

        EmployeeDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Employee Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14), new java.awt.Color(102, 102, 102))); // NOI18N

        EmployeeNameLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        EmployeeNameLabel.setText("Name:");

        EmployeeLastNameLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        EmployeeLastNameLabel.setText("Last Name:");

        NameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NameTextFieldActionPerformed(evt);
            }
        });

        LastNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LastNameTextFieldActionPerformed(evt);
            }
        });

        EmailTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmailTextFieldActionPerformed(evt);
            }
        });

        EmployeeEmailLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        EmployeeEmailLabel.setText("E-mail:");

        CompanyNameLabel34.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        CompanyNameLabel34.setText("Telephone:");

        CompanyNameTextField36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CompanyNameTextField36ActionPerformed(evt);
            }
        });

        CompanyNameLabel29.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        CompanyNameLabel29.setText("Country:");

        CompanyNameLabel31.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        CompanyNameLabel31.setText("City:");

        CompanyNameTextField32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CompanyNameTextField32ActionPerformed(evt);
            }
        });

        CompanyNameLabel30.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        CompanyNameLabel30.setText("Address:");

        CompanyNameTextField33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CompanyNameTextField33ActionPerformed(evt);
            }
        });

        DateOfBirthPicker1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DateOfBirthPicker1ActionPerformed(evt);
            }
        });

        UsernameLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        UsernameLabel.setText("UserName: ");

        javax.swing.GroupLayout EmployeeDetailsPanelLayout = new javax.swing.GroupLayout(EmployeeDetailsPanel);
        EmployeeDetailsPanel.setLayout(EmployeeDetailsPanelLayout);
        EmployeeDetailsPanelLayout.setHorizontalGroup(
            EmployeeDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EmployeeDetailsPanelLayout.createSequentialGroup()
                .addGroup(EmployeeDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EmployeeDetailsPanelLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(UsernameLabel))
                    .addGroup(EmployeeDetailsPanelLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(EmployeeDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(EmployeeNameLabel)
                            .addComponent(EmployeeLastNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(EmployeeDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(LastNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                            .addComponent(NameTextField))))
                .addGap(18, 18, 18)
                .addGroup(EmployeeDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(EmployeeDetailsPanelLayout.createSequentialGroup()
                        .addComponent(EmployeeEmailLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(EmailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(EmployeeDetailsPanelLayout.createSequentialGroup()
                        .addComponent(CompanyNameLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(CompanyNameTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(EmployeeDetailsPanelLayout.createSequentialGroup()
                        .addGroup(EmployeeDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(CompanyNameLabel31)
                            .addComponent(CompanyNameLabel29)
                            .addComponent(CompanyNameLabel30))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(EmployeeDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(CompanyNameTextField32)
                            .addComponent(CompanyNameTextField33)
                            .addComponent(DateOfBirthPicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        EmployeeDetailsPanelLayout.setVerticalGroup(
            EmployeeDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmployeeDetailsPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(EmployeeDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EmployeeDetailsPanelLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(EmployeeDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(EmployeeEmailLabel)
                            .addComponent(EmailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(EmployeeDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CompanyNameLabel34)
                            .addComponent(CompanyNameTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(EmployeeDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CompanyNameLabel29)
                            .addComponent(DateOfBirthPicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(EmployeeDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CompanyNameLabel31)
                            .addComponent(CompanyNameTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(EmployeeDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CompanyNameLabel30)
                            .addComponent(CompanyNameTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(EmployeeDetailsPanelLayout.createSequentialGroup()
                        .addGroup(EmployeeDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(NameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(EmployeeNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(EmployeeDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LastNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(EmployeeLastNameLabel))
                        .addGap(20, 20, 20)
                        .addComponent(UsernameLabel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        EmploymentDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Employment  Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14), new java.awt.Color(102, 102, 102))); // NOI18N

        PositionLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        PositionLabel.setText("Position:");

        UserTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        UserTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserTypeComboBoxActionPerformed(evt);
            }
        });

        buttonGroup.add(FullTimeRadioButton);
        FullTimeRadioButton.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        FullTimeRadioButton.setText("Full Time");

        buttonGroup.add(FullTimeRadioButton1);
        FullTimeRadioButton1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        FullTimeRadioButton1.setText("Part Time");

        HireDateLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        HireDateLabel.setText("Hire Date");

        DateOfHirePicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DateOfHirePickerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout EmploymentDetailsPanelLayout = new javax.swing.GroupLayout(EmploymentDetailsPanel);
        EmploymentDetailsPanel.setLayout(EmploymentDetailsPanelLayout);
        EmploymentDetailsPanelLayout.setHorizontalGroup(
            EmploymentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EmploymentDetailsPanelLayout.createSequentialGroup()
                .addGroup(EmploymentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(EmploymentDetailsPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(FullTimeRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(FullTimeRadioButton1))
                    .addGroup(EmploymentDetailsPanelLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(PositionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(UserTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addComponent(HireDateLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(DateOfHirePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        EmploymentDetailsPanelLayout.setVerticalGroup(
            EmploymentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EmploymentDetailsPanelLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(EmploymentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EmploymentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(HireDateLabel)
                        .addComponent(DateOfHirePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(EmploymentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(UserTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(PositionLabel)))
                .addGap(15, 15, 15)
                .addGroup(EmploymentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FullTimeRadioButton)
                    .addComponent(FullTimeRadioButton1))
                .addContainerGap())
        );

        AddNewEmployeesButton.setText("Add New Emplyees");

        ViewEmployeesButton.setText("View Employees");
        ViewEmployeesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewEmployeesButtonActionPerformed(evt);
            }
        });

        CancelButton.setText("Cancel");
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ButtonPanelLayout = new javax.swing.GroupLayout(ButtonPanel);
        ButtonPanel.setLayout(ButtonPanelLayout);
        ButtonPanelLayout.setHorizontalGroup(
            ButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ButtonPanelLayout.createSequentialGroup()
                .addContainerGap(229, Short.MAX_VALUE)
                .addComponent(CancelButton)
                .addGap(18, 18, 18)
                .addComponent(ViewEmployeesButton)
                .addGap(18, 18, 18)
                .addComponent(AddNewEmployeesButton)
                .addContainerGap())
        );
        ButtonPanelLayout.setVerticalGroup(
            ButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ButtonPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(ButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddNewEmployeesButton)
                    .addComponent(ViewEmployeesButton)
                    .addComponent(CancelButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ButtonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(EmploymentDetailsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(EmployeeDetailsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(EmployeeDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(EmploymentDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void NameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NameTextFieldActionPerformed

    private void LastNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LastNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LastNameTextFieldActionPerformed

    private void EmailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmailTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmailTextFieldActionPerformed

    private void CompanyNameTextField36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CompanyNameTextField36ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CompanyNameTextField36ActionPerformed

    private void CompanyNameTextField32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CompanyNameTextField32ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CompanyNameTextField32ActionPerformed

    private void CompanyNameTextField33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CompanyNameTextField33ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CompanyNameTextField33ActionPerformed

    private void UserTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserTypeComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UserTypeComboBoxActionPerformed

    private void DateOfHirePickerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DateOfHirePickerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DateOfHirePickerActionPerformed

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_CancelButtonActionPerformed

    private void ViewEmployeesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewEmployeesButtonActionPerformed
          
        this.setVisible(false);
           setUpForm=new SetUpForm();
           
    }//GEN-LAST:event_ViewEmployeesButtonActionPerformed

    private void DateOfBirthPicker1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DateOfBirthPicker1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DateOfBirthPicker1ActionPerformed

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
            java.util.logging.Logger.getLogger(AddUsersForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddUsersForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddUsersForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddUsersForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddUsersForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddNewEmployeesButton;
    private javax.swing.JPanel ButtonPanel;
    private javax.swing.JButton CancelButton;
    private javax.swing.JLabel CompanyNameLabel29;
    private javax.swing.JLabel CompanyNameLabel30;
    private javax.swing.JLabel CompanyNameLabel31;
    private javax.swing.JLabel CompanyNameLabel34;
    private javax.swing.JTextField CompanyNameTextField32;
    private javax.swing.JTextField CompanyNameTextField33;
    private javax.swing.JTextField CompanyNameTextField36;
    private org.jdesktop.swingx.JXDatePicker DateOfBirthPicker1;
    private org.jdesktop.swingx.JXDatePicker DateOfHirePicker;
    private javax.swing.JTextField EmailTextField;
    private javax.swing.JPanel EmployeeDetailsPanel;
    private javax.swing.JLabel EmployeeEmailLabel;
    private javax.swing.JLabel EmployeeLastNameLabel;
    private javax.swing.JLabel EmployeeNameLabel;
    private javax.swing.JPanel EmploymentDetailsPanel;
    private javax.swing.JRadioButton FullTimeRadioButton;
    private javax.swing.JRadioButton FullTimeRadioButton1;
    private javax.swing.JLabel HireDateLabel;
    private javax.swing.JTextField LastNameTextField;
    private javax.swing.JTextField NameTextField;
    private javax.swing.JLabel PositionLabel;
    private javax.swing.JComboBox<String> UserTypeComboBox;
    private javax.swing.JLabel UsernameLabel;
    private javax.swing.JButton ViewEmployeesButton;
    private javax.swing.ButtonGroup buttonGroup;
    // End of variables declaration//GEN-END:variables
}
