package com.easybusiness.eb_androidapp.AsyncTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;

import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.LoginActivity;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LogoutAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private final String sessionId;
    private final Activity activity;
    private final View view;

    public LogoutAsyncTask(Activity activity, View view, String sessionId) {
        this.sessionId = sessionId;
        this.activity = activity;
        this.view = view;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        if (sessionId.trim().isEmpty()) {
            return false;
        }

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("SessionID", sessionId)
                ;
        String query = builder.build().getEncodedQuery();

        if (query == null) query = "";

        try {

            URL url = new URL(activity.getString(R.string.baseURL) + "/Logout/");
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
                String responseData = AsyncTasks.convertStreamToString(inputStream);

                JSONObject outterObject = new JSONObject(responseData);
                final String status = outterObject.getString("Status");
                final String title = outterObject.getString("Title");
                final String message = outterObject.getString("Message");

                if (status.equals(AsyncTasks.RESPONSE_OK)) {
                    return true;
                }
                else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                    alertDialogBuilder.setTitle(title.substring(0, title.length()-1));
                    alertDialogBuilder.setMessage(message);
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
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alertDialogBuilder.create().show();
                        }
                    });
                    return false;
                }
                //Unknown error
                else {
                    final AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(activity, title, message);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.show();
                        }
                    });
                    return false;
                }
            }
            //Connection error
            else {
                final AlertDialog alertDialog = AsyncTasks.createConnectionErrorDialog(activity);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.show();
                    }
                });
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            if (aBoolean) {
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
                sharedPreferences.edit().clear().apply();
                Intent i = new Intent(activity, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(i);
                activity.finish();
            }
        }
    }
}
