/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;

public class HTTPConnection {

    public static final String RESPONSE_OK = "OK";
    public static final String RESPONSE_ERROR = "Error";

    public static final String API_URL = "http://www.panickapps.com/eb/API";

    public static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static String encodeForAPI(final String apiBaseURL, final String entityName, final String endpointName) {
        return apiBaseURL + "/" + entityName + "/" + endpointName + "/";
    }

    public static String executePost(String targetURL, String entityName, String endpointName, String urlParameters) {
        HttpURLConnection urlConnection = null;
        String responseData = null;

        try {
            //Create connection
            URL url = new URL(encodeForAPI(API_URL, entityName, endpointName));
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            byte[] outputBytes = urlParameters.getBytes("UTF-8");
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            OutputStream os = urlConnection.getOutputStream();
            os.write(outputBytes);
            os.close();
            int statusCode = urlConnection.getResponseCode();

            //OK
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                responseData = convertStreamToString(inputStream);
                urlConnection.disconnect();
                return responseData;
            } else {
                showMessageDialog(null, "HTTP Connection Error on URL: " + encodeForAPI(API_URL, entityName, endpointName) + ". Please make sure you have an internet connection.", "Connection Error", JOptionPane.PLAIN_MESSAGE);
                throw new RuntimeException("HTTP Connection Error on URL: " + encodeForAPI(API_URL, entityName, endpointName));
            }

            

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        
        return null;

    }
}
