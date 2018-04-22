package com.easybusiness.eb_androidapp.AsyncTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ProgressBar;

import com.easybusiness.eb_androidapp.Model.AppMode;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.LoginActivity;
import com.easybusiness.eb_androidapp.UI.MainActivity;
import com.easybusiness.eb_androidapp.Utilities.Hash;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginAsyncTask extends AsyncTask<Void, Void, Void> {

    private final String username;
    private final String password;
    private final Activity activity;
    private final View view;

    public LoginAsyncTask(Activity activity, View view, String username, String password) {
        this.username = username;
        this.password = password;
        this.activity = activity;
        this.view = view;
    }

    @Override
    protected Void doInBackground(Void... params) {

        final ProgressBar progressBar = (ProgressBar) ((LoginActivity)activity).getProgressView();
        progressBar.setVisibility(View.VISIBLE);

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("Username", username)
                .appendQueryParameter("Password", password)
                ;
        String query = builder.build().getEncodedQuery();

        if (query == null) query = "";

        try {

            URL url = new URL(activity.getString(R.string.baseURL) + "/Login/");
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
                System.out.println(responseData);
                final String status = outterObject.getString("Status");
                final String title = outterObject.getString("Title");
                final String message = outterObject.getString("Message");

                if (status.equals(AsyncTasks.RESPONSE_OK)) {

                    final String firstName = outterObject.getString("Firstname");
                    final String lastName = outterObject.getString("Lastname");
                    String sessionID = outterObject.getString("SessionID");
                    int userLevelID = outterObject.getInt("UserLevelID");

                    PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(MainActivity.PREFERENCE_SESSIONID, sessionID).apply();
                    PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(MainActivity.PREFERENCE_FIRSTNAME, firstName).apply();
                    PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(MainActivity.PREFERENCE_LASTNAME, lastName).apply();
                    PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(MainActivity.PREFERENCE_USERNAME, username).apply();
                    PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(MainActivity.PREFERENCE_PASSWORD_HASH, Hash.MD5(password)).apply();
                    PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(MainActivity.PREFERENCE_USERLEVELID, String.valueOf(userLevelID)).apply();


                    //Admins:
                    if (userLevelID <= 2) {
                        Intent i = new Intent(activity, MainActivity.class);
                        i.putExtra(MainActivity.APP_MODE_STRING, AppMode.MODE_ADMIN);
                        activity.startActivity(i);
                        activity.finish();
                    }
                    //Users:
                    else {
                        Intent i = new Intent(activity, MainActivity.class);
                        i.putExtra(MainActivity.APP_MODE_STRING, AppMode.MODE_USER);
                        activity.startActivity(i);
                        activity.finish();
                    }
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
                            ((LoginActivity) activity).showProgress(false);
                            progressBar.setVisibility(View.VISIBLE);
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
                }
                //Unknown error
                else {
                    final AlertDialog alertDialog = AsyncTasks.createConnectionErrorDialog(activity);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.show();
                        }
                    });
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

}
