//package com.easybusiness.eb_androidapp.AsyncTask;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.os.AsyncTask;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.easybusiness.eb_androidapp.Entities.Countries;
//import com.easybusiness.eb_androidapp.Entities.Customers;
//import com.easybusiness.eb_androidapp.R;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.BufferedInputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//
///*
//
//Sample query builder:
//
//Uri.Builder builder = new Uri.Builder()
//                .appendQueryParameter("username", edit_username.getText().toString())
//                .appendQueryParameter("password", edit_password.getText().toString());
//
//            String query = builder.build().getEncodedQuery();
// */
//
//public class AddEmployee_GetCountriesAsyncTask extends AsyncTask<Void,Void,Void> {
//
//    private String query;
//    private String responseData;
//    private Activity activity;
//    private View view;
//    private ArrayList<Countries> countries;
//    private ArrayAdapter<Countries> adapter;
//
//    public AddEmployee_GetCountriesAsyncTask(String query, Activity activity, View view) {
//        this.query = query;
//        this.activity = activity;
//        this.view = view;
//        countries = new ArrayList<>();
//    }
//
//    @Override
//    protected Void doInBackground(Void... params) {
//
//        if (query == null) query = "";
//
//        try {
//            URL url = new URL(AsyncTasks.encodeForAPI(activity.getString(R.string.baseURL), "Countries", "GetMultiple"));
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setDoOutput(true);
//            byte[] outputBytes = query.getBytes("UTF-8");
//            urlConnection.setRequestMethod("POST");
//            urlConnection.connect();
//            OutputStream os = urlConnection.getOutputStream();
//            os.write(outputBytes);
//            os.close();
//            int statusCode = urlConnection.getResponseCode();
//            urlConnection.disconnect();
//
//            //OK
//            if (statusCode == HttpURLConnection.HTTP_OK) {
//                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
//                responseData = AsyncTasks.convertStreamToString(inputStream);
//
//                System.out.println(responseData);
//                JSONObject outterObject = new JSONObject(responseData);
//                final String status = outterObject.getString("Status");
//                final String title = outterObject.getString("Title");
//                final String message = outterObject.getString("Message");
//
//                if (status.equals(AsyncTasks.RESPONSE_OK)) {
//                    JSONArray dataArray = outterObject.getJSONArray("Data");
//                    for (int i = 0; i < dataArray.length(); i++) {
//                        JSONObject jsonObject = dataArray.getJSONObject(i);
//                        String name = jsonObject.getString("Name");
//                        int id = jsonObject.getInt("ID");
//                        Countries c = new Countries(id, name);
//                        countries.add(c);
//                    }
//
//                    adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, countries);
//
//                }
//
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Spinner countriesSpinner = view.findViewById(R.id.country_spinner);
//                        countriesSpinner.setAdapter(adapter);
//                    }
//                });
//
//            }
//            //CONNECTION ERROR
//            else {
//                //TODO Show dialog?
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//}