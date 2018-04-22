package com.easybusiness.eb_androidapp.AsyncTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.view.View;

import com.easybusiness.eb_androidapp.Entities.Countries;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.MainActivity;

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

public class GetCountriesAsyncTask extends AsyncTask<Void,Void,Void> {

    private String query;
    private String responseData;
    private Activity activity;
    private View view;

    public GetCountriesAsyncTask(String query, Activity activity, View view) {
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
                System.out.println(responseData);

                JSONObject outterObject = new JSONObject(responseData);
                final String status = outterObject.getString("Status");
                final String title = outterObject.getString("Title");
                final String message = outterObject.getString("Message");

                if (status.equals(AsyncTasks.RESPONSE_OK)) {
                    MainActivity mainActivity = (MainActivity) activity;
                    JSONArray dataArray = outterObject.getJSONArray("Data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject jsonObject = dataArray.getJSONObject(i);
                        int id = jsonObject.getInt("ID");
                        String countryName = jsonObject.getString("Name");
                        mainActivity.COUNTRY_DATA.add(new Countries(id, countryName));
                    }

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