package com.easybusiness.eb_androidapp.UI.Fragments;


import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.easybusiness.eb_androidapp.AsyncTask.ViewCustomers_SelectProductsAsyncTask;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectProductsFragment extends Fragment {

    public static final String TAG = "SelectProductsFragment";
    public static final String TITLE = "Select Products";

    View view;

    public SelectProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_select_products, container, false);

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "None");

        Uri.Builder builder = new Uri.Builder().appendQueryParameter("SessionID", sessionID);
        String query = builder.build().getEncodedQuery();

        new ViewCustomers_SelectProductsAsyncTask(query, getActivity(), view).execute();

    }

}
