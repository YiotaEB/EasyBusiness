package com.easybusiness.eb_androidapp.AsyncTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.easybusiness.eb_androidapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AsyncTasks {

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

    public static AlertDialog createConnectionErrorDialog(final Activity activity) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(R.string.connection_error);
        alertDialogBuilder.setMessage(R.string.connection_error_message);
        alertDialogBuilder.setIcon(R.drawable.ic_error_black_24dp);
        alertDialogBuilder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.abort, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.finish();
            }
        });
        return alertDialogBuilder.create();
    }

    public static AlertDialog createGeneralErrorDialog(final Activity activity, String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setIcon(R.drawable.ic_error_black_24dp);
        alertDialogBuilder.setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.finish();
            }
        });
        return alertDialogBuilder.create();
    }

}
