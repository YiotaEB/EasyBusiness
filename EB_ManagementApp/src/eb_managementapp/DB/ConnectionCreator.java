/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eb_managementapp.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author panay
 */
public class ConnectionCreator {

    //Conection Information
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String DB_LOCATION = "jdbc:mysql://160.153.16.64:3306/panayiota_easybusiness";
    public static final String DB_NAME = "dbu_easybusiness";
    public static final String DB_PASSWORD = "penny15";

    
    //Creates a connection to the DB and returns a Connection object
    public Connection connect() {
        Connection conn = null;

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DB_LOCATION, DB_NAME, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(System.err);
        }
        return conn;
    }

    
    
    /*public static void main(String[] args) {

        //window closing event 
        /*try {
          if (conn != null)
              conn.close();
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        System.out.println("Database connection established");
        // create the java statement
        Statement st = conn.createStatement();

        // the mysql insert statement
        String query = " Select Username From Users";

        // execute the query, and get a java resultset
        ResultSet rs = st.executeQuery(query);

        // iterate through the java resultset
        while (rs.next()) {
            String userName = rs.getString("Username");

            // print the results
            System.out.println(" User name: " + userName);
        }
        st.close();

    }*/
}
