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
public class EditEmployeesFragment extends Fragment {

    public static final String TAG = "EditEmployeesFragment";
    public static final String TITLE = "Edit Employee";

    private ArrayList<Countries> countries;
    private ArrayList<UserLevels> positions;

    private String title = TITLE;
    private Users user;

    private View v;
    private EditText firstnameEditText;
    private EditText lastnameEditText;
    private EditText usernameEditText;
    private EditText addressEditText;
    private EditText telephoneEditText;
    private EditText cityEditText;
    private Spinner countrySpinner;
    private Spinner positionSpinner;
    private Button editEmployeesButton;
    private SharedPreferences sharedPreferences;

    int year;
    int month;
    int day;

    public EditEmployeesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_edit_employees, container, false);

        editEmployeesButton = v.findViewById(R.id.edit_employees_Btn);
        firstnameEditText = v.findViewById(R.id.edit_employees_firstname_edittext);
        lastnameEditText = v.findViewById(R.id.edit_employees_lastname_edittext);
        usernameEditText = v.findViewById(R.id.edit_employees_username_edittext);
        addressEditText = v.findViewById(R.id.edit_employees_address_edittext);
        telephoneEditText = v.findViewById(R.id.edit_employees_telephone_edittext);
        cityEditText = v.findViewById(R.id.edit_employees_city_edittext);
        countrySpinner = v.findViewById(R.id.edit_country_spinner);
        positionSpinner = v.findViewById(R.id.edit_position_spinner);

        //Give values to the fields:
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (Users) bundle.getSerializable(ViewEmployeeFragment.EMPLOYEE_KEY);
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle(title);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        getRequiredFields();

        editEmployeesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput()) {
                    new EditEmployeeAsyncTask(
                            getActivity(),
                            view,
                            sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "")
                    ).execute();
                }
            }
        });

        //Set values for the inputs:
        firstnameEditText.setText(user.getFirstname());
        lastnameEditText.setText(user.getLastname());
        usernameEditText.setText(user.getUsername());
        addressEditText.setText(user.getAddress());
        telephoneEditText.setText(user.getTelephone());
        cityEditText.setText(user.getCity());
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

    private void getRequiredFields() {
        new GetCountriesAsyncTask("", getActivity(), v).execute();

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("SessionID", sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, ""));

        String userLevelsQuery = builder.build().getEncodedQuery();

        new GetUserLevelsAsyncTask(userLevelsQuery, getActivity(), v).execute();
    }

    private class GetCountriesAsyncTask extends AsyncTask<Void,Void,Void> {

        private String query;
        private String responseData;
        private Activity activity;
        private View view;
        private ArrayAdapter<String> adapter;

        public GetCountriesAsyncTask(String query, Activity activity, View view) {
            this.query = query;
            this.activity = activity;
            this.view = view;
            countries = new ArrayList<>();
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

                        adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, countryNames);

                    }
                    //ERROR:
                    else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(activity, title, message);
                                alertDialog.show();
                            }
                        });
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Spinner countriesSpinner = view.findViewById(R.id.edit_country_spinner);
                            countriesSpinner.setAdapter(adapter);
                            countriesSpinner.setSelection(user.getCountryID() - 1);
                        }
                    });

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

    private class GetUserLevelsAsyncTask extends AsyncTask<Void,Void,Void> {

        private String query;
        private String responseData;
        private Activity activity;
        private View view;
        private ArrayAdapter<String> adapter;

        public GetUserLevelsAsyncTask(String query, Activity activity, View view) {
            this.query = query;
            this.activity = activity;
            this.view = view;
            positions = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (query == null) query = "";

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(activity.getString(R.string.baseURL), "Userlevels", "GetMultiple"));
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
                        JSONArray dataArray = outterObject.getJSONArray("Data");
                        ArrayList<String> positionNames = new ArrayList<>();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            String name = jsonObject.getString("UserLevelName");
                            int id = jsonObject.getInt("UserLevelID");
                            boolean show;
                            int showInt = jsonObject.getInt("Show");
                            if (showInt > 0) show = true;
                            else show = false;
                            UserLevels u = new UserLevels(id, name, show);
                            if (u.getShow()) {
                                positions.add(u);
                                positionNames.add(u.getUserLevelName());
                            }
                        }

                        adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, positionNames);

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final View progressView = activity.findViewById(R.id.edit_employees_progress);
                                final View employeeAddView = activity.findViewById(R.id.edit_employees_view);
                                progressView.setVisibility(View.GONE);
                                employeeAddView.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                    //ERROR:
                    else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(activity, title, message);
                                alertDialog.show();
                            }
                        });
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Spinner positionsSpinner = view.findViewById(R.id.edit_position_spinner);
                            positionsSpinner.setAdapter(adapter);
                            positionsSpinner.setSelection(user.getUserLevelID() - 1);
                        }
                    });

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

    private class EditEmployeeAsyncTask extends AsyncTask<Void,Void,Void> {

        private String query;
        private String responseData;
        private Activity activity;
        private View view;
        private ArrayAdapter<String> adapter;

        public EditEmployeeAsyncTask(Activity activity, View view, String sessionID) {

            System.out.println("COUNTRIES SIZE: " + countries.size());

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("UserID", String.valueOf(user.getUserID()))
                    .appendQueryParameter("SessionID", sessionID)
                    .appendQueryParameter("Username", String.valueOf(usernameEditText.getText()))
                    .appendQueryParameter("Password", " ") //TODO
                    .appendQueryParameter("UserLevelID", String.valueOf(user.getUserLevelID()))
                    .appendQueryParameter("Firstname", String.valueOf(firstnameEditText.getText()))
                    .appendQueryParameter("Lastname", String.valueOf(lastnameEditText.getText()))
                    .appendQueryParameter("DateHired", String.valueOf(user.getDateHired()).substring(0, String.valueOf(user.getDateHired()).length() - 4))
                    .appendQueryParameter("City", String.valueOf(cityEditText.getText()))
                    .appendQueryParameter("Address", String.valueOf(addressEditText.getText()))
                    .appendQueryParameter("Telephone", String.valueOf(telephoneEditText.getText()))
                    .appendQueryParameter("CountryID", String.valueOf(countries.get(countrySpinner.getSelectedItemPosition()).getID()));

            this.query = builder.build().getEncodedQuery();
            System.out.println(query);
            this.activity = activity;
            this.view = view;
        }

        @Override
        protected Void doInBackground(Void... params) {



            try {
                URL url = new URL(AsyncTasks.encodeForAPI(activity.getString(R.string.baseURL), "Users", "Update"));
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
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "Employee '" + user.getFirstname() + "' saved.", Toast.LENGTH_LONG).show();
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                fm.popBackStack();
                                if (view != null) {
                                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                            }
                        });
                    }
                    //ERROR:
                    else {
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


}
