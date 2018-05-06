package com.easybusiness.eb_androidapp.UI.Fragments;


import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
public class EditSupplierFragment extends Fragment {

    public static final String TAG = "EditProductFragment";

    private View v;
    private int supplierID;
    private String supplierName;
    private int countryID = 0;

    private ArrayList<Countries> countriesList;

    private SharedPreferences sharedPreferences;
    private String sessionID;

    private EditText nameEditText;
    private EditText telephoneEditText;
    private EditText addressEditText;
    private EditText cityEditText;
    private Spinner countrySpinner;
    private Button saveButton;

    private ProgressBar progressBar;
    private View layout;

    public EditSupplierFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_edit_supplier, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "None");

        progressBar = v.findViewById(R.id.edit_supplier_progress);
        layout = v.findViewById(R.id.edit_supplier_layout);

        nameEditText = v.findViewById(R.id.edit_supplier_name_edittext);
        telephoneEditText = v.findViewById(R.id.edit_supplier_telephone_edittext);
        addressEditText = v.findViewById(R.id.edit_supplier_address_edittext);
        cityEditText = v.findViewById(R.id.edit_supplier_city_edittext);
        countrySpinner = v.findViewById(R.id.edit_supplier_country_spinner);
        saveButton = v.findViewById(R.id.edit_supplier_save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput()) {
                    new UpdateSupplierAsyncTask().execute();
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            supplierID = bundle.getInt(ViewSupplierFragment.SUPPLIER_ID_KEY);
            supplierName = bundle.getString(ViewSupplierFragment.SUPPLIER_NAME_KEY);
        }

        getActivity().setTitle(supplierName);

        new GetSupplierAsyncTask().execute();

    }

    private boolean checkInput() {
        boolean flag = true;

        //Check name:
        if (nameEditText.getText().toString().trim().isEmpty()) {
            nameEditText.setError("Please provide a valid answer.");
            flag = false;
        }
        else {
            nameEditText.setError(null);
        }

        //Check address:
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

        //Telephone
        if (telephoneEditText.getText().toString().trim().isEmpty()) {
            telephoneEditText.setError("Please provide a valid answer.");
            flag = false;
        }
        else {
            telephoneEditText.setError(null);
        }

        return flag;
    }

    private class GetSupplierAsyncTask extends AsyncTask<Void, Void, Void> {

        private String responseData;

        @Override
        protected Void doInBackground(Void... voids) {


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    layout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                }
            });


            String query;
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("ID", String.valueOf(supplierID))
                    .appendQueryParameter("SessionID", sessionID);
            query = builder.build().getEncodedQuery();

            System.out.println("GET SUPPLIER QUERY -->" + query);

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Suppliers", "GetByID"));
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
                    System.out.println(responseData);

                    final String status = outterObject.getString("Status");
                    final String title = outterObject.getString("Title");
                    final String message = outterObject.getString("Message");

                    if (status.equals(AsyncTasks.RESPONSE_OK)) {
                        JSONObject jsonObject = outterObject.getJSONObject("Data");

                        final String name = jsonObject.getString("Name");
                        final String address = jsonObject.getString("Address");
                        final String city = jsonObject.getString("City");
                        final String telephone = jsonObject.getString("Telephone");
                        countryID = jsonObject.getInt("CountryID");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nameEditText.setText(name);
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
            if (countryID != 0) new GetCountriesAsyncTask().execute();
        }
    }

    private class GetCountriesAsyncTask extends AsyncTask<Void,Void,Void> {

        private String responseData;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            String query;

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("Limit", "0")
                    .appendQueryParameter("SessionID", sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, ""));
            query = builder.build().getEncodedQuery();

            if (query == null) query = "";

            System.out.println("PRODUCT TYPES QUERY: " + query);

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

                    System.out.println(responseData);
                    JSONObject outterObject = new JSONObject(responseData);
                    final String status = outterObject.getString("Status");
                    final String title = outterObject.getString("Title");
                    final String message = outterObject.getString("Message");

                    ArrayList<String> countryNames = new ArrayList<>();
                    countriesList = new ArrayList<>();

                    if (status.equals(AsyncTasks.RESPONSE_OK)) {
                        final JSONArray dataArray = outterObject.getJSONArray("Data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            int id = jsonObject.getInt("ID");
                            String name = jsonObject.getString("Name");
                            Countries p = new Countries(id, name);
                            countriesList.add(p);
                            countryNames.add(p.getName());
                        }

                        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, countryNames);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                countrySpinner.setAdapter(adapter);
                            }
                        });

                        for (int i = 0; i < countriesList.size(); i++) {
                            final int pos = i;
                            if (countriesList.get(i).getID() == countryID) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        countrySpinner.setSelection(pos);
                                    }
                                });
                                break;
                            }
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                layout.setVisibility(View.VISIBLE);
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

    private class UpdateSupplierAsyncTask extends AsyncTask<Void, Void, Void> {

        private String responseData;

        @Override
        protected Void doInBackground(Void... voids) {


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    layout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                }
            });


            String query;
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("ID", String.valueOf(supplierID))
                    .appendQueryParameter("Name", String.valueOf(nameEditText.getText()))
                    .appendQueryParameter("CountryID", String.valueOf(countriesList.get(countrySpinner.getSelectedItemPosition()).getID()))
                    .appendQueryParameter("Address", String.valueOf(addressEditText.getText()))
                    .appendQueryParameter("Telephone", String.valueOf(telephoneEditText.getText()))
                    .appendQueryParameter("City", String.valueOf(cityEditText.getText()))
                    .appendQueryParameter("SessionID", sessionID);
            query = builder.build().getEncodedQuery();

            System.out.println("UPDATE SUPPLIERS QUERY -->" + query);

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Suppliers", "Update"));
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
                    System.out.println(responseData);

                    final String status = outterObject.getString("Status");
                    final String title = outterObject.getString("Title");
                    final String message = outterObject.getString("Message");

                    if (status.equals(AsyncTasks.RESPONSE_OK)) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), nameEditText.getText() + " saved.", Toast.LENGTH_LONG).show();
                                getActivity().getSupportFragmentManager().popBackStack();
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

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            });

            return null;

        }

    }

}
