package com.easybusiness.eb_androidapp.UI.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easybusiness.eb_androidapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCustomersFragment extends Fragment {

    public static final String TAG = "AddCustomersFragment";
    public static final String TITLE = "Add Customers";


    public AddCustomersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_customers, container, false);



        return view;
    }

}
