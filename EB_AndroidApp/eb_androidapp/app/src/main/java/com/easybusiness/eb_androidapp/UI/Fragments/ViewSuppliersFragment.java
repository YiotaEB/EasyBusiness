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

import com.easybusiness.eb_androidapp.AsyncTask.GetSuppliersAsyncTask;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSuppliersFragment extends Fragment {

    public static final String TAG = "ViewSuppliersFragment";
    public static final String TITLE = "Suppliers";


    public ViewSuppliersFragment() {
        // Required empty public constructor
        System.out.println("Constructed");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_suppliers, container, false);

//        Uri.Builder builder = new Uri.Builder().appendQueryParameter("Limit", String.valueOf(3));
//        String query = builder.build().getEncodedQuery();
//        new AddEmployee_GetCountriesAsyncTask(query, getActivity(), v).execute();

        Button addSupplierBtn = v.findViewById(R.id.add_supplier_btn);
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

        return v;
    }

}
