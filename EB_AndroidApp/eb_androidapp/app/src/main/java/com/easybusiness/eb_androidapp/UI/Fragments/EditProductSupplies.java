package com.easybusiness.eb_androidapp.UI.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.easybusiness.eb_androidapp.AsyncTask.AsyncTasks;
import com.easybusiness.eb_androidapp.Entities.ProductSupplies;
import com.easybusiness.eb_androidapp.Entities.Products;
import com.easybusiness.eb_androidapp.Entities.Supplies;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.Adapters.ProductAdapter;
import com.easybusiness.eb_androidapp.UI.Adapters.ProductSuppliesAdapter;
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
public class EditProductSupplies extends Fragment {

    public static final String TAG = "EditProductSuppliesFragment";
    public static final String TITLE = "Product Supplies";
    private View v;
    private SharedPreferences preferences;

    private ArrayList<Products> productsList;
    private ArrayList<ProductSupplies> productSupplies;
    private ArrayList<Supplies> suppliesList;

    private ProgressBar progressBar;
    private View layout;
    private Button addProductSupply;
    private ListView productSuppliesListview;
    private Spinner productSpinner;

    public EditProductSupplies() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_edit_product_supplies, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        addProductSupply = v.findViewById(R.id.add_productSupplyButton);
        productSuppliesListview = v.findViewById(R.id.productSupplies_listview);
        productSpinner = v.findViewById(R.id.selectProduct_Spinner);
        progressBar = v.findViewById(R.id.edit_productSupplies_progress);
        layout = v.findViewById(R.id.edit_productSupplies_layout);

        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new GetProductSuppliesAsyncTask().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addProductSupply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSupplyDialog();
            }
        });

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(TITLE);

        new GetProductsAsyncTask().execute();


    }

    private void showSupplyDialog() {
        String[] options = new String[suppliesList.size()];

        for (int i = 0; i < suppliesList.size(); i++) {
            options[i] = suppliesList.get(i).getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final int selectedSupplyID = suppliesList.get(i).getID();
                final int selectedProductID = productsList.get(productSpinner.getSelectedItemPosition()).getID();

                //Create quantity dialog:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_product_supplies_quantity, null);
                dialogBuilder.setView(dialogView);
                final EditText quantityEditText = dialogView.findViewById(R.id.dialog_productSupplies_quantityEdittext);
                quantityEditText.setText("1");
                dialogBuilder.setTitle("Enter quantity");
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new AddProductSupply(selectedProductID, selectedSupplyID, Integer.parseInt(quantityEditText.getText().toString()))
                                .execute();
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

            }
        });
        builder.create().show();
    }

    public class AddProductSupply extends AsyncTask<Void,Void,Void> {

        private String query;
        private String responseData;

        public AddProductSupply(int productID, int supplyID, int quantity) {
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("SessionID", preferences.getString(MainActivity.PREFERENCE_SESSIONID, ""))
                    .appendQueryParameter("ID", "0")
                    .appendQueryParameter("ProductID", String.valueOf(productID))
                    .appendQueryParameter("SupplyID", String.valueOf(supplyID))
                    .appendQueryParameter("QuantityRequired", String.valueOf(quantity));

            query = builder.build().getEncodedQuery();
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (query == null) query = "";

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Productsupplies", "Create"));
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
                                Toast.makeText(getActivity(), "Supply added.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final android.app.AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(getActivity(), title, message);
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
                            final android.app.AlertDialog alertDialog = AsyncTasks.createConnectionErrorDialog(getActivity());
                            alertDialog.show();
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new GetProductSuppliesAsyncTask().execute();
        }
    }

    public class GetProductsAsyncTask extends AsyncTask<Void,Void,Void> {

        private String query;
        private String responseData;

        public GetProductsAsyncTask() {
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("SessionID", preferences.getString(MainActivity.PREFERENCE_SESSIONID, ""))
                    .appendQueryParameter("Limit", "0");

            query = builder.build().getEncodedQuery();
        }

        @Override
        protected Void doInBackground(Void... params) {

            productsList = new ArrayList<>();

            if (query == null) query = "";

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Products", "GetMultiple"));
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
                        productsList = new ArrayList<>();
                        JSONArray dataArray = outterObject.getJSONArray("Data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            int id = jsonObject.getInt("ID");
                            String name = jsonObject.getString("Name");
                            double price = jsonObject.getDouble("Price");
                            int productSizeID = jsonObject.getInt("ProductSizeID");
                            int productTypeID = jsonObject.getInt("ProductTypeID");
                            int productSuppliesID = jsonObject.getInt("ProductSuppliesID");
                            int quantityInStock = jsonObject.getInt("QuantityInStock");
                            Products p = new Products(id, name, price, quantityInStock, productSizeID, productTypeID, productSuppliesID);
                            productsList.add(p);
                        }

                        String[] productNames = new String[productsList.size()];
                        for (int i = 0; i < productsList.size(); i++) {
                            productNames[i] = productsList.get(i).getName();
                        }

                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, productNames);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                productSpinner.setAdapter(adapter);
                            }
                        });

                    }
                    else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
                        final android.app.AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(getActivity(), title, message);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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
                            final android.app.AlertDialog alertDialog = AsyncTasks.createConnectionErrorDialog(getActivity());
                            alertDialog.show();
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new GetSuppliesAsyncTask().execute();
        }
    }

    public class GetSuppliesAsyncTask extends AsyncTask<Void,Void,Void> {

        private String query;
        private String responseData;

        public GetSuppliesAsyncTask() {
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("SessionID", preferences.getString(MainActivity.PREFERENCE_SESSIONID, ""))
                    .appendQueryParameter("Limit", "0");

            query = builder.build().getEncodedQuery();
        }

        @Override
        protected Void doInBackground(Void... params) {

            suppliesList = new ArrayList<>();

            if (query == null) query = "";

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Supplies", "GetMultiple"));
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
                        suppliesList = new ArrayList<>();
                        JSONArray dataArray = outterObject.getJSONArray("Data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            int id = jsonObject.getInt("ID");
                            String name = jsonObject.getString("Name");
                            int supplierID = jsonObject.getInt("SupplierID");
                            int quantity = jsonObject.getInt("Quantity");
                            double price = jsonObject.getDouble("Price");
                            Supplies p = new Supplies(id, name, supplierID, quantity, (float) price);
                            suppliesList.add(p);
                        }

                    }
                    else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
                        final android.app.AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(getActivity(), title, message);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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
                            final android.app.AlertDialog alertDialog = AsyncTasks.createConnectionErrorDialog(getActivity());
                            alertDialog.show();
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new GetProductSuppliesAsyncTask().execute();
        }
    }

    public class GetProductSuppliesAsyncTask extends AsyncTask<Void,Void,Void> {

        private String query;
        private String responseData;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }

        public GetProductSuppliesAsyncTask() {

            int productID = 0;
            if (productSpinner.getCount() > 0) {
                productID = productsList.get(productSpinner.getSelectedItemPosition()).getID();
            }
            else {
                return;
            }

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("SessionID", preferences.getString(MainActivity.PREFERENCE_SESSIONID, ""))
                    .appendQueryParameter("Fields", "ProductID")
                    .appendQueryParameter("Values", String.valueOf(productID));

            query = builder.build().getEncodedQuery();
        }

        @Override
        protected Void doInBackground(Void... params) {

            productSupplies = new ArrayList<>();

            if (query == null) query = "";

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Productsupplies", "SearchByField"));
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
                        productSupplies = new ArrayList<>();
                        JSONArray dataArray = outterObject.getJSONArray("Data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            int id = jsonObject.getInt("ID");
                            int productID = jsonObject.getInt("ProductID");
                            int supplyID = jsonObject.getInt("SupplyID");
                            int quantityRequired = jsonObject.getInt("QuantityRequired");
                            ProductSupplies p = new ProductSupplies(id, productID, supplyID, quantityRequired);
                            productSupplies.add(p);
                        }

                        final ProductSuppliesAdapter adapter = new ProductSuppliesAdapter(getActivity(), productSupplies, suppliesList);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                productSuppliesListview.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);
                                layout.setVisibility(View.VISIBLE);
                            }
                        });

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                layout.setVisibility(View.VISIBLE);
                            }
                        });

                    }

                    else if (title.equals("Failed to retrieve item(s).")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayAdapter<String> emptyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_2);
                                productSuppliesListview.setAdapter(emptyAdapter);
                                Toast.makeText(getActivity(), "No supplies exist for this item.", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                layout.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                    else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final android.app.AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(getActivity(), title, message);
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
                            final android.app.AlertDialog alertDialog = AsyncTasks.createConnectionErrorDialog(getActivity());
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
