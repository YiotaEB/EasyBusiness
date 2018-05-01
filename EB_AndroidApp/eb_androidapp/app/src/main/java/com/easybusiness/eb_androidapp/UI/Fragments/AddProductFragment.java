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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.easybusiness.eb_androidapp.AsyncTask.AsyncTasks;
import com.easybusiness.eb_androidapp.Entities.ProductSizes;
import com.easybusiness.eb_androidapp.Entities.ProductTypes;
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

public class AddProductFragment extends Fragment {

    public static final String TAG = "AddProductFragment";
    public static final String TITLE = "Add Product";

    private View v;

    private EditText productNameEditText;
    private EditText productPriceEditText;
    private EditText productQuantityEditText;
    private Spinner productTypeSpinner;
    private Spinner productSizeSpinner;
    private Button addProductButton;
    ProgressBar progressBar;
    View layout;

    private ArrayList<ProductTypes> productTypes;
    private ArrayList<ProductSizes> productSizes;

    private SharedPreferences sharedPreferences;


    public AddProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_add_product, container, false);

        progressBar = v.findViewById(R.id.add_products_progress);
        layout = v.findViewById(R.id.add_employees_view);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        productNameEditText = v.findViewById(R.id.product_name_edittext);
        productPriceEditText = v.findViewById(R.id.product_price_edittext);
        productQuantityEditText = v.findViewById(R.id.product_quantity_edittext);
        productTypeSpinner = v.findViewById(R.id.product_type_spinner);
        productSizeSpinner = v.findViewById(R.id.product_size_spinner);
        addProductButton = v.findViewById(R.id.add_product_button);

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput()) {
                    new AddProductAsyncTask(
                            sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "")
                    ).execute();
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(TITLE);
        getRequiredFields();
    }

    private boolean checkInput() {

        boolean flag = true;

        //Check name:
        if (productNameEditText.getText().toString().trim().isEmpty()) {
            productNameEditText.setError("Please provide a valid name.");
            flag = false;
        }
        else {
            productNameEditText.setError(null);
        }

        //Check price
        if (productPriceEditText.getText().toString().trim().isEmpty()) {
            productPriceEditText.setError("Please provide a valid price.");
            flag = false;
        }
        else {
            productPriceEditText.setError(null);
        }

        //Check Quantity:
        if (productQuantityEditText.getText().toString().trim().isEmpty()) {
            productQuantityEditText.setError("Please provide a valid quantity.");
            flag = false;
        }
        else {
            productQuantityEditText.setError(null);
        }

        return flag;
    }

    private void getRequiredFields() {
        new GetProductTypesAsyncTask().execute();
        new GetProductSizesAsyncTask().execute();
    }

    private class GetProductTypesAsyncTask extends AsyncTask<Void,Void,Void> {

        private String responseData;
        private ArrayAdapter<String> adapter;

        @Override
        protected Void doInBackground(Void... params) {

            String query;

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("Limit", "0")
                    .appendQueryParameter("SessionID", sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, ""));
            query = builder.toString();

            if (query == null) query = "";

            System.out.println("PRODUCT TYPES QUERY: " + query);

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Producttypes", "GetMultiple"));
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
                        ArrayList<String> productNames = new ArrayList<>();
                        productTypes = new ArrayList<>();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            String name = jsonObject.getString("Name");
                            int id = jsonObject.getInt("ID");
                            ProductTypes c = new ProductTypes(id, name);
                            productTypes.add(c);
                            productNames.add(c.getName());
                        }

                        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, productNames);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                productTypeSpinner.setAdapter(adapter);
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

    private class GetProductSizesAsyncTask extends AsyncTask<Void,Void,Void> {

        private String responseData;
        private ArrayAdapter<String> adapter;

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
            query = builder.toString();

            if (query == null) query = "";

            System.out.println("PRODUCT SIZES QUERY: " + query);

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Productsizes", "GetMultiple"));
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
                        ArrayList<String> productSizeNames = new ArrayList<>();
                        productSizes = new ArrayList<>();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            String name = jsonObject.getString("Name");
                            int id = jsonObject.getInt("ID");
                            int unitTypeID = jsonObject.getInt("UnitTypeID");
                            ProductSizes s = new ProductSizes(id, name, unitTypeID);
                            productSizes.add(s);
                            productSizeNames.add(s.getName());
                        }

                        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, productSizeNames);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                productSizeSpinner.setAdapter(adapter);
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

    private class AddProductAsyncTask extends AsyncTask<Void,Void,Void> {

        private String responseData;
        private String query;
        private ArrayAdapter<String> adapter;

        public AddProductAsyncTask(String sessionID) {

            int productSizeID = productSizes.get(productSizeSpinner.getSelectedItemPosition()).getID();
            int productTypeID = productTypes.get(productTypeSpinner.getSelectedItemPosition()).getID();

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("ID", "0")
                    .appendQueryParameter("SessionID", sessionID)
                    .appendQueryParameter("Name", String.valueOf(productNameEditText.getText()))
                    .appendQueryParameter("Price", String.valueOf(productPriceEditText.getText()))
                    .appendQueryParameter("QuantityInStock", String.valueOf(productQuantityEditText.getText()))
                    .appendQueryParameter("ProductSizeID", String.valueOf(productSizeID))
                    .appendQueryParameter("ProductTypeID", String.valueOf(productTypeID))
                    .appendQueryParameter("ProductSuppliesID", "0");

            this.query = builder.build().getEncodedQuery();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Products", "Create"));
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Product '" + productNameEditText.getText() + "' created.", Toast.LENGTH_LONG).show();
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                fm.popBackStack();
                                if (v != null) {
                                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                }
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