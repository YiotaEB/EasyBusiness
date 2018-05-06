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
import com.easybusiness.eb_androidapp.Entities.Countries;
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
public class ViewCustomerFragment extends Fragment {

    public static final String TAG = "ViewCustomerFragment";

    public static final String CUSTOMER_ID_KEY = "customer-id";
    public static final String CUSTOMER_NAME_KEY = "customer-name";

    private View v;
    private String title = "Customer";

    private int customerID = 0;
    private SharedPreferences sharedPreferences;
    private String sessionID;
    private ArrayList<Countries> countriesList;

    private ProgressBar progressBar;
    private View layout;

    private TextView cityTextview;
    private TextView addressTextview;
    private TextView countryTextview;
    private TextView nameTextview;
    private TextView telephoneTextview;
    private Button editButton;
    private Button toPDFButton;

    public ViewCustomerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_customer, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "");

        progressBar = v.findViewById(R.id.view_customer_progress);
        layout = v.findViewById(R.id.view_customer_layout);

        cityTextview = v.findViewById(R.id.viewCustomer_city_textview);
        addressTextview = v.findViewById(R.id.viewCustomer_address_textview);
        countryTextview = v.findViewById(R.id.viewCustomer_country_textview);
        nameTextview = v.findViewById(R.id.viewCustomer_name);
        telephoneTextview = v.findViewById(R.id.viewCustomer_telephone_textview);

        editButton = v.findViewById(R.id.viewCustomer_editButton);
        toPDFButton = v.findViewById(R.id.viewCustomer_ToPDFButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt(ViewCustomerFragment.CUSTOMER_ID_KEY, customerID);
                Fragment newFragment = new EditCustomerFragment();
                newFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                fragmentTransaction.replace(R.id.frame, newFragment, EditCustomerFragment.TAG);
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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            customerID = bundle.getInt(CUSTOMER_ID_KEY);
            title = bundle.getString(CUSTOMER_NAME_KEY);
        }

        new GetCountriesAsyncTask().execute();

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
                        final int id = jsonObject.getInt("ID");
                        final String name = jsonObject.getString("Name");
                        final int countryID = jsonObject.getInt("CountryID");
                        final String address = jsonObject.getString("Address");
                        final String telephone = jsonObject.getString("Telephone");
                        final String city = jsonObject.getString("City");
                        final int customerProductsID = jsonObject.getInt("CustomerProductsID");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getActivity().setTitle(name);
                                nameTextview.setText(name);
                                telephoneTextview.setText(telephone);
                                cityTextview.setText(city);
                                addressTextview.setText(address);

                                String countryName = "";
                                for (int i = 0; i < countriesList.size(); i++) {
                                    if (countriesList.get(i).getID() == countryID) {
                                        countryName = countriesList.get(i).getName();
                                        break;
                                    }
                                }
                                countryTextview.setText(countryName);

                                progressBar.setVisibility(View.GONE);
                                layout.setVisibility(View.VISIBLE);
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

    private class GetCountriesAsyncTask extends AsyncTask<Void, Void, Void> {

        private String responseData;
        private String query;

        public GetCountriesAsyncTask() {
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("Limit", String.valueOf("0"))
                    .appendQueryParameter("SessionID", sessionID);
            query = builder.build().getEncodedQuery();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    layout.setVisibility(View.GONE);
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
            new GetCustomerAsyncTask().execute();
        }
    }

}
