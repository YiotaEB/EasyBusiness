package com.easybusiness.eb_androidapp.UI.Fragments;


import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.easybusiness.eb_androidapp.AsyncTask.GetEmployeesAsyncTask;
import com.easybusiness.eb_androidapp.Entities.Users;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.Adapters.EmployeeAdapter;
import com.easybusiness.eb_androidapp.UI.Dialogs;
import com.easybusiness.eb_androidapp.UI.MainActivity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewEmployeesFragment extends Fragment {

    public static final String TAG = "ViewEmployeesFragment";
    public static final String TITLE = "Employees";

    private SearchView searchView;
    private ListView employeesListView;
    private Button addEmployeeBtn;
    private Button refreshButton;
    public static EmployeeAdapter allEmployeesAdapter;
    private View v;


    public ViewEmployeesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_employees, container, false);

        employeesListView = v.findViewById(R.id.employees_list_view);
        searchView = v.findViewById(R.id.employees_search_view);
        addEmployeeBtn = v.findViewById(R.id.add_employees_btn);
        refreshButton = v.findViewById(R.id.refresh_employees);

        //VIEW (Short click)
        employeesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                MainActivity mainActivity = (MainActivity) getActivity();
                bundle.putString(ViewEmployeeFragment.EMPLOYEE_NAME_KEY, mainActivity.EMPLOYEES_DATA.get(i).getFirstname() + " " + mainActivity.EMPLOYEES_DATA.get(i).getLastname());
                bundle.putString(ViewEmployeeFragment.EMPLOYEE_POSITION, mainActivity.getUserLevelNameFromID(mainActivity.EMPLOYEES_DATA.get(i).getUserLevelID()));
                bundle.putString(ViewEmployeeFragment.EMPLOYEE_USERNAME, mainActivity.EMPLOYEES_DATA.get(i).getUsername());
                bundle.putString(ViewEmployeeFragment.EMPLOYEE_CITY, mainActivity.EMPLOYEES_DATA.get(i).getCity());
                bundle.putString(ViewEmployeeFragment.EMPLOYEE_ADDRESS, mainActivity.EMPLOYEES_DATA.get(i).getAddress());
                bundle.putString(ViewEmployeeFragment.EMPLOYEE_TELEPHONE, mainActivity.EMPLOYEES_DATA.get(i).getTelephone());
                //TODO SIMILAR TO POSITIONS, GET THEM FROM DB:


                bundle.putString(ViewEmployeeFragment.EMPLOYEE_COUNTRY,  String.valueOf(mainActivity.getCountryFromCountryID(mainActivity.EMPLOYEES_DATA.get(i).getCountryID())));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY");
                Date date = new Date(mainActivity.EMPLOYEES_DATA.get(i).getDateHired());
                bundle.putString(ViewEmployeeFragment.EMPLOYEE_DATEHIRED, simpleDateFormat.format(date));

                Fragment newFragment = new ViewEmployeeFragment();
                newFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                fragmentTransaction.replace(R.id.frame, newFragment, ViewEmployeeFragment.TAG);
                fragmentTransaction.addToBackStack(newFragment.getTag());
                fragmentTransaction.commit();
            }
        });

        //DELETE (Long click)
        employeesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity mainActivity = (MainActivity) getActivity();
                AlertDialog dialog = Dialogs.createDeleteDialog(getActivity(), view, "Users", mainActivity.EMPLOYEES_DATA.get(i).getUserID(), mainActivity.EMPLOYEES_DATA.get(i).getFirstname());
                dialog.show();
                return true;
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "None");
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("SessionID", sessionID);
                String query = builder.build().getEncodedQuery();
                new GetEmployeesAsyncTask(query, getActivity(), v).execute();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(TITLE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                employeesListView.setAdapter(allEmployeesAdapter);

                final EmployeeAdapter adapter = (EmployeeAdapter) employeesListView.getAdapter();
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
                employeesListView.setAdapter(newAdapter);
                return true;
            }
        });

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

        new GetEmployeesAsyncTask(query, getActivity(), v).execute();

    }
}
