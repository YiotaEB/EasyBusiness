package com.easybusiness.eb_androidapp.UI.Fragments;


import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easybusiness.eb_androidapp.AsyncTask.AsyncTasks;
import com.easybusiness.eb_androidapp.Entities.Countries;
import com.easybusiness.eb_androidapp.Entities.Products;
import com.easybusiness.eb_androidapp.Entities.SaleProducts;
import com.easybusiness.eb_androidapp.Entities.Sales;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.MainActivity;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminHomeFragment extends Fragment {

    public static final String TAG = "AdminHomeFragment";
    public static final String TITLE = "Home";

    private GraphView latestSalesGraphView;
    private String companyName;
    private String companyAddress;
    private String companyTelephone;
    private String companyCity;
    private String companyCountry;
    private ArrayList<Countries> countriesList;
    private ArrayList<Sales> salesList;
    private ArrayList<SaleProducts> saleProductsList;
    private ArrayList<Datee> saleDates;
    private ArrayList<Double> saleTotals;
    private ArrayList<Products> productsList;
    private HashMap<Datee, Double> daySalesTotals;
    private HashMap<Long, Double> daySalesTotals2;

    private SharedPreferences sharedPreferences;
    private String sessionID;

    private ProgressBar progressBar;
    private CardView cardView;
    private TextView companyNameTextView;

    public AdminHomeFragment() {
        // Required empty public constructor
    }


    private class Datee {
        public int day;
        public int month;
        public int year;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_home, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "");

        latestSalesGraphView = v.findViewById(R.id.latest_sales_graph);
        progressBar = v.findViewById(R.id.admin_home_progress);
        cardView = v.findViewById(R.id.company_info_cardview);
        companyNameTextView = v.findViewById(R.id.companyName);


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(TITLE);

        new GetCountriesAsyncTask().execute();
        new GetSalesAsyncTask().execute();

    }

    private void processData() {

        //Initialize lists:
        saleDates = new ArrayList<>();
        saleTotals = new ArrayList<>();
        daySalesTotals = new HashMap<>();
        daySalesTotals2 = new HashMap<>();

        //Get each sale total and date:
        for (int i = 0; i < salesList.size(); i++) {
            double totalForSale = 0.0;

            long timestamp = salesList.get(i).getSaleTimeDate();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timestamp);
            Datee datee = new Datee();
            datee.day = cal.get(Calendar.DAY_OF_MONTH);
            datee.month = cal.get(Calendar.MONTH);
            datee.year = cal.get(Calendar.YEAR);

            //System.out.println(" DATE " + datee.day + " " + datee.month + " " + datee.year);
            saleDates.add(datee);

            for (int j = 0; j < saleProductsList.size(); j++) {
                double totalForSalesProduct = 0.0;
                int quantity = saleProductsList.get(j).getQuantitySold();
                if (saleProductsList.get(j).getSaleID() == salesList.get(i).getID()) {
                    double price = 0.0;
                    for (int k = 0; k < productsList.size(); k++) {
                        if (saleProductsList.get(j).getProductID() == productsList.get(k).getID()) {
                            price = productsList.get(k).getPrice();
                            break;
                        }
                    }
                    totalForSalesProduct = quantity * price;
                }
                totalForSale += totalForSalesProduct;
            }


            Datee dateFound = null;
            for (Map.Entry<Datee, Double> entry : daySalesTotals.entrySet()) {
                Datee e = entry.getKey();
                if (e.day == datee.day && e.month == datee.month && e.year == datee.year) {
                    dateFound = e;
                    break;
                }
            }

            //USE THIS
            Long dateFound2 = null;
            for (Map.Entry<Long, Double> entry : daySalesTotals2.entrySet()) {
                Long e = entry.getKey();
                if (e == timestamp) {
                    dateFound2 = e;
                    break;
                }
            }

            //If date already exists in map:

            if (dateFound != null) {
                double total = daySalesTotals.get(dateFound);
                total += totalForSale;
                daySalesTotals.put(dateFound, total);
            }

            //If date does not already exist in map:
            else {
                daySalesTotals.put(datee, totalForSale);
            }

            saleTotals.add(totalForSale);



        }

        int entries = 0;
        for (Map.Entry<Datee, Double> entry : daySalesTotals.entrySet()) {
            entries++;
            System.out.println("Date: " + entry.getKey().day + "/" + entry.getKey().month + " -- " + entry.getValue());
        }

        //Put data into the graph:
        latestSalesGraphView.setTitle("Latest sales");

        DataPoint[] datapoints = new DataPoint[entries];
        int currentpoint = 0;
        for (Map.Entry<Datee, Double> entry : daySalesTotals.entrySet()) {
            datapoints[currentpoint] = new DataPoint(currentpoint, entry.getValue());
            currentpoint++;
        }

        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(datapoints);
        series2.setAnimated(true);
        series2.setDrawValuesOnTop(true);
        series2.setColor(Color.BLUE);
        series2.setSpacing(2);
        latestSalesGraphView.addSeries(series2);

    }

    private class GetCompanyInformationAsyncTask extends AsyncTask<Void, Void, Void> {

        private String responseData;

        @Override
        protected Void doInBackground(Void... voids) {

            String query;
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("Limit", "0")
                    .appendQueryParameter("SessionID", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(MainActivity.PREFERENCE_SESSIONID, ""));
            query = builder.build().getEncodedQuery();

            System.out.println("GET COMPANY INFO QUERY -->" + query);

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Companyinformation", "GetMultiple"));
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

                        JSONArray dataArray = outterObject.getJSONArray("Data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);

                            companyName = jsonObject.getString("CompanyName");
                            companyAddress = jsonObject.getString("Address");
                            companyCity = jsonObject.getString("City");
                            companyTelephone = jsonObject.getString("Telephone");

                            int countryID = jsonObject.getInt("CountryID");

                            for (int j = 0; j < countriesList.size(); j++) {
                                if (countryID == countriesList.get(j).getID()) {
                                    companyCountry = countriesList.get(j).getName();
                                    break;
                                }
                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    companyNameTextView.setText(companyName);
                                    cardView.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            });

                        }

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

    private class GetCountriesAsyncTask extends AsyncTask<Void, Void, Void> {

        private String responseData;
        private String query;

        public GetCountriesAsyncTask() {
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("Limit", String.valueOf("0"))
                    .appendQueryParameter("SessionID", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(MainActivity.PREFERENCE_SESSIONID, ""));
            query = builder.build().getEncodedQuery();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cardView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                }
            });

            countriesList = new ArrayList<>();

            System.out.println("GET COUNTRY QUERY -->" + query);

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
                    System.out.println(responseData);

                    final String status = outterObject.getString("Status");
                    final String title = outterObject.getString("Title");
                    final String message = outterObject.getString("Message");

                    if (status.equals(AsyncTasks.RESPONSE_OK)) {
                        final JSONArray dataArray = outterObject.getJSONArray("Data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            int id = jsonObject.getInt("ID");
                            final String name = jsonObject.getString("Name");
                            Countries c = new Countries(id, name);
                            countriesList.add(c);
                        }

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
            new GetCompanyInformationAsyncTask().execute();
        }
    }

    public class GetSalesAsyncTask extends AsyncTask<Void,Void,Void> {

        private String query;
        private String responseData;

        @Override
        protected Void doInBackground(Void... params) {

            salesList = new ArrayList<>();

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("Limit", "0")
                    .appendQueryParameter("SessionID", sessionID);

            query = builder.build().getEncodedQuery();

            if (query == null) query = "";

            System.out.println("SALES QUERY -> " + query);

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Sales", "GetMultiple"));
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
                            int customerID = jsonObject.getInt("CustomerID");
                            int saleProductsID = jsonObject.getInt("SaleProductsID");
                            double tax = jsonObject.getDouble("Tax");
                            long saleTimeDate = jsonObject.getLong("SaleTimeDate");

                            Sales p = new Sales(id, customerID, saleProductsID, tax, saleTimeDate);
                            salesList.add(p);
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
            new GetSalesProductsAsyncTask().execute();
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
            processData();
        }
    }

}
