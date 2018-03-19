package com.easybusiness.eb_androidapp.UI.Fragments.TabFragments;


import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.easybusiness.eb_androidapp.AsyncTask.GetProductsAsyncTask;
import com.easybusiness.eb_androidapp.AsyncTask.GetSuppliesAsyncTask;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.Fragments.AddProductFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.AddSupplyFragment;
import com.easybusiness.eb_androidapp.UI.MainActivity;

public class SuppliesTabFragment extends Fragment {

    public static final String TAG = "ViewSuppliesFragment";
    public static final String TITLE = "View Supplies";

    View v;

    public SuppliesTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_tab_supplies, container, false);

        ImageButton addSupplyBtn =v.findViewById(R.id.addSupply);
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



        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "None");

        Uri.Builder builder = new Uri.Builder().appendQueryParameter("SessionID", sessionID);
        String query = builder.build().getEncodedQuery();

        new GetSuppliesAsyncTask(query, getActivity(), v).execute();

    }
}
