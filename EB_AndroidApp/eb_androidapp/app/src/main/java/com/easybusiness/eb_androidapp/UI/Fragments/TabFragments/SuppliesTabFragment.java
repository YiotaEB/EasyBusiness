package com.easybusiness.eb_androidapp.UI.Fragments.TabFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easybusiness.eb_androidapp.R;

public class SuppliesTabFragment extends Fragment {

    public static final String TAG = "ViewSuppliesFragment";
    public static final String TITLE = "View Supplies";

    public SuppliesTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_supplies, container, false);



        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setTitle(TITLE);
    }
}
