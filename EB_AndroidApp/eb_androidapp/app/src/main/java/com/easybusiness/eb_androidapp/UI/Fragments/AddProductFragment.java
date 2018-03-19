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
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.easybusiness.eb_androidapp.AsyncTask.AsyncTasks;
import com.easybusiness.eb_androidapp.Entities.Countries;
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
    public static final String TITLE = "AddProducts";

    private ArrayList<ProductTypes> productType;

    private View v;
    private Spinner productTypeSpinner;
    private EditText productNameEditText;
    private Spinner productQuantitySpinner;
    private EditText productPriceEditText;
    private Button addProductBtn;
    private SharedPreferences sharedPreferences;

    private ListView productSizeList;


    public AddProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_product, container, false);


        addProductBtn = v.findViewById(R.id.add_product_Btn);
        productQuantitySpinner = v.findViewById(R.id.product_quantity_spinner);
        productPriceEditText = v.findViewById(R.id.product_price_edittext);
        productNameEditText = v.findViewById(R.id.product_name_edittext);
        productTypeSpinner = v.findViewById(R.id.product_type_spinner);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        getRequiredFields();

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput()) {
                    new AddProductAsyncTask(
                            getActivity(),
                            v,
                            sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, ""),
                            productNameEditText.getText().toString(),
                            productPriceEditText.getText().toString(),
                            productType.get(productTypeSpinner.getSelectedItemPosition()).getID()
                    ).execute();
                }
            }
        });

    }

    private boolean checkInput() {

        boolean flag = true;

        //Check name:
        if (productNameEditText.getText().toString().trim().isEmpty()) {
            productNameEditText.setError("Please provide a valid answer.");
            flag = false;
        }
        else {
            productNameEditText.setError(null);
        }

        //Check price
        if (productPriceEditText.getText().toString().trim().isEmpty()) {
            productPriceEditText.setError("Please provide a valid answer.");
            flag = false;
        }
        else {
            productPriceEditText.setError(null);
        }

        return flag;
    }

    private void getRequiredFields() {
        new GetProductTypeAsyncTask("", getActivity(), v).execute();

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("SessionID", sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, ""));

        //String userLevelsQuery = builder.build().getEncodedQuery();

        // new AddEmployeesFragment.GetUserLevelsAsyncTask(userLevelsQuery, getActivity(), v).execute();
    }

    private class GetProductTypeAsyncTask extends AsyncTask<Void,Void,Void> {

        private String query;
        private String responseData;
        private Activity activity;
        private View view;
        private ArrayAdapter<String> adapter;

        public GetProductTypeAsyncTask(String query, Activity activity, View view) {
            this.query = query;
            this.activity = activity;
            this.view = view;
            productType = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (query == null) query = "";

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(activity.getString(R.string.baseURL), "ProductTypes", "GetMultiple"));
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
                        ArrayList<String> productName = new ArrayList<>();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            String name = jsonObject.getString("Name");
                            int id = jsonObject.getInt("ID");
                            ProductTypes c = new ProductTypes(id, name);
                            productType.add(c);
                            productName.add(c.getName());
                        }

                        adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, productName);

                    }
                    //ERROR:
                    else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final AlertDialog alertDialog = AsyncTasks.createUnknownErrorDialog(activity, title, message);
                                alertDialog.show();
                            }
                        });
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Spinner productTypesSpinner = view.findViewById(R.id.product_type_spinner);
                            productTypesSpinner.setAdapter(adapter);
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

    private class AddProductAsyncTask extends AsyncTask<Void,Void,Void> {

        private String name;
        private String query;
        private String responseData;
        private Activity activity;
        private View view;
        private ArrayAdapter<String> adapter;

        public AddProductAsyncTask(Activity activity, View view, String sessionID,  String name, String price, int productTypeID) {

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("ID", "0")
                    .appendQueryParameter("SessionID", sessionID)
                    .appendQueryParameter("Name", name)
                    .appendQueryParameter("Price",price)
                    //.appendQueryParameter("QuantityInStock","0")
                    //.appendQueryParameter("ProductSizeID", "0")
                    .appendQueryParameter("ProductTypeID", String.valueOf(productTypeID));
                    //.appendQueryParameter("ProductSuppliesID", "0");

            this.name = name;
            this.query = builder.build().getEncodedQuery();
            System.out.println(query);
            this.activity = activity;
            this.view = view;
            productType = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {



            try {
                URL url = new URL(AsyncTasks.encodeForAPI(activity.getString(R.string.baseURL), "Products", "Create"));
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
                                Toast.makeText(activity, "Product '" + name + "' created.", Toast.LENGTH_LONG).show();
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
                                final AlertDialog alertDialog = AsyncTasks.createUnknownErrorDialog(activity, title, message);
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