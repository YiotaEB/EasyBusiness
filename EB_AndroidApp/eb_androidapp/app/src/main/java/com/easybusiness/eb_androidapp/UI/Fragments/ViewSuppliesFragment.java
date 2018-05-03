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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import com.easybusiness.eb_androidapp.AsyncTask.GetSuppliesAsyncTask;
import com.easybusiness.eb_androidapp.Entities.Supplies;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.Adapters.SuppliesAdapter;
import com.easybusiness.eb_androidapp.UI.Dialogs;
import com.easybusiness.eb_androidapp.UI.MainActivity;

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




        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle(TITLE);

        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                supplyListView.setAdapter(allSuppliesAdapter);

                final SuppliesAdapter adapter = (SuppliesAdapter) supplyListView.getAdapter();
                ArrayList<Supplies> searchedSupplies= new ArrayList<>();
                System.out.println("ADAPTER SIZE: " + adapter.getCount());
                for (int i = 0; i < adapter.getCount(); i++) {
                    Supplies supplies= adapter.getItem(i);
                    if (supplies.getName() != null) {
                        if (supplies.getName().toLowerCase().contains(newText.toLowerCase())) {
                            searchedSupplies.add(supplies);
                        }
                    } else {
                        return false;
                    }
                }
                final SuppliesAdapter newAdapter = new SuppliesAdapter(getActivity(), searchedSupplies);
                supplyListView.setAdapter(newAdapter);

                return true;
            }
        });

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
}
