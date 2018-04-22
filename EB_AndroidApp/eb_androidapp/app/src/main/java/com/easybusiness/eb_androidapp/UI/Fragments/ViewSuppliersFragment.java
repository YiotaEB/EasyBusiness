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

import com.easybusiness.eb_androidapp.AsyncTask.GetSuppliersAsyncTask;
import com.easybusiness.eb_androidapp.Entities.Suppliers;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.Adapters.SupplierAdapter;
import com.easybusiness.eb_androidapp.UI.Dialogs;
import com.easybusiness.eb_androidapp.UI.MainActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSuppliersFragment extends Fragment {

    public static final String TAG = "ViewSuppliersFragment";
    public static final String TITLE = "Suppliers";


    private SearchView searchView;
    private ListView suppliersListView;
    private Button addSupplierBtn;
    private Button refreshButton;
    public static SupplierAdapter allSuppliersAdapter;
    private View v;


    public ViewSuppliersFragment() {
        // Required empty public constructor
        System.out.println("Constructed");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_view_suppliers, container, false);
        suppliersListView = v.findViewById(R.id.supplier_List_View);
        searchView = v.findViewById(R.id.supplier_search_view);
        addSupplierBtn = v.findViewById(R.id.add_supplier_btn);
        refreshButton = v.findViewById(R.id.refresh_suppliers);

        //VIEW (Short click)
        suppliersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                MainActivity mainActivity = (MainActivity) getActivity();
                bundle.putString(ViewSupplierFragment.SUPPLIER_NAME_KEY, mainActivity.SUPPLIER_DATA.get(i).getName());
                bundle.putString(ViewSupplierFragment.SUPPLIER_CITY, mainActivity.SUPPLIER_DATA.get(i).getCity());
                bundle.putString(ViewSupplierFragment.SUPPLIER_ADDRESS, mainActivity.SUPPLIER_DATA.get(i).getAddress());
                bundle.putString(ViewSupplierFragment.SUPPLIER_TELEPHONE, mainActivity.SUPPLIER_DATA.get(i).getTelephone());
                //TODO
                bundle.putString(ViewSupplierFragment.SUPPLIER_COUNTRY, String.valueOf(mainActivity.SUPPLIER_DATA.get(i).getCountryID()));
                //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY");

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
                MainActivity mainActivity = (MainActivity) getActivity();
                AlertDialog dialog = Dialogs.createDeleteDialog(getActivity(), view, "Suppliers", mainActivity.SUPPLIER_DATA.get(i).getID(), mainActivity.SUPPLIER_DATA.get(i).getName(), new ViewSuppliersFragment());
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
                new GetSuppliersAsyncTask(query, getActivity(), v).execute();
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
                suppliersListView.setAdapter(allSuppliersAdapter);

                final SupplierAdapter adapter = (SupplierAdapter) suppliersListView.getAdapter();
                ArrayList<Suppliers> searchedSuppliers = new ArrayList<>();
                System.out.println("ADAPTER SIZE: " + adapter.getCount());
                for (int i = 0; i < adapter.getCount(); i++) {
                    Suppliers suppliers= adapter.getItem(i);
                    if (suppliers.getName() != null) {
                        if (suppliers.getName().toLowerCase().contains(newText.toLowerCase())) {
                            searchedSuppliers.add(suppliers);
                        }
                    } else {
                        return false;
                    }
                }
                final SupplierAdapter newAdapter = new SupplierAdapter(getActivity(), searchedSuppliers);
                suppliersListView.setAdapter(newAdapter);
                return true;
            }
        });


        addSupplierBtn = v.findViewById(R.id.add_supplier_btn);
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

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "None");

        Uri.Builder builder = new Uri.Builder().appendQueryParameter("SessionID", sessionID);
        String query = builder.build().getEncodedQuery();

        new GetSuppliersAsyncTask(query, getActivity(), v).execute();
    }
}
