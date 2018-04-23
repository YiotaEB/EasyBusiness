package com.easybusiness.eb_androidapp.AsyncTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.easybusiness.eb_androidapp.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostAsyncTask extends AsyncTask<String, Integer, String> {

    private final Activity activity;
    private final ProgressBar progressBar;
    private final String apiURL;
    private final String endpointName;
    private final String entityName;
    private final String queryParams;
    private final View normalView;

    public PostAsyncTask(final Activity activity, final ProgressBar progressBar, final View normalView,
                         final String apiURL, final String endpointName, final String entityName, final String queryParams) {
        this.activity = activity;
        this.progressBar = progressBar;
        this.queryParams = queryParams;
        this.apiURL = apiURL;
        this.endpointName = endpointName;
        this.entityName = entityName;
        this.normalView = normalView;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        normalView.setVisibility(View.GONE);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(activity.getString(R.string.baseURL) + "/" + entityName + "/" + endpointName + "/");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            byte[] outputBytes = queryParams.getBytes("UTF-8");
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
                return responseData;
            }
            //Connection Error
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
            final AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(activity, "Unexpected Error", e.getMessage());
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alertDialog.show();
                }
            });
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressBar.setVisibility(View.GONE);
        normalView.setVisibility(View.VISIBLE);
    }


}
