package com.easybusiness.eb_androidapp.UI.Fragments;


import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.easybusiness.eb_androidapp.AsyncTask.GetCustomersAsyncTask;
import com.easybusiness.eb_androidapp.Entities.Customers;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.Adapters.CustomerAdapter;
import com.easybusiness.eb_androidapp.UI.Dialogs;
import com.easybusiness.eb_androidapp.UI.MainActivity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewCustomersFragment extends Fragment {

    public static final String TAG = "ViewCustomerFragment";
    public static final String TITLE = "Customers";

    private SearchView searchView;
    private ListView customersListView;
    private Button addCustomerButton;
    private Button refreshButton;
    public static CustomerAdapter allCustomersAdapter;
    private View v;

    public ViewCustomersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_customers, container, false);

        customersListView = v.findViewById(R.id.customer_List_view);
        searchView = v.findViewById(R.id.customer_search_view);
        addCustomerButton = v.findViewById(R.id.add_customer_btn);
        refreshButton = v.findViewById(R.id.refresh_customers);

        //VIEW (Short click)
        customersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                MainActivity mainActivity = (MainActivity) getActivity();
                bundle.putString(ViewCustomerFragment.CUSTOMER_NAME_KEY, mainActivity.CUSTOMERS_DATA.get(i).getName());
                bundle.putString(ViewCustomerFragment.CUSTOMER_CITY, mainActivity.CUSTOMERS_DATA.get(i).getCity());
                bundle.putString(ViewCustomerFragment.CUSTOMER_ADDRESS, mainActivity.CUSTOMERS_DATA.get(i).getAddress());
                bundle.putString(ViewCustomerFragment.CUSTOMER_TELEPHONE, mainActivity.CUSTOMERS_DATA.get(i).getTelephone());
                //TODO Customers Products take from the DB
                bundle.putString(ViewCustomerFragment.CUSTOMER_COUNTRY, mainActivity.getCountryFromCountryID(mainActivity.CUSTOMERS_DATA.get(i).getCountryID()));
                //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY");

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
                MainActivity mainActivity = (MainActivity) getActivity();
                AlertDialog dialog = Dialogs.createDeleteDialog(getActivity(), view, "Customers", mainActivity.CUSTOMERS_DATA.get(i).getID(), mainActivity.CUSTOMERS_DATA.get(i).getName(), new ViewCustomersFragment());
                dialog.show();
                return true;
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "None");
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("SessionID", sessionID);
                String query = builder.build().getEncodedQuery();
                new GetCustomersAsyncTask(query, getActivity(), v).execute();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(TITLE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customersListView.setAdapter(allCustomersAdapter);

                final CustomerAdapter adapter = (CustomerAdapter) customersListView.getAdapter();
                ArrayList<Customers> searchedCustomers = new ArrayList<>();
                System.out.println("ADAPTER SIZE: " + adapter.getCount());
                for (int i = 0; i < adapter.getCount(); i++) {
                    Customers customers= adapter.getItem(i);
                    if (customers.getName() != null) {
                        if (customers.getName().toLowerCase().contains(newText.toLowerCase())) {
                            searchedCustomers.add(customers);
                        }
                    } else {
                        return false;
                    }
                }
                final CustomerAdapter newAdapter = new CustomerAdapter(getActivity(), searchedCustomers);
                customersListView.setAdapter(newAdapter);
                return true;
            }
        });

        addCustomerButton = v.findViewById(R.id.add_customer_btn);
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

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "None");

        Uri.Builder builder = new Uri.Builder().appendQueryParameter("SessionID", sessionID);
        String query = builder.build().getEncodedQuery();

        new GetCustomersAsyncTask(query, getActivity(), v).execute();
    }
}
