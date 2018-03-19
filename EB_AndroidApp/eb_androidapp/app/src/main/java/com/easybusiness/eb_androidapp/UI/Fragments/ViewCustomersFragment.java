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

import com.easybusiness.eb_androidapp.AsyncTask.GetCustomersAsyncTask;
import com.easybusiness.eb_androidapp.AsyncTask.GetProductsAsyncTask;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewCustomersFragment extends Fragment {

    public static final String TAG = "ViewCustomersFragment";
    public static final String TITLE = "Customers";


    public ViewCustomersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_customers, container, false);

        Button addCustomerButton = v.findViewById(R.id.add_customer_btn);
        addCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new AddCustomersFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                getActivity().setTitle(AddCustomersFragment.TITLE);
                fragmentTransaction.replace(R.id.frame, newFragment, AddCustomersFragment.TAG);
                fragmentTransaction.addToBackStack(newFragment.getTag());
                fragmentTransaction.commit();
                ((MainActivity) getActivity()).setMenuItemChecked(newFragment);
            }
        });

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "None");

        Uri.Builder builder = new Uri.Builder().appendQueryParameter("SessionID", sessionID);
        String query = builder.build().getEncodedQuery();

        new GetCustomersAsyncTask(query, getActivity(), v).execute();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getActionBar().setTitle(TITLE);
    }
}
