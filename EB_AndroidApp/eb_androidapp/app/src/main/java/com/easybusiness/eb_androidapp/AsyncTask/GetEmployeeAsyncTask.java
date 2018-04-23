//package com.easybusiness.eb_androidapp.AsyncTask;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.os.AsyncTask;
//import android.view.View;
//import android.widget.ListView;
//
//import com.easybusiness.eb_androidapp.Entities.Users;
//import com.easybusiness.eb_androidapp.R;
//import com.easybusiness.eb_androidapp.UI.Adapters.EmployeeAdapter;
//import com.easybusiness.eb_androidapp.UI.Fragments.ViewEmployeeFragment;
//import com.easybusiness.eb_androidapp.UI.Fragments.ViewEmployeesFragment;
//import com.easybusiness.eb_androidapp.UI.MainActivity;
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
//public class GetEmployeeAsyncTask extends AsyncTask<Void, Void, Void> {
//
//    private String query;
//    private String responseData;
//    private Activity activity;
//    private View view;
//    Users user = null;
//
//    public GetEmployeeAsyncTask(String query, Activity activity, View view) {
//        this.query = query;
//        this.activity = activity;
//        this.view = view;
//    }
//
//    @Override
//    protected Void doInBackground(Void... params) {
//
//        if (query == null) query = "";
//
//        try {
//            URL url = new URL(AsyncTasks.encodeForAPI(activity.getString(R.string.baseURL), "Users", "GetByID"));
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
//                JSONObject outterObject = new JSONObject(responseData);
//                System.out.println(responseData);
//
//                final String status = outterObject.getString("Status");
//                final String title = outterObject.getString("Title");
//                final String message = outterObject.getString("Message");
//
//                if (status.equals(AsyncTasks.RESPONSE_OK)) {
//                    JSONObject jsonObject = outterObject.getJSONObject("Data");
//                    String firstName = jsonObject.getString("Firstname");
//                    String lastName = jsonObject.getString("Lastname");
//                    String telephone = jsonObject.getString("Telephone");
//                    int userlevelID = jsonObject.getInt("UserLevelID");
//                    int id = jsonObject.getInt("UserID");
//                    int dateHired = jsonObject.getInt("DateHired");
//                    String city = jsonObject.getString("City");
//                    String address = jsonObject.getString("Address");
//                    int countryID = jsonObject.getInt("CountryID");
//                    String username = jsonObject.getString("Username");
//
//
//                    ViewEmployeeFragment.user = new Users(id, username, "", userlevelID, firstName, lastName, dateHired, city, address, telephone, countryID);
//
//                    ViewEmployeeFragment.usernameTextview.setText(user.getUsername());
//                    ViewEmployeeFragment.positionTextview.setText(positionName);
//                    ViewEmployeeFragment.cityTextview.setText(user.getCity());
//                    ViewEmployeeFragment.addressTextview.setText(user.getAddress());
//                    ViewEmployeeFragment.countryTextview.setText(countryName);
//                    ViewEmployeeFragment.dateHiredTextview.setText(formattedDate);
//                    ViewEmployeeFragment.telephoneTextview.setText(user.getTelephone());
//
//
//                } else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            final AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(activity, title, message);
//                            alertDialog.show();
//                        }
//                    });
//                }
//
//                //CONNECTION ERROR
//                else {
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            final AlertDialog alertDialog = AsyncTasks.createConnectionErrorDialog(activity);
//                            alertDialog.show();
//                        }
//                    });
//                }
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//}