package com.easybusiness.eb_androidapp.UI.Fragments;


import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.easybusiness.eb_androidapp.AsyncTask.AsyncTasks;
import com.easybusiness.eb_androidapp.Entities.Customers;
import com.easybusiness.eb_androidapp.Other.AppMode;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.Adapters.CustomerAdapter;
import com.easybusiness.eb_androidapp.UI.Dialogs;
import com.easybusiness.eb_androidapp.UI.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewCustomersFragment extends Fragment {

    public static final String TAG = "ViewCustomerFragment";
    public static final String TITLE = "Customers";

    private SharedPreferences sharedPreferences;
    private String sessionID;

    private ArrayList<Customers> customersList;

    private ProgressBar progressBar;
    private View layout;
    private SearchView searchView;
    private ListView customersListView;
    private ImageButton addCustomerButton;
    private Button refreshButton;
    private View v;

    public ViewCustomersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_customers, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "");

        progressBar = v.findViewById(R.id.view_customers_progress);
        layout = v.findViewById(R.id.view_customers_layout);
        customersListView = v.findViewById(R.id.custoemr_List_view);
        searchView = v.findViewById(R.id.customer_search_view);
        addCustomerButton = v.findViewById(R.id.add_customer_btn);
        refreshButton = v.findViewById(R.id.refresh_customers);

        MainActivity activity = (MainActivity) getActivity();
        if (activity.getAppMode() == AppMode.MODE_USER) {
            addCustomerButton.setVisibility(View.GONE);
        }

        addCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new AddCustomersFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                getActivity().setTitle(AddCustomersFragment.TITLE);
                fragmentTransaction.replace(R.id.frame, newFragment, AddCustomersFragment.TAG);
                fragmentTransaction.addToBackStack(newFragment.getTag());
                fragmentTransaction.commit();
                ((MainActivity) getActivity()).setMenuItemChecked(newFragment);
            }
        });

        //VIEW (Short click)
        customersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putInt(ViewCustomerFragment.CUSTOMER_ID_KEY, customersList.get(i).getID());
                bundle.putString(ViewCustomerFragment.CUSTOMER_NAME_KEY, customersList.get(i).getName());

                Fragment newFragment = new ViewCustomerFragment();
                newFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                fragmentTransaction.replace(R.id.frame, newFragment, ViewCustomerFragment.TAG);
                fragmentTransaction.addToBackStack(newFragment.getTag());
                fragmentTransaction.commit();
            }
        });

        //DELETE (Long click)
        customersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog dialog = Dialogs.createDeleteDialog(getActivity(), view, "Customers", customersList.get(i).getID(), customersList.get(i).getName(), new ViewCustomersFragment());
                dialog.show();
                return true;
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetCustomersAsyncTask().execute();
            }
        });

        customersListView.setTextFilterEnabled(true);
        setupSearchView();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(TITLE);

        new GetCustomersAsyncTask().execute();
    }

    private void setupSearchView() {
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    customersListView.clearTextFilter();
                } else {
                    customersListView.setFilterText(s);
                }
                return true;
            }
        });
        searchView.setQueryHint("Search...");
    }

    public class GetCustomersAsyncTask extends AsyncTask<Void, Void, Void> {

        private String query;
        private String responseData;

        @Override
        protected Void doInBackground(Void... params) {

            customersList = new ArrayList<>();

            String query;
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("Limit", String.valueOf("0"))
                    .appendQueryParameter("SessionID", sessionID);
            query = builder.build().getEncodedQuery();

            System.out.println("GET CUSTOMERS QUERY -->" + query);

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
                            String telephone = jsonObject.getString("Telephone");
                            int countryID = jsonObject.getInt("CountryID");
                            String city = jsonObject.getString("City");
                            String address = jsonObject.getString("Address");
                            int customerProductsID = jsonObject.getInt("CustomerProductsID");
                            Customers p = new Customers(id, name, countryID, city, address, telephone, customerProductsID);
                            customersList.add(p);
                        }

                        final CustomerAdapter customersAdapter = new CustomerAdapter(getActivity(), customersList);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                customersListView.setAdapter(customersAdapter);
                            }
                        });


                    } else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
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
