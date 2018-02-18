package com.easybusiness.eb_androidapp.UI.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easybusiness.eb_androidapp.AsyncTask.GetCountriesAsyncTask;
import com.easybusiness.eb_androidapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSuppliersFragment extends Fragment {

    public static final String TAG = "ViewSuppliersFragment";
    public static final String TITLE = "Suppliers";


    public ViewSuppliersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_suppliers, container, false);

        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("Limit", String.valueOf(3));
        String query = builder.build().getEncodedQuery();
        new GetCountriesAsyncTask(query, getActivity(), v).execute();

        return v;
    }

}
