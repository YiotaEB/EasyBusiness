/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eb_managementapp.UI;

import Utilities.HTTPConnection;

/**
 *
 * @author panay
 */
public class NetworkTest {
    
    public static void main(String [] args)
    {
        System.out.println(HTTPConnection.executePost(HTTPConnection.API_URL, "Countries", "GetMultiple", "Limit=2"));
    }
    
}
