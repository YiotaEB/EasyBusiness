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
import com.easybusiness.eb_androidapp.Entities.Suppliers;
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
public class EditSupplyFragment extends Fragment {

    public static final String TAG = "EditProductFragment";

    private View v;

    private int supplyID;
    private String supplyName;

    private ArrayList<Suppliers> suppliersList;

    private SharedPreferences sharedPreferences;
    private String sessionID;
    private int supplierID = 0;

    private EditText supplyNameEditText;
    private EditText quantityEditText;
    private EditText priceEditText;
    private Button saveButton;
    private Spinner suppliersSpinner;

    private ProgressBar progressBar;
    private View layout;

    public EditSupplyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_edit_supply, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "None");

        progressBar = v.findViewById(R.id.edit_supply_progress);
        layout = v.findViewById(R.id.edit_supply_layout);
        supplyNameEditText = v.findViewById(R.id.edit_supply_name_edittext);
        quantityEditText = v.findViewById(R.id.edit_supply_quantity_edittext);
        priceEditText = v.findViewById(R.id.edit_supply_price_edittext);
        suppliersSpinner = v.findViewById(R.id.edit_supply_supplier_spinner);
        saveButton = v.findViewById(R.id.edit_supply_save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput()) {
                    new UpdateSupplyAsyncTask().execute();
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
            supplyID = bundle.getInt(ViewSupplyFragment.SUPPLY_ID_KEY);
            supplyName = bundle.getString(ViewSupplyFragment.SUPPLY_NAME_KEY);
            supplierID = bundle.getInt(ViewSupplyFragment.SUPPLY_SUPPLIER);
        }

        getActivity().setTitle(supplyName);

        new GetSupplyAsyncTask().execute();

    }

    private boolean checkInput() {
        boolean flag = true;

        //Check name:
        if (supplyNameEditText.getText().toString().trim().isEmpty()) {
            supplyNameEditText.setError("Please provide a valid answer.");
            flag = false;
        }
        else {
            supplyNameEditText.setError(null);
        }

        //Check price:
        if (priceEditText.getText().toString().trim().isEmpty()) {
            priceEditText.setError("Please provide a valid answer.");
            flag = false;
        }
        else {
            priceEditText.setError(null);
        }

        //Check quantity
        if (quantityEditText.getText().toString().trim().isEmpty()) {
            quantityEditText.setError("Please provide a valid answer.");
            flag = false;
        }
        else {
            quantityEditText.setError(null);
        }

        return flag;
    }

    private class GetSupplyAsyncTask extends AsyncTask<Void, Void, Void> {

        private String responseData;

        @Override
        protected Void doInBackground(Void... voids) {


            String query;
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("ID", String.valueOf(supplyID))
                    .appendQueryParameter("SessionID", sessionID);
            query = builder.build().getEncodedQuery();


            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Supplies", "GetByID"));
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

                        final String name = jsonObject.getString("Name");
                        final String price = jsonObject.getString("Price");
                        final String quantity = jsonObject.getString("Quantity");

                        supplierID = jsonObject.getInt("SupplierID");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                supplyNameEditText.setText(name);
                                priceEditText.setText(price);
                                quantityEditText.setText(quantity);
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
            new GetSuppliersAsyncTask().execute();
        }
    }

    private class GetSuppliersAsyncTask extends AsyncTask<Void,Void,Void> {

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


            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Suppliers", "GetMultiple"));
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

                    ArrayList<String> supplierNames = new ArrayList<>();
                    suppliersList = new ArrayList<>();

                    if (status.equals(AsyncTasks.RESPONSE_OK)) {
                        final JSONArray dataArray = outterObject.getJSONArray("Data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            int id = jsonObject.getInt("ID");
                            String name = jsonObject.getString("Name");
                            int countryID = jsonObject.getInt("CountryID");
                            String address = jsonObject.getString("Address");
                            String telephone = jsonObject.getString("Telephone");
                            String city = jsonObject.getString("City");
                            Suppliers p = new Suppliers(id, name, countryID, address, telephone, city);
                            suppliersList.add(p);
                            supplierNames.add(p.getName());
                        }

                        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, supplierNames);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                suppliersSpinner.setAdapter(adapter);
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

    private class UpdateSupplyAsyncTask extends AsyncTask<Void, Void, Void> {

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
                    .appendQueryParameter("ID", String.valueOf(supplyID))
                    .appendQueryParameter("Name", String.valueOf(supplyNameEditText.getText()))
                    .appendQueryParameter("Price", String.valueOf(priceEditText.getText()))
                    .appendQueryParameter("Quantity", String.valueOf(quantityEditText.getText()))
                    .appendQueryParameter("SupplierID", String.valueOf(suppliersList.get(suppliersSpinner.getSelectedItemPosition()).getID()))
                    .appendQueryParameter("SessionID", sessionID);
            query = builder.build().getEncodedQuery();

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Supplies", "Update"));
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
                                Toast.makeText(getActivity(), supplyNameEditText.getText() + " saved.", Toast.LENGTH_LONG).show();
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
