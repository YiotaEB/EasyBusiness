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
import android.widget.ListView;
import android.widget.SearchView;

import com.easybusiness.eb_androidapp.AsyncTask.GetEmployeeAsyncTask;
import com.easybusiness.eb_androidapp.Entities.Users;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.Adapters.EmployeeAdapter;
import com.easybusiness.eb_androidapp.UI.MainActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewEmployeesFragment extends Fragment {

    public static final String TAG = "ViewEmployeesFragment";
    public static final String TITLE = "Employees";

    private SearchView searchView;
    public static EmployeeAdapter allEmployeesAdapter;


    public ViewEmployeesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_employees, container, false);

        final ListView employeesListview = v.findViewById(R.id.employees_list_view);

        searchView = v.findViewById(R.id.employees_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                employeesListview.setAdapter(allEmployeesAdapter);

                final EmployeeAdapter adapter = (EmployeeAdapter) employeesListview.getAdapter();
                ArrayList<Users> searchedUsers = new ArrayList<>();
                System.out.println("ADAPTER SIZE: " + adapter.getCount());
                for (int i = 0; i < adapter.getCount(); i++) {
                    Users user = adapter.getItem(i);
                    if (user.getFirstname() != null && user.getLastname() != null && user.getUsername() != null) {
                        if (user.getFirstname().toLowerCase().contains(newText.toLowerCase())) {
                            searchedUsers.add(user);
                        } else if (user.getLastname().toLowerCase().contains(newText.toLowerCase())) {
                            searchedUsers.add(user);
                        } else if (user.getUsername().toLowerCase().contains(newText.toLowerCase())) {
                            searchedUsers.add(user);
                        }
                    } else {
                        return false;
                    }
                }
                final EmployeeAdapter newAdapter = new EmployeeAdapter(getActivity(), searchedUsers);
                employeesListview.setAdapter(newAdapter);
                return true;
            }
        });

        Button addEmployeeBtn = v.findViewById(R.id.add_employees_btn);
        addEmployeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new AddEmployeesFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                getActivity().setTitle(AddEmployeesFragment.TITLE);
                fragmentTransaction.replace(R.id.frame, newFragment, AddEmployeesFragment.TAG);
                fragmentTransaction.addToBackStack(newFragment.getTag());
                fragmentTransaction.commit();
                ((MainActivity) getActivity()).setMenuItemChecked(newFragment);
            }
        });

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "None");

        Uri.Builder builder = new Uri.Builder().appendQueryParameter("SessionID", sessionID);
        String query = builder.build().getEncodedQuery();

        new GetEmployeeAsyncTask(query, getActivity(), v).execute();

        return v;
    }

}
