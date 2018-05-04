package com.easybusiness.eb_androidapp.UI.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.easybusiness.eb_androidapp.AsyncTask.AsyncTasks;
import com.easybusiness.eb_androidapp.Entities.Customers;
import com.easybusiness.eb_androidapp.Entities.Products;
import com.easybusiness.eb_androidapp.Entities.SaleProducts;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.Adapters.CustomerAdapter;
import com.easybusiness.eb_androidapp.UI.Adapters.NewSaleAdapter;
import com.easybusiness.eb_androidapp.UI.Adapters.SalesAdapter;
import com.easybusiness.eb_androidapp.UI.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AddSaleFragment extends Fragment {

    public static final String TAG = "AddSaleFragment";
    public static final String TITLE = "Add Sale";

    private ProgressBar progressBar;
    private View layout;
    private ArrayList<Products> productsList;
    private ArrayList<Customers> customersList;
    private ArrayList<SaleProducts> addedSaleProductsList;
    private SharedPreferences sharedPreferences;
    private String sessionID;
    private int maxSaleID = -1;
    private String addedProducts = "";
    private int addsToComplete = 0;
    private int completedAdds = 0;

    private FloatingActionButton addProductButton;
    private ListView productsListview;
    private Button addSaleButton;
    private Spinner customerSpinner;

    public AddSaleFragment() {
        addedSaleProductsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_sale, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "");

        progressBar = v.findViewById(R.id.add_sale_progress);
        layout = v.findViewById(R.id.add_sale_layout);

        addProductButton = v.findViewById(R.id.add_product_action_btn);
        productsListview = v.findViewById(R.id.add_sale_products_listview);
        addSaleButton = v.findViewById(R.id.add_sale_Btn);
        customerSpinner = v.findViewById(R.id.add_sale_customer_spinner);

        addSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddSaleAsyncTask().execute();
            }
        });

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSupplyDialog();
            }
        });

        return v;
    }

    private void showSupplyDialog() {
        String[] options = new String[productsList.size()];

        for (int i = 0; i < productsList.size(); i++) {
            options[i] = productsList.get(i).getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final int selectedProductID = productsList.get(i).getID();

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
                        int quantity = Integer.parseInt(String.valueOf(quantityEditText.getText()));

                        //Check if quantity is less or equal to that of the product stock:
                        int stock = 0;
                        for (int i = 0; i < productsList.size(); i++) {
                            if (productsList.get(i).getID() == selectedProductID) {
                                stock = productsList.get(i).getQuantityInStock();
                                break;
                            }
                        }
                        if (quantity > stock) {
                            Toast.makeText(getActivity(), "The quantity cannot be more than the stock (" + stock + ")", Toast.LENGTH_LONG).show();
                            return;
                        }

                        addedSaleProductsList.add(new SaleProducts(0, -1, selectedProductID, quantity));
                        NewSaleAdapter adapter = new NewSaleAdapter(getActivity(), addedSaleProductsList, productsList);
                        productsListview.setAdapter(adapter);
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

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(TITLE);

        new GetCustomersAsyncTask().execute();

    }

    public class GetCustomersAsyncTask extends AsyncTask<Void,Void,Void> {

        private String query;
        private String responseData;

        @Override
        protected Void doInBackground(Void... params) {

            customersList = new ArrayList<>();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                    addProductButton.setVisibility(View.GONE);
                    layout.setVisibility(View.GONE);
                }
            });

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("Limit", "0")
                    .appendQueryParameter("SessionID", sessionID);

            query = builder.build().getEncodedQuery();

            if (query == null) query = "";

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Customers", "GetMultiple"));
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
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);

                            int id = jsonObject.getInt("ID");
                            String name = jsonObject.getString("Name");
                            int countryID = jsonObject.getInt("CountryID");
                            String city = jsonObject.getString("City");
                            String address = jsonObject.getString("Address");
                            String telephone = jsonObject.getString("Telephone");
                            int customerProductsID = jsonObject.getInt("CustomerProductsID");

                            Customers p = new Customers(id, name, countryID, city, address, telephone, customerProductsID);
                            customersList.add(p);
                        }

                        String[] customerNames = new String[customersList.size()];
                        for (int i = 0; i < customersList.size(); i++) {
                            customerNames[i] = customersList.get(i).getName();
                        }

                        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, customerNames);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                customerSpinner.setAdapter(adapter);
                            }
                        });

                    }
                    else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
                        final AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(getActivity(), title, message);
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

        @Override
        protected void onPostExecute(Void aVoid) {
            new GetProductsAsyncTask().execute();
        }
    }

    public class GetProductsAsyncTask extends AsyncTask<Void,Void,Void> {

        private String query;
        private String responseData;

        @Override
        protected Void doInBackground(Void... params) {

            productsList = new ArrayList<>();

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("Limit", "0")
                    .appendQueryParameter("SessionID", sessionID);

            query = builder.build().getEncodedQuery();

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


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                layout.setVisibility(View.VISIBLE);
                                addProductButton.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                    else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
                        final AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(getActivity(), title, message);
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

    public class AddSaleAsyncTask extends AsyncTask<Void,Void,Void> {

        private String query;
        private String responseData;
        private boolean result = false;

        @Override
        protected Void doInBackground(Void... params) {

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("ID", "0")
                    .appendQueryParameter("CustomerID", String.valueOf(customersList.get(customerSpinner.getSelectedItemPosition()).getID()))
                    .appendQueryParameter("SaleProductsID", "0")
                    .appendQueryParameter("Tax", "0")
                    .appendQueryParameter("SaleTimeDate", String.valueOf(System.currentTimeMillis()))
                    .appendQueryParameter("SessionID", sessionID);

            query = builder.build().getEncodedQuery();

            if (query == null) query = "";

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Sales", "Create"));
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
                        result = true;
                    }
                    else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
                        final AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(getActivity(), title, message);
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

        @Override
        protected void onPostExecute(Void aVoid) {
            if (result) {
                new GetMaxSaleIDAsyncTask().execute();
            }
        }
    }

    public class GetMaxSaleIDAsyncTask extends AsyncTask<Void,Void,Void> {

        private String query;
        private String responseData;
        private boolean result = false;

        @Override
        protected Void doInBackground(Void... params) {

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("a", "a");

            query = builder.build().getEncodedQuery();

            if (query == null) query = "";

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Sales", "GetMaxID"));
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
                    responseData = responseData.replace("\r\n", "");
                    responseData = responseData.replace("\n", "");
                    maxSaleID = Integer.parseInt(responseData);
                    result = true;

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

        @Override
        protected void onPostExecute(Void aVoid) {
            if (result) {
                addsToComplete = addedSaleProductsList.size();
                for (int i = 0; i < addedSaleProductsList.size(); i++) {
                    new AddSaleProductsAsyncTask(
                            addedSaleProductsList.get(i).getProductID(),
                            productsList.get(addedSaleProductsList.get(i).getID()).getName(),
                            addedSaleProductsList.get(i).getQuantitySold())
                            .execute();
                }
            }
        }
    }

    public class AddSaleProductsAsyncTask extends AsyncTask<Void,Void,Void> {

        private String query;
        private String responseData;
        private boolean result = false;

        int productID = -1;
        int quantity = -1;
        String productName;

        public AddSaleProductsAsyncTask(int productID, String productName, int quantity) {
            this.productID = productID;
            this.quantity = quantity;
            this.productName = productName;
        }

        @Override
        protected Void doInBackground(Void... params) {

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("ID", "0")
                    .appendQueryParameter("SaleID", String.valueOf(maxSaleID))
                    .appendQueryParameter("ProductID", String.valueOf(productID))
                    .appendQueryParameter("QuantitySold", String.valueOf(quantity))
                    .appendQueryParameter("SessionID", sessionID);

            query = builder.build().getEncodedQuery();

            if (query == null) query = "";

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Saleproducts", "Create"));
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
                        //TODO -- Added correctly, but wrong name displayed!!!!!
                        addedProducts += productName + ", ";
                        completedAdds++;
                    }
                    else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
                        final AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(getActivity(), title, message);
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

        @Override
        protected void onPostExecute(Void aVoid) {
            if (completedAdds == addsToComplete) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Added " + addedProducts.substring(0, addedProducts.length() - 2), Toast.LENGTH_LONG).show();
                        addedSaleProductsList = new ArrayList<>();
                        NewSaleAdapter adapter = new NewSaleAdapter(getActivity(), addedSaleProductsList, productsList);
                        productsListview.setAdapter(adapter);
                    }
                });
            }
        }
    }

}
