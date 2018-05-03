package com.easybusiness.eb_androidapp.UI.Fragments;


import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.easybusiness.eb_androidapp.AsyncTask.AsyncTasks;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.MainActivity;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSupplyFragment extends Fragment {

    public static final String TAG = "ViewSupplyFragment";

    public static final String SUPPLY_NAME_KEY = "supply-name";
    public static final String SUPPLY_ID_KEY = "supply-id";
    public static final String SUPPLY_PRICE= "supply-price";
    public static final String SUPPLY_QUANTITY= "supply-quantity";
    public static final String SUPPLY_SUPPLIER = "supply-supplier";

    private View v;
    private View layout;
    private ProgressBar progressBar;

    private String title = "Supply";
    private String price = "";
    private String quantityInStock = "";
    private String supplier = "";

    private SharedPreferences sharedPreferences;
    private String sessionID;
    int supplierID = 0;
    int supplyID = 0;

    private TextView priceTextview;
    private TextView quantityTextview;
    private TextView supplierTextview;
    private TextView nameTextview;

    private Button editButton;
    private Button toPDFButton;

    public ViewSupplyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_supply, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "None");

        layout = v.findViewById(R.id.view_supply_layout);
        progressBar = v.findViewById(R.id.view_supply_progress);

        priceTextview = v.findViewById(R.id.viewSupply_price_textview);
        quantityTextview = v.findViewById(R.id.viewSupply_quantity_textview);
        supplierTextview = v.findViewById(R.id.viewSupply_supplier_textview);
        nameTextview = v.findViewById(R.id.viewSupply_name);

        editButton = v.findViewById(R.id.viewSupply_editButton);
        toPDFButton = v.findViewById(R.id.viewSupply_ToPDFButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt(SUPPLY_ID_KEY, supplyID);
                bundle.putString(SUPPLY_NAME_KEY, title);
                bundle.putInt(SUPPLY_SUPPLIER, supplierID);
                Fragment newFragment = new EditSupplyFragment();
                newFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                fragmentTransaction.replace(R.id.frame, newFragment, EditSupplyFragment.TAG);
                fragmentTransaction.addToBackStack(newFragment.getTag());
                fragmentTransaction.commit();
            }
        });

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

        getActivity().setTitle(title);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            title = bundle.getString(SUPPLY_NAME_KEY);
            price = bundle.getString(SUPPLY_PRICE);
            quantityInStock = bundle.getString(SUPPLY_QUANTITY);
            supplier = bundle.getString(SUPPLY_SUPPLIER);
            supplyID = bundle.getInt(SUPPLY_ID_KEY);
            supplierID = Integer.parseInt(supplier);
        }

        getActivity().setTitle(title);

        new GetSupplyAsyncTask().execute();

    }

    private class GetSupplyAsyncTask extends AsyncTask<Void, Void, Void> {

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
                    .appendQueryParameter("SessionID", sessionID);
            query = builder.build().getEncodedQuery();

            System.out.println("GET SUPPLY QUERY -->" + query);

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
                    System.out.println(responseData);

                    final String status = outterObject.getString("Status");
                    final String title = outterObject.getString("Title");
                    final String message = outterObject.getString("Message");

                    if (status.equals(AsyncTasks.RESPONSE_OK)) {
                        final JSONObject jsonObject = outterObject.getJSONObject("Data");
                        final String name = jsonObject.getString("Name");
                        final String price = jsonObject.getString("Price");
                        final String quantity = jsonObject.getString("Quantity");

                        supplierID = jsonObject.getInt("SupplierID");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getActivity().setTitle(name);
                                nameTextview.setText(name);
                                priceTextview.setText("€" + price);
                                quantityTextview.setText(quantity);
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

        @Override
        protected void onPostExecute(Void aVoid) {
            new GetSupplySupplierAsyncTask().execute();
        }
    }

    private class GetSupplySupplierAsyncTask extends AsyncTask<Void,Void,Void> {

        private String responseData;

        @Override
        protected Void doInBackground(Void... params) {

            String query;

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("ID", String.valueOf(supplierID))
                    .appendQueryParameter("SessionID", sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, ""));
            query = builder.build().getEncodedQuery();

            if (query == null) query = "";

            System.out.println("SUPPLIERS QUERY: " + query);

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

                    System.out.println(responseData);
                    JSONObject outterObject = new JSONObject(responseData);
                    final String status = outterObject.getString("Status");
                    final String title = outterObject.getString("Title");
                    final String message = outterObject.getString("Message");

                    if (status.equals(AsyncTasks.RESPONSE_OK)) {
                        final JSONObject jsonObject = outterObject.getJSONObject("Data");
                        final String name = jsonObject.getString("Name");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                supplierTextview.setText(name);
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
