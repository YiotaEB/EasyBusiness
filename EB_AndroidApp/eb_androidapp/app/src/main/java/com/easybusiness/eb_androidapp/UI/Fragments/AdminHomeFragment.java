package com.easybusiness.eb_androidapp.UI.Fragments;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminHomeFragment extends Fragment {

    public static final String TAG = "AdminHomeFragment";
    public static final String TITLE = "Home";


    public AdminHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_home, container, false);

        TextView welcomeTextView = v.findViewById(R.id.welcome);

        String firstname = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(MainActivity.PREFERENCE_FIRSTNAME, "no");
        String lastname = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(MainActivity.PREFERENCE_LASTNAME, "no");
        String fullname = firstname + " " + lastname;
        welcomeTextView.setText("Welcome " + fullname);

        return v;
    }

}
