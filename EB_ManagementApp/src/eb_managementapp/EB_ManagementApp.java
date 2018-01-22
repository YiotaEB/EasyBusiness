package eb_managementapp;

import eb_managementapp.UI.Forms.AddProductsForm;
import eb_managementapp.UI.Forms.SuppliersForm;
import eb_managementapp.UI.Forms.AddUsersForm;
import eb_managementapp.UI.Forms.AdminForm;
import eb_managementapp.UI.Forms.LoginForm;
import eb_managementapp.UI.MainForm;
import eb_managementapp.UI.Forms.CompanyDetailsForm;
import eb_managementapp.UI.Forms.SetUpForm;
import eb_managementapp.UI.Forms.CustomersForm;
import eb_managementapp.UI.Forms.AddSizeForm;
import eb_managementapp.UI.Forms.AddProductTypeForm;
import java.sql.SQLException;

public class EB_ManagementApp {

    public static LoginForm loginForm ;
    public static MainForm mainForm ;
    public static CompanyDetailsForm companyDetailsForm;
    public static AdminForm adminForm;
    public static AddUsersForm addUsersForm ;
    public static AddProductsForm addProductsForm;
    public static SuppliersForm addSupplierForm;
    public static SetUpForm setUpForm;
    public static AddProductTypeForm addProductTypeForm;
    public static AddSizeForm addSizeForm;
    public static CustomersForm customersForm;
    
    
    
    
    //The program starts here
    public static void main(String[] args) throws SQLException {
        
        
        
        
        //Making the setup form visible 
        adminForm = new AdminForm();
        
        
        //Making the login form visible 
        //loginForm = new LoginForm();
        
        
        
       
      
        
        
        /******************************************************************************************/
        
    }
    
}
