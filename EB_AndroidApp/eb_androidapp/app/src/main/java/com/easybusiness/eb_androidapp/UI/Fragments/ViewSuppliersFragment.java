package com.easybusiness.eb_androidapp.UI.Fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.Image;
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
import android.widget.Toast;

import com.easybusiness.eb_androidapp.AsyncTask.AsyncTasks;
import com.easybusiness.eb_androidapp.Entities.Suppliers;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.Adapters.SupplierAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSuppliersFragment extends Fragment {

    public static final String TAG = "ViewSuppliersFragment";
    public static final String TITLE = "Suppliers";

    private SharedPreferences sharedPreferences;
    private String sessionID;

    private ArrayList<Suppliers> suppliersList;

    private ProgressBar progressBar;
    private View layout;
    private SearchView searchView;
    private ListView suppliersListView;
    private ImageButton addSupplierBtn;
    private Button refreshButton;
    private Button printButton;
    private View v;


    public ViewSuppliersFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_view_suppliers, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "");

        progressBar = v.findViewById(R.id.view_suppliers_progress);
        layout = v.findViewById(R.id.view_suppliers_layout);
        suppliersListView = v.findViewById(R.id.suppliersList);
        searchView = v.findViewById(R.id.suppliers_search_view);
        addSupplierBtn = v.findViewById(R.id.addSuppliersButton);
        refreshButton = v.findViewById(R.id.refresh_suppliers);
        printButton = v.findViewById(R.id.print_suppliers_list_btn);

        //VIEW (Short click)
        suppliersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putInt(ViewSupplierFragment.SUPPLIER_ID_KEY, suppliersList.get(i).getID());
                bundle.putString(ViewSupplierFragment.SUPPLIER_NAME_KEY, suppliersList.get(i).getName());
                bundle.putString(ViewSupplierFragment.SUPPLIER_CITY, suppliersList.get(i).getCity()); //TODO REMOVE
                bundle.putString(ViewSupplierFragment.SUPPLIER_ADDRESS, suppliersList.get(i).getAddress()); //TODO REMOVE
                bundle.putString(ViewSupplierFragment.SUPPLIER_TELEPHONE, suppliersList.get(i).getTelephone()); //TODO REMOVE
                bundle.putString(ViewSupplierFragment.SUPPLIER_COUNTRY, String.valueOf(suppliersList.get(i).getCountryID())); //TODO REMOVE
                //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY"); //TODO REMOVE

                Fragment newFragment = new ViewSupplierFragment();
                newFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                fragmentTransaction.replace(R.id.frame, newFragment, ViewSupplierFragment.TAG);
                fragmentTransaction.addToBackStack(newFragment.getTag());
                fragmentTransaction.commit();
            }
        });

        //DELETE (Long click)
        suppliersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog dialog = Dialogs.createDeleteDialog(getActivity(), view, "Suppliers", suppliersList.get(i).getID(), suppliersList.get(i).getName(), new ViewSuppliersFragment());
                dialog.show();
                return true;
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetSuppliersAsyncTask().execute();
            }
        });

        addSupplierBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new AddSuppliersFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                getActivity().setTitle(AddSuppliersFragment.TITLE);
                fragmentTransaction.replace(R.id.frame, newFragment, AddSuppliersFragment.TAG);
                fragmentTransaction.addToBackStack(newFragment.getTag());
                fragmentTransaction.commit();
                ((MainActivity) getActivity()).setMenuItemChecked(newFragment);
            }
        });

        suppliersListView.setTextFilterEnabled(true);
        setupSearchView();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(TITLE);

        new GetSuppliersAsyncTask().execute();
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
                    suppliersListView.clearTextFilter();
                } else {
                    suppliersListView.setFilterText(s);
                }
                return true;
            }
        });
        searchView.setQueryHint("Search...");
    }


    public class GetSuppliersAsyncTask extends AsyncTask<Void,Void,Void> {

        private String query;
        private String responseData;

        public GetSuppliersAsyncTask() {
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("SessionID", sessionID);
            query = builder.build().getEncodedQuery();
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (query == null) query = "";

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Suppliers", "GetMultiple"));
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
                        suppliersList = new ArrayList<>();
                        JSONArray dataArray = outterObject.getJSONArray("Data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            String name = jsonObject.getString("Name");
                            String telephone = jsonObject.getString("Telephone");
                            Suppliers p = new Suppliers(0, name, 0, " ", telephone, "");
                            suppliersList.add(p);

                            final SupplierAdapter supplierAdapter = new SupplierAdapter(getActivity(), suppliersList);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    suppliersListView.setAdapter(supplierAdapter);
                                }
                            });

                        }


                    }
                    else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
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
