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
import android.widget.SearchView;

import com.easybusiness.eb_androidapp.AsyncTask.AsyncTasks;
import com.easybusiness.eb_androidapp.Entities.Supplies;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.Adapters.SuppliesAdapter;
import com.easybusiness.eb_androidapp.UI.Dialogs;
import com.easybusiness.eb_androidapp.UI.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ViewSuppliesFragment extends Fragment {

    public static final String TAG = "ViewSuppliesFragment";
    public static final String TITLE = "View Supplies";

    private SearchView searchView;
    private ListView supplyListView;
    private Button refreshButton;
    public static SuppliesAdapter allSuppliesAdapter;
    private ImageButton addSupplyBtn;
    View v;

    public ViewSuppliesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_supplies, container, false);

        supplyListView = v.findViewById(R.id.suppliesList);
        searchView = v.findViewById(R.id.supplies_searchview);
        addSupplyBtn = v.findViewById(R.id.addSupply);
        refreshButton = v.findViewById(R.id.refresh_supplies);

        //VIEW (Short click)
        supplyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                MainActivity mainActivity = (MainActivity) getActivity();
                bundle.putString(ViewSupplyFragment.SUPPLY_NAME_KEY, mainActivity.SUPPLY_DATA.get(i).getName());
                bundle.putInt(ViewSupplyFragment.SUPPLY_ID_KEY, mainActivity.SUPPLY_DATA.get(i).getID());
                bundle.putString(ViewSupplyFragment.SUPPLY_PRICE, String.valueOf(mainActivity.SUPPLY_DATA.get(i).getPrice()));
                bundle.putString(ViewSupplyFragment.SUPPLY_QUANTITY, String.valueOf(mainActivity.SUPPLY_DATA.get(i).getQuantity()));
                bundle.putString(ViewSupplyFragment.SUPPLY_SUPPLIER,String.valueOf(mainActivity.SUPPLY_DATA.get(i).getSupplierID()));

                Fragment newFragment = new ViewSupplyFragment();
                newFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                fragmentTransaction.replace(R.id.frame, newFragment, ViewSupplyFragment.TAG);
                fragmentTransaction.addToBackStack(newFragment.getTag());
                fragmentTransaction.commit();
            }
        });

        //DELETE (Long click)
        supplyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity mainActivity = (MainActivity) getActivity();
                AlertDialog dialog = Dialogs.createDeleteDialog(getActivity(), view, "Supplies", mainActivity.SUPPLY_DATA.get(i).getID(), mainActivity.SUPPLY_DATA.get(i).getName(), new ViewSuppliesFragment());
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
                new GetSuppliesAsyncTask(query, getActivity(), v).execute();
            }
        });


        //Make list view searchable:
        supplyListView.setTextFilterEnabled(true);
        //Searchview properties:
        setupSearchView();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle(TITLE);

        addSupplyBtn =v.findViewById(R.id.addSupply);
        addSupplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new AddSupplyFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                getActivity().setTitle(AddSupplyFragment.TITLE);
                fragmentTransaction.replace(R.id.frame, newFragment, AddSupplyFragment.TAG);
                fragmentTransaction.addToBackStack(newFragment.getTag());
                fragmentTransaction.commit();
                ((MainActivity) getActivity()).setMenuItemChecked(newFragment);
            }
        });

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "None");

        Uri.Builder builder = new Uri.Builder().appendQueryParameter("SessionID", sessionID);
        String query = builder.build().getEncodedQuery();

        new GetSuppliesAsyncTask(query, getActivity(), v).execute();

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
                    supplyListView.clearTextFilter();
                } else {
                    supplyListView.setFilterText(s);
                }
                return true;
            }
        });
        searchView.setQueryHint("Search...");
    }

    public class GetSuppliesAsyncTask extends AsyncTask<Void,Void,Void> {

        private String query;
        private String responseData;
        private Activity activity;
        private View view;
        ArrayList<Supplies> supplies = null;

        public GetSuppliesAsyncTask(String query, Activity activity, View view) {
            this.query = query;
            this.activity = activity;
            this.view = view;
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (query == null) query = "";

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(activity.getString(R.string.baseURL), "Supplies", "GetMultiple"));
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
                        supplies = new ArrayList<>();
                        JSONArray dataArray = outterObject.getJSONArray("Data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            int id = jsonObject.getInt("ID");
                            String name = jsonObject.getString("Name");
                            int supplierID = jsonObject.getInt("SupplierID");
                            double price = jsonObject.getDouble("Price");
                            int quantity = jsonObject.getInt("Quantity");
                            Supplies p = new Supplies(id, name, supplierID, quantity, (float) price);
                            supplies.add(p);
                        }

                        MainActivity mainActivity = (MainActivity) activity;
                        mainActivity.SUPPLY_DATA = supplies;

                        final ListView suppliesListview = activity.findViewById(R.id.suppliesList);
                        String [] items = new String[supplies.size()];
                        for (int i = 0; i < items.length; i++)
                            items[i] = supplies.get(i).getName();
                        final SuppliesAdapter suppliesAdapter = new SuppliesAdapter(activity, supplies);

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                suppliesListview.setAdapter(suppliesAdapter);
                            }
                        });


                    }
                    else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final android.app.AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(activity, title, message);
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
                            final android.app.AlertDialog alertDialog = AsyncTasks.createConnectionErrorDialog(activity);
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
