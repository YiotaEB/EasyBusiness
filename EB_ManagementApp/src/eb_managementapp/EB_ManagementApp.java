package eb_managementapp;

import Utilities.HTTPConnection;
import eb_managementapp.UI.Forms.AddProductsForm;
import eb_managementapp.UI.Forms.AddUsersForm;
import eb_managementapp.UI.Forms.AdminForm;
import eb_managementapp.UI.Forms.LoginForm;
import eb_managementapp.UI.Forms.MainForm;
import eb_managementapp.UI.Forms.CompanyDetailsForm;
import eb_managementapp.UI.Forms.SetUpForm;
import eb_managementapp.UI.Forms.CustomersForm;
import eb_managementapp.UI.Forms.AddSizeForm;
import eb_managementapp.UI.Forms.AddProductTypeForm;
import eb_managementapp.UI.Forms.AddSuppliersForm;
import eb_managementapp.UI.Forms.AddSuppliesForm;
import static eb_managementapp.UI.Forms.LoginForm.SESSION_FILENAME;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import org.json.JSONObject;

public class EB_ManagementApp {

    public static LoginForm loginForm;
    public static MainForm mainForm;
    public static CompanyDetailsForm companyDetailsForm;
    public static AdminForm adminForm;
    public static AddUsersForm addUsersForm;
    public static AddProductsForm addProductsForm;
    public static SetUpForm setUpForm;
    public static AddProductTypeForm addProductTypeForm;
    public static AddSizeForm addSizeForm;
    public static AddSuppliersForm addSuppliersForm;
    public static AddSuppliesForm addSuppliesForm;
    public static CustomersForm customersForm;

    //The program starts here
    public static void main(String[] args) throws SQLException {

        //Making the setup form visible 
        //adminForm = new AdminForm(new JFrame());
        //Making the login form visible 
        attemptExistingLogin();

    }

    public static String readSetting(String filename) {
        String currentLine;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while ((currentLine = br.readLine()) != null) {
                return currentLine;
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    private static void attemptExistingLogin() {
        String username = readSetting(LoginForm.SESSION_USERNAME);
        String password = readSetting(LoginForm.SESSION_PASSWORD);
        if (username == null || password == null) {
            loginForm = new LoginForm();
            return;
        }
        String response = HTTPConnection.executePost(HTTPConnection.API_URL + "/LoginExisting/", "Username=" + username + "&Password=" + Utilities.Hash.MD5(password));
        System.out.println(response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_ERROR)) {
                loginForm = new LoginForm();
            } else if (status.equals(HTTPConnection.RESPONSE_OK)) {
                String adminSetting = readSetting(LoginForm.ADMIN_EXISTING);
                String setupCompleted = readSetting(LoginForm.SETUP_COMPLETED);
                if (adminSetting == null || setupCompleted == null) {
                    adminForm = new AdminForm(new JFrame());
                    return;
                }
                if (adminSetting.equals("true")) {
                    if (setupCompleted.equals("true")) {
                        mainForm = new MainForm();
                    } else {
                    setUpForm = new SetUpForm(new JFrame());
                    }
                } else {
                    adminForm = new AdminForm(new JFrame());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
