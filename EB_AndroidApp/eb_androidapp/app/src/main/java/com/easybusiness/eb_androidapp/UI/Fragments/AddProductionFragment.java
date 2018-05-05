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
import com.easybusiness.eb_androidapp.Entities.ProductionBatches;
import com.easybusiness.eb_androidapp.Entities.Products;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.Adapters.NewProductionAdapter;
import com.easybusiness.eb_androidapp.UI.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AddProductionFragment extends Fragment {

    public static final String TAG = "AddProductionFragment";
    public static final String TITLE = "Add Production";

    private ProgressBar progressBar;
    private View layout;
    private ArrayList<Products> productsList;
    private ArrayList<ProductionBatches> addedProductionProductsList;
    private SharedPreferences sharedPreferences;
    private String sessionID;
    private int maxProductionID = -1;
    private String addedProducts = "";
    private int addsToComplete = 0;
    private int completedAdds = 0;
    private int completedIncreaseTasks = 0;

    private FloatingActionButton addProductButton;
    private ListView productsListview;
    private Button addProductionButton;

    public AddProductionFragment() {
        addedProductionProductsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_production, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "");

        progressBar = v.findViewById(R.id.add_production_product_progress);
        layout = v.findViewById(R.id.add_production_product_layout);

        addProductButton = v.findViewById(R.id.add_production_product_action_btn);
        productsListview = v.findViewById(R.id.add_production_product_listview);
        addProductionButton = v.findViewById(R.id.add_production_product_Btn);

        addProductionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < addedProductionProductsList.size(); i++) {
                    new AddProductionAsyncTask(addedProductionProductsList.get(i).getQuantityProduced(), addedProductionProductsList.get(i).getProductID()).execute();
                }
            }
        });

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductDialog();
            }
        });

        return v;
    }

    private void showProductDialog() {
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
                        int quantity = 0;
                        try {
                            quantity = Integer.parseInt(String.valueOf(quantityEditText.getText()));
                        } catch (Exception e) {
                            quantityEditText.setText("0");
                            return;
                        }

                        addedProductionProductsList.add(new ProductionBatches(0, quantity, selectedProductID, System.currentTimeMillis()));
                        NewProductionAdapter adapter = new NewProductionAdapter (getActivity(), addedProductionProductsList, productsList);
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

        new GetProductsAsyncTask().execute();

    }

    public class GetProductsAsyncTask extends AsyncTask<Void, Void, Void> {

        private String query;
        private String responseData;

        @Override
        protected Void doInBackground(Void... params) {

            productsList = new ArrayList<>();

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

                    } else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
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

    public class AddProductionAsyncTask extends AsyncTask<Void, Void, Void> {

        private String query;
        private String responseData;
        private boolean result = false;
        private int quantity = -1;
        private int productID = -1;

        public AddProductionAsyncTask(int quantity, int productID) {
            this.quantity = quantity;
            this.productID = productID;
        }

        @Override
        protected Void doInBackground(Void... params) {

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("ID", "0")
                    .appendQueryParameter("QuantityProduced", String.valueOf(quantity))
                    .appendQueryParameter("ProductID", String.valueOf(productID))
                    .appendQueryParameter("ProductionDate", String.valueOf(System.currentTimeMillis()))
                    .appendQueryParameter("SessionID", sessionID);

            query = builder.build().getEncodedQuery();

            if (query == null) query = "";

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Productionbatches", "Create"));
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
                    } else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
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
                new GetMaxProductionBatchIDAsyncTask().execute();
            }
        }
    }

    public class GetMaxProductionBatchIDAsyncTask extends AsyncTask<Void, Void, Void> {

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
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Productionbatches", "GetMaxID"));
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
                    maxProductionID = Integer.parseInt(responseData);
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
                addsToComplete = addedProductionProductsList.size();
                for (int i = 0; i < addedProductionProductsList.size(); i++) {
                    new IncreaseStockAsyncTask(addedProductionProductsList.get(i).getProductID(), addedProductionProductsList.get(i).getQuantityProduced()).execute();
                }
            }
        }
    }

    public class IncreaseStockAsyncTask extends AsyncTask<Void, Void, Void> {

        private String query;
        private String responseData;

        int productID = -1;
        int quantity = -1;
        Products p;

        public IncreaseStockAsyncTask(int productID, int quantity) {
            this.productID = productID;
            this.quantity = quantity;

            for (int i = 0; i < productsList.size(); i++) {
                if (productsList.get(i).getID() == productID) {
                    p = productsList.get(i);
                    break;
                }
            }

        }

        @Override
        protected Void doInBackground(Void... params) {

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("ID", String.valueOf(p.getID()))
                    .appendQueryParameter("Name", p.getName())
                    .appendQueryParameter("Price", String.valueOf(p.getPrice()))
                    .appendQueryParameter("QuantityInStock", String.valueOf(p.getQuantityInStock() + quantity))
                    .appendQueryParameter("ProductSizeID", String.valueOf(p.getProductSizeID()))
                    .appendQueryParameter("ProductTypeID", String.valueOf(p.getProductTypeID()))
                    .appendQueryParameter("ProductSuppliesID", String.valueOf(p.getProductSuppliesID()))
                    .appendQueryParameter("SessionID", sessionID);
            query = builder.build().getEncodedQuery();

            if (query == null) query = "";

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Products", "Update"));
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
                        completedIncreaseTasks++;
                        addedProducts += p.getName() + ", ";
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addedProducts = addedProducts.substring(0, addedProducts.length() - 2);
                                Toast.makeText(getActivity(), "Added " + addedProducts, Toast.LENGTH_LONG).show();
                                if (completedIncreaseTasks == addsToComplete)
                                    getFragmentManager().popBackStack();
                            }
                        });
                    } else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
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

}
