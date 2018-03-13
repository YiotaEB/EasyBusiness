package com.easybusiness.eb_androidapp.AsyncTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.easybusiness.eb_androidapp.R;

import org.json.JSONArray;
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

public class GetUserLevelsAsyncTask extends AsyncTask<Void,Void,Void> {

    private String query;
    private String responseData;
    private Activity activity;
    private View view;

    public GetUserLevelsAsyncTask(String query, Activity activity, View view) {
        this.query = query;
        this.activity = activity;
        this.view = view;
    }

    @Override
    protected Void doInBackground(Void... params) {

        if (query == null) query = "";

        try {
            URL url = new URL(AsyncTasks.encodeForAPI(activity.getString(R.string.baseURL), "Countries", "GetMultiple"));
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

                //TODO: Change JSON Parsing
                JSONObject outterObject = new JSONObject(responseData);
                final String status = outterObject.getString("Status");
                final String title = outterObject.getString("Title");
                final String message = outterObject.getString("Message");

                if (status.equals(AsyncTasks.RESPONSE_OK)) {
                    JSONArray dataArray = outterObject.getJSONArray("Data");
                    responseData = "";
                    for (int i = 0; i < dataArray.length(); i++) {
                        responseData += (dataArray.getJSONObject(i).getString("Name") + "\r\n");
                    }

                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //TODO CHANGE:
                        AlertDialog.Builder b = new AlertDialog.Builder(activity);
                        b.setTitle(title);
                        b.setMessage(message);
                        b.create().show();
                    }
                });


            }
            //CONNECTION ERROR
            else {
                responseData = "Connection Error";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //TODO CHANGE:
                    TextView textView = view.findViewById(R.id.testView);
                    textView.setText(responseData);
                }
            });
        }
        return null;
    }


}