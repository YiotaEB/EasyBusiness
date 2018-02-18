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
public class AddSuppliersFragment extends Fragment {

    public static final String TAG = "AddSuppliersFragment";
    public static final String TITLE = "Add Suppliers";


    public AddSuppliersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_suppliers, container, false);
    }

}
