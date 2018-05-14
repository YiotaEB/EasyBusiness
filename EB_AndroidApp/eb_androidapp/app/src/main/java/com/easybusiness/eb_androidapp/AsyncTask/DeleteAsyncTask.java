package com.easybusiness.eb_androidapp.AsyncTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.Fragments.ViewEmployeesFragment;
import com.easybusiness.eb_androidapp.UI.LoginActivity;
import com.easybusiness.eb_androidapp.UI.MainActivity;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/*

Sample query builder:

Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("username", edit_username.getText().toString())
                .appendQueryParameter("password", edit_password.getText().toString());

            String query = builder.build().getEncodedQuery();
 */

public class DeleteAsyncTask extends AsyncTask<Void,Void,Void> {

    private String entityName;
    private int itemID = -1;
    private String itemName;
    private static final String entityEndpoint = "Delete";
    private String query;
    private String responseData;
    private Activity activity;
    private View view;
    private Fragment sender;

    public DeleteAsyncTask(String entityName, int itemID, final String itemName, Activity activity, View view, DialogInterface startingDialog) {

        if (startingDialog != null) {
            startingDialog.dismiss();
        }

        this.entityName = entityName;
        this.activity = activity;
        this.view = view;
        this.itemID = itemID;
        this.itemName = itemName;
        this.sender = sender;
        final String sessionID = PreferenceManager.getDefaultSharedPreferences(activity).getString(MainActivity.PREFERENCE_SESSIONID, null);
        if (sessionID == null) {
            activity.startActivity(new Intent(activity, LoginActivity.class));
            activity.finish();
        }
        if (entityName == null || entityName.isEmpty()) {
            throw new RuntimeException("Entity name to delete cannot be blank.");
        }
        if (itemID < 0) {
            throw new RuntimeException("Item ID to delete cannot be < 0.");
        }

        String itemIDName = "ID";
        if (entityName.equals("Users")) itemIDName = "UserID";

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter(itemIDName, String.valueOf(itemID))
                .appendQueryParameter("SessionID", sessionID);

        query = builder.build().getEncodedQuery();
    }

    @Override
    protected Void doInBackground(Void... params) {

        if (query == null) query = "";

        try {
            URL url = new URL(AsyncTasks.encodeForAPI(activity.getString(R.string.baseURL), entityName, entityEndpoint));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            byte[] outputBytes = query.getBytes("UTF-8");
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            OutputStream os = urlConnection.getOutputStream();
            os.write(outputBytes);
            os.close();
            int statusCode = urlConnection.getResponseCode();
            urlConnection.disconnect();

            //OK
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                responseData = AsyncTasks.convertStreamToString(inputStream);

                JSONObject outterObject = new JSONObject(responseData);
                final String status = outterObject.getString("Status");
                final String title = outterObject.getString("Title");
                final String message = outterObject.getString("Message");

                if (status.equals(AsyncTasks.RESPONSE_OK)) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, itemName + " " + activity.getString(R.string.has_been_delete), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(activity, title, message);
                            alertDialog.show();
                        }
                    });
                }


            }
            //CONNECTION ERROR
            else {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final AlertDialog alertDialog = AsyncTasks.createConnectionErrorDialog(activity);
                        alertDialog.show();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}