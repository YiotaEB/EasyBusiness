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
public class ViewProductFragment extends Fragment {

    public static final String TAG = "ViewProductFragment";

    public static final String PRODUCT_ID_KEY = "product-id";
    public static final String PRODUCT_NAME_KEY = "product-name";
    public static final String PRODUCT_PRICE= "product-price";
    public static final String PRODUCT_QUANTITY_IN_STOCK= "product-quantity-in-stock";

    public static final String PRODUCT_SIZE_ID = "product-size-id";
    public static final String PRODUCT_TYPE_ID = "product-type-id";

    public static final String PRODUCT_SIZE = "product-size";
    public static final String PRODUCT_TYPE = "product-type";

    private View v;
    private int id;
    private String title = "Product";
    private String price = "";
    private String quantityInStock = "";
    private String size = "";
    private String type  = "";
    int productSize;
    int productType;

    private SharedPreferences sharedPreferences;
    private String sessionID;

    private TextView priceTextview;
    private TextView quantityTextview;
    private TextView sizeTextview;
    private TextView nameTextview;
    private TextView typeTextview;

    private ProgressBar progressBar;
    private View layout;

    private Button editButton;
    private Button toPDFButton;

    public ViewProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_product, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "None");

        progressBar = v.findViewById(R.id.view_product_progress);
        layout = v.findViewById(R.id.view_product_layout);

        priceTextview = v.findViewById(R.id.viewProduct_price_textview);
        quantityTextview = v.findViewById(R.id.viewProduct_quantity_textview);
        sizeTextview = v.findViewById(R.id.viewProduct_size_textview);
        nameTextview = v.findViewById(R.id.viewProduct_name);
        typeTextview = v.findViewById(R.id.viewProduct_type_textview);

        editButton = v.findViewById(R.id.viewProduct_editButton);
        toPDFButton = v.findViewById(R.id.viewProduct_ToPDFButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt(PRODUCT_ID_KEY, id);
                Fragment newFragment = new EditProductFragment();
                newFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                fragmentTransaction.replace(R.id.frame, newFragment, EditProductFragment.TAG);
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
            id = bundle.getInt(PRODUCT_ID_KEY);
            title = bundle.getString(PRODUCT_NAME_KEY);
            price = bundle.getString(PRODUCT_PRICE);
            quantityInStock = bundle.getString(PRODUCT_QUANTITY_IN_STOCK);
            size = bundle.getString(PRODUCT_SIZE);
            type = bundle.getString(PRODUCT_TYPE);
            productType = bundle.getInt(PRODUCT_TYPE_ID);
            productSize = bundle.getInt(PRODUCT_SIZE_ID);
        }

        getActivity().setTitle(title);
        priceTextview.setText("€" + price);
        quantityTextview.setText(quantityInStock);
        sizeTextview.setText(size);
        nameTextview.setText(title);
        typeTextview.setText(type);

        new GetProductAsyncTask().execute();
        new GetProductTypeAsyncTask().execute();
        new GetProductSizeAsyncTask().execute();

    }

    private class GetProductAsyncTask extends AsyncTask<Void, Void, Void> {

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
                    .appendQueryParameter("ID", String.valueOf(id))
                    .appendQueryParameter("SessionID", sessionID);
            query = builder.build().getEncodedQuery();

            System.out.println("GET PRODUCT QUERY -->" + query);

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Products", "GetByID"));
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
                        nameTextview.setText(name);
                        priceTextview.setText("€" + jsonObject.getString("Price"));
                        quantityTextview.setText(jsonObject.getString("QuantityInStock"));

                        productSize = jsonObject.getInt("ProductSizeID");
                        productType  = jsonObject.getInt("ProductTypeID");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getActivity().setTitle(name);
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

    private class GetProductTypeAsyncTask extends AsyncTask<Void,Void,Void> {

        private String responseData;

        @Override
        protected Void doInBackground(Void... params) {

            String query;

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("ID", String.valueOf(productType))
                    .appendQueryParameter("SessionID", sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, ""));
            query = builder.build().getEncodedQuery();

            if (query == null) query = "";

            System.out.println("PRODUCT TYPES QUERY: " + query);

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Producttypes", "GetByID"));
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
                                typeTextview.setText(name);
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

    private class GetProductSizeAsyncTask extends AsyncTask<Void,Void,Void> {

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
                    .appendQueryParameter("ID", String.valueOf(productSize))
                    .appendQueryParameter("SessionID", sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, ""));
            query = builder.build().getEncodedQuery();

            if (query == null) query = "";

            System.out.println("PRODUCT SIZES QUERY: " + query);

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Productsizes", "GetByID"));
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
                                sizeTextview.setText(name);
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

}
