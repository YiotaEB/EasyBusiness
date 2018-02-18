package com.easybusiness.eb_androidapp.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AsyncTaskTools {

    public static final String RESPONSE_OK = "OK";
    public static final String RESPONSE_ERROR = "Error";

    public static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null)
                sb.append((line + "\n"));
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

}
