package com.easybusiness.eb_androidapp.UI.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.easybusiness.eb_androidapp.AsyncTask.AsyncTasks;
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
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddSuppliersFragment extends Fragment {

    public static final String TAG = "AddSuppliersFragment";
    public static final String TITLE = "Add Suppliers";

    private ArrayList<Countries> countries;

    private View v;
    private EditText nameEditText;
    private EditText addressEditText;
    private EditText telephoneEditText;
    private EditText cityEditText;
    private Spinner countrySpinner;
    private Button add_supplier_Btn;
    private SharedPreferences sharedPreferences;

    public AddSuppliersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_add_suppliers, container, false);

        add_supplier_Btn = v.findViewById(R.id.add_supplier_Btn);
        nameEditText = v.findViewById(R.id.supplier_name_edittext);
        addressEditText = v.findViewById(R.id.supplier_address_edittext);
        telephoneEditText = v.findViewById(R.id.supplier_telephone_edittext);
        cityEditText = v.findViewById(R.id.supplier_city_edittext);
        countrySpinner = v.findViewById(R.id.supplier_country_spinner);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(TITLE);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        getRequiredFields();

        add_supplier_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput()) {
                    new AddSuppliersAsyncTask(
                            getActivity(),
                            view,
                            sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, ""),
                            nameEditText.getText().toString(),
                            cityEditText.getText().toString(),
                            addressEditText.getText().toString(),
                            telephoneEditText.getText().toString(),
                            countries.get(countrySpinner.getSelectedItemPosition()).getID()
                    ).execute();
                }
            }
        });

    }

    private boolean checkInput() {

        boolean flag = true;

        //Check first name:
        if (nameEditText.getText().toString().trim().isEmpty()) {
            nameEditText.setError("Please provide a valid answer.");
            flag = false;
        }
        else {
            nameEditText.setError(null);
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
        new AddSuppliersFragment.GetCountriesAsyncTask("", getActivity(), v).execute();

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("SessionID", sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, ""));

        //String userLevelsQuery = builder.build().getEncodedQuery();

       // new AddEmployeesFragment.GetUserLevelsAsyncTask(userLevelsQuery, getActivity(), v).execute();
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
                            Spinner countriesSpinner = view.findViewById(R.id.supplier_country_spinner);
                            countriesSpinner.setAdapter(adapter);
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

    private class AddSuppliersAsyncTask extends AsyncTask<Void,Void,Void> {

        private String name;
        private String query;
        private String responseData;
        private Activity activity;
        private View view;
        private ArrayAdapter<String> adapter;

        public AddSuppliersAsyncTask(Activity activity, View view, String sessionID,  String name, String city, String address, String telephone, int countryID) {

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("ID", "0")
                    .appendQueryParameter("SessionID", sessionID)
                    .appendQueryParameter("Name", name)
                    .appendQueryParameter("City", city)
                    .appendQueryParameter("Address", address)
                    .appendQueryParameter("Telephone", telephone)
                    .appendQueryParameter("CountryID", String.valueOf(countryID));

            this.name = name;
            this.query = builder.build().getEncodedQuery();
            this.activity = activity;
            this.view = view;
            countries = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {



            try {
                URL url = new URL(AsyncTasks.encodeForAPI(activity.getString(R.string.baseURL), "Suppliers", "Create"));
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
                                Toast.makeText(activity, "Suppliers '" + name + "' created.", Toast.LENGTH_LONG).show();
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