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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.easybusiness.eb_androidapp.AsyncTask.AsyncTasks;
import com.easybusiness.eb_androidapp.Entities.Products;
import com.easybusiness.eb_androidapp.Entities.SaleProducts;
import com.easybusiness.eb_androidapp.Entities.Sales;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.Adapters.SaleProductsAdapter;
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
public class ViewSaleFragment extends Fragment {

    public static final String TAG = "ViewSaleFragment";

    public static final String SALE_ID_KEY = "sale-id";

    private View v;

    private SharedPreferences sharedPreferences;
    private String sessionID;

    private TextView totalTextView;
    private ListView productsListView;
    private Button toPDFButton;

    private ProgressBar progressBar;
    private View layout;

    int saleID = 0;
    int saleProductsID = 0;
    int customerID = 0;
    ArrayList<SaleProducts> saleProductsList;
    ArrayList<Products> productsList;
    Sales sale;

    public ViewSaleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_sale, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "None");

        progressBar = v.findViewById(R.id.view_sale_progress);
        layout = v.findViewById(R.id.view_sale_layout);

        productsListView = v.findViewById(R.id.sale_products_listview);
        totalTextView = v.findViewById(R.id.sale_total_price);
        toPDFButton = v.findViewById(R.id.viewSale_toPDF);

        toPDFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TODO", Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            saleID = bundle.getInt(SALE_ID_KEY, 0);
        }

        new GetSaleAsyncTask().execute();

    }

    private class GetSaleAsyncTask extends AsyncTask<Void, Void, Void> {

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
                    .appendQueryParameter("ID", String.valueOf(saleID))
                    .appendQueryParameter("SessionID", sessionID);
            query = builder.build().getEncodedQuery();

            System.out.println("GET SALE QUERY -->" + query);

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Sales", "GetByID"));
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
                        final JSONObject jsonObject = outterObject.getJSONObject("Data");

                        int id = jsonObject.getInt("ID");
                        customerID = jsonObject.getInt("CustomerID");
                        saleProductsID = jsonObject.getInt("SaleProductsID");
                        double tax = jsonObject.getDouble("Tax");
                        long timedate = jsonObject.getLong("SaleTimeDate");

                        sale = new Sales(id, customerID, saleProductsID, tax, timedate);

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
            new GetCustomerAsyncTask().execute();
            new GetSalesProductsAsyncTask().execute();
        }
    }

    private class GetCustomerAsyncTask extends AsyncTask<Void, Void, Void> {

        private String responseData;

        @Override
        protected Void doInBackground(Void... voids) {


            String query;
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("ID", String.valueOf(customerID))
                    .appendQueryParameter("SessionID", sessionID);
            query = builder.build().getEncodedQuery();

            System.out.println("GET CUSTOMER QUERY -->" + query);

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Customers", "GetByID"));
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
                        final JSONObject jsonObject = outterObject.getJSONObject("Data");

                        final String customerName = jsonObject.getString("Name");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getActivity().setTitle("Sold to " + customerName);
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

    }

    public class GetSalesProductsAsyncTask extends AsyncTask<Void,Void,Void> {

        private String query;
        private String responseData;

        @Override
        protected Void doInBackground(Void... params) {

            saleProductsList = new ArrayList<>();

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("Limit", "0")
                    .appendQueryParameter("SessionID", sessionID);

            query = builder.build().getEncodedQuery();

            if (query == null) query = "";

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Saleproducts", "GetMultiple"));
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
                            int saleID = jsonObject.getInt("SaleID");
                            int productID = jsonObject.getInt("ProductID");
                            int quantitySold = jsonObject.getInt("QuantitySold");

                            SaleProducts p = new SaleProducts(id, saleID, productID, quantitySold);
                            saleProductsList.add(p);
                        }

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
            final SaleProductsAdapter adapter = new SaleProductsAdapter(getActivity(), sale, saleProductsList, productsList);

            float total = 0;
            for (int i = 0; i < saleProductsList.size(); i++) {
                double currentItemTotal = 0;
                if (saleProductsList.get(i).getSaleID() == sale.getID()) {
                    int quantity = saleProductsList.get(i).getQuantitySold();
                    double price = 0;
                    //Find product:
                    int productID = saleProductsList.get(i).getProductID();
                    for (int j = 0; j < productsList.size(); j++) {
                        if (productsList.get(j).getID() == productID) {
                            price = productsList.get(j).getPrice();
                            break;
                        }
                    }
                    currentItemTotal = quantity * price;
                }
                total += currentItemTotal;
            }
            final String totalString = "€" + String.format("%.2f", total);


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    productsListView.setAdapter(adapter);
                    totalTextView.setText(totalString);
                }
            });




        }
    }

}
