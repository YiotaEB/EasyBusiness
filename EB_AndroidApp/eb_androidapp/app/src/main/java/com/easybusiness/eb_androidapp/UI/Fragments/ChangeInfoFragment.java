package com.easybusiness.eb_androidapp.UI.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.easybusiness.eb_androidapp.AsyncTask.AsyncTasks;
import com.easybusiness.eb_androidapp.AsyncTask.GetEmployeesAsyncTask;
import com.easybusiness.eb_androidapp.Entities.Countries;
import com.easybusiness.eb_androidapp.Entities.UserLevels;
import com.easybusiness.eb_androidapp.Entities.Users;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeInfoFragment extends Fragment {

    public static final String TAG = "ChangeInfoFragment";
    public static final String TITLE = "Change info";

    private ArrayList<Countries> countries;

    private String title = TITLE;
    private int userID;
    private int countryID;
    private Users user;

    private View v;
    private EditText firstnameEditText;
    private EditText lastnameEditText;
    private EditText addressEditText;
    private EditText telephoneEditText;
    private EditText cityEditText;
    private Spinner countrySpinner;
    private Button editEmployeesButton;
    private SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_change_info, container, false);

        editEmployeesButton = v.findViewById(R.id.save_info);
        firstnameEditText = v.findViewById(R.id.info_firstname_edittext);
        lastnameEditText = v.findViewById(R.id.info_lastname_edittext);
        addressEditText = v.findViewById(R.id.info_address_edittext);
        telephoneEditText = v.findViewById(R.id.info_telephone_edittext);
        cityEditText = v.findViewById(R.id.info_city_edittext);
        countrySpinner = v.findViewById(R.id.info_country_spinner);

        //Give values to the fields:
        Bundle bundle = getArguments();
        if (bundle != null) {
            userID = bundle.getInt(ViewEmployeeFragment.EMPLOYEE_KEY);
        }

        editEmployeesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput()) {
                    new EditEmployeeAsyncTask().execute();
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(title);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        new GetEmployeeAsyncTask().execute();
    }

    private boolean checkInput() {

        boolean flag = true;

        //Check first name:
        if (firstnameEditText.getText().toString().trim().isEmpty()) {
            firstnameEditText.setError("Please provide a valid answer.");
            flag = false;
        }
        else {
            firstnameEditText.setError(null);
        }

        //Check last name:
        if (lastnameEditText.getText().toString().trim().isEmpty()) {
            lastnameEditText.setError("Please provide a valid answer.");
            flag = false;
        }
        else {
            lastnameEditText.setError(null);
        }

        //Check address
        if (addressEditText.getText().toString().trim().isEmpty()) {
            addressEditText.setError("Please provide a valid answer.");
            flag = false;
        }
        else {
            addressEditText.setError(null);
        }

        //Check city
        if (cityEditText.getText().toString().trim().isEmpty()) {
            cityEditText.setError("Please provide a valid answer.");
            flag = false;
        }
        else {
            cityEditText.setError(null);
        }

        //Check Telephone
        if (telephoneEditText.getText().toString().trim().isEmpty()) {
            telephoneEditText.setError("Please provide a valid answer.");
            flag = false;
        }
        else {
            telephoneEditText.setError(null);
        }

        return flag;
    }

    private class GetCountriesAsyncTask extends AsyncTask<Void,Void,Void> {

        private String responseData;
        private ArrayAdapter<String> adapter;

        public GetCountriesAsyncTask() {
            countries = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String query;
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("Limit", "0")
                    .appendQueryParameter("SessionID", sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, ""));
            query = builder.build().getEncodedQuery();

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Countries", "GetMultiple"));
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
                        JSONArray dataArray = outterObject.getJSONArray("Data");
                        ArrayList<String> countryNames = new ArrayList<>();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            String name = jsonObject.getString("Name");
                            int id = jsonObject.getInt("ID");
                            Countries c = new Countries(id, name);
                            countries.add(c);
                            countryNames.add(c.getName());
                        }

                        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, countryNames);

                    }
                    //ERROR:
                    else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(getActivity(), title, message);
                                alertDialog.show();
                            }
                        });
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            countrySpinner.setAdapter(adapter);
                            for (int i = 0; i < countries.size(); i++) {
                                final int pos = i;
                                if (countries.get(i).getID() == countryID) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            countrySpinner.setSelection(pos);
                                        }
                                    });
                                    break;
                                }
                            }
                        }
                    });

                }
                //CONNECTION ERROR
                else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final AlertDialog alertDialog = AsyncTasks.createConnectionErrorDialog(getActivity());
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

    private class GetEmployeeAsyncTask extends AsyncTask<Void, Void, Void> {

        private String responseData;

        @Override
        protected Void doInBackground(Void... voids) {


            String query;
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("UserID", String.valueOf(userID))
                    .appendQueryParameter("SessionID", sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, ""));
            query = builder.build().getEncodedQuery();


            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Users", "GetByID"));
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
                        JSONObject jsonObject = outterObject.getJSONObject("Data");

                        final String firstname = jsonObject.getString("Firstname");
                        final String lastname = jsonObject.getString("Lastname");
                        final String username = jsonObject.getString("Username");
                        final long hired = jsonObject.getLong("DateHired");
                        final int userLevelID = jsonObject.getInt("UserLevelID");
                        final int id = jsonObject.getInt("UserID");
                        final String address = jsonObject.getString("Address");
                        final String city = jsonObject.getString("City");
                        final String telephone = jsonObject.getString("Telephone");
                        countryID = jsonObject.getInt("CountryID");
                        final String password = jsonObject.getString("Password");

                        user = new Users(id, username, password, userLevelID, firstname, lastname, (int) hired, city, address, telephone, countryID);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                firstnameEditText.setText(firstname);
                                lastnameEditText.setText(lastname);
                                addressEditText.setText(address);
                                cityEditText.setText(city);
                                telephoneEditText.setText(telephone);
                            }
                        });

                    } else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(getActivity(), title, message);
                                alertDialog.show();
                            }
                        });
                    }

                    //CONNECTION ERROR
                    else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final AlertDialog alertDialog = AsyncTasks.createConnectionErrorDialog(getActivity());
                                alertDialog.show();
                            }
                        });
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (user !=null) {
                if (!user.getUsername().equals(sharedPreferences.getString(MainActivity.PREFERENCE_USERNAME, ""))) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Invalid user selected", Toast.LENGTH_LONG).show();
                        }
                    });
                    getFragmentManager().popBackStack();
                }
            }

            if (countryID != 0) new GetCountriesAsyncTask().execute();
        }
    }

    private class EditEmployeeAsyncTask extends AsyncTask<Void,Void,Void> {

        private String query;
        private String responseData;

        public EditEmployeeAsyncTask() {


            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("UserID", String.valueOf(user.getUserID()))
                    .appendQueryParameter("SessionID", sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, ""))
                    .appendQueryParameter("Username", user.getUsername())
                    .appendQueryParameter("Password", user.getPassword())
                    .appendQueryParameter("UserLevelID", String.valueOf(user.getUserLevelID()))
                    .appendQueryParameter("Firstname", String.valueOf(firstnameEditText.getText()))
                    .appendQueryParameter("Lastname", String.valueOf(lastnameEditText.getText()))
                    .appendQueryParameter("DateHired", String.valueOf(user.getDateHired()))
                    .appendQueryParameter("City", String.valueOf(cityEditText.getText()))
                    .appendQueryParameter("Address", String.valueOf(addressEditText.getText()))
                    .appendQueryParameter("Telephone", String.valueOf(telephoneEditText.getText()))
                    .appendQueryParameter("CountryID", String.valueOf(countries.get(countrySpinner.getSelectedItemPosition()).getID()));

            this.query = builder.build().getEncodedQuery();
        }

        @Override
        protected Void doInBackground(Void... params) {



            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Users", "Update"));
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Employee '" + user.getFirstname() + "' saved.", Toast.LENGTH_LONG).show();
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                fm.popBackStack();
                                if (v != null) {
                                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                }
                                MainActivity mainActivity = (MainActivity) getActivity();
                                mainActivity.updateTextviews(user.getFirstname(), user.getLastname(), String.valueOf(user.getUserLevelID()));
                                mainActivity.recreate();
                            }
                        });
                    }
                    //ERROR:
                    else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(getActivity(), title, message);
                                alertDialog.show();
                            }
                        });
                    }

                }
                //CONNECTION ERROR
                else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final AlertDialog alertDialog = AsyncTasks.createConnectionErrorDialog(getActivity());
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


}
