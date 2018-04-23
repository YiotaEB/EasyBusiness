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
import android.widget.TextView;
import android.widget.Toast;


import com.easybusiness.eb_androidapp.AsyncTask.GetCountriesAsyncTask;
import com.easybusiness.eb_androidapp.Entities.Users;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.MainActivity;

import java.util.Date;

import static com.easybusiness.eb_androidapp.UI.MainActivity.PREFERENCE_SESSIONID;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewEmployeeFragment extends Fragment {

    public static final String TAG = "ViewEmployeeFragment";

    public static final String EMPLOYEE_KEY = "employee-key";

    private View v;
    private String title = "Employee";
    public static Users user;

    public static TextView positionTextview;
    public static TextView usernameTextview;
    public static TextView cityTextview;
    public static TextView addressTextview;
    public static TextView countryTextview;
    public static TextView dateHiredTextview;
    public static TextView nameTextview;
    public static TextView telephoneTextview;

    private Button editButton;
    private Button toPDFButton;

    public ViewEmployeeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_employee, container, false);

        positionTextview = v.findViewById(R.id.viewEmployee_position_textview);
        usernameTextview = v.findViewById(R.id.viewEmployee_username_textview);
        cityTextview = v.findViewById(R.id.viewEmployee_city_textview);
        addressTextview = v.findViewById(R.id.viewEmployee_address_textview);
        countryTextview = v.findViewById(R.id.viewEmployee_country_textview);
        dateHiredTextview = v.findViewById(R.id.viewEmployee_datehired_textview);
        nameTextview = v.findViewById(R.id.viewEmployee_name);
        telephoneTextview = v.findViewById(R.id.viewEmployee_telephone_textview);

        editButton = v.findViewById(R.id.viewEmployee_editButton);
        toPDFButton = v.findViewById(R.id.viewEmployee_ToPDFButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(EMPLOYEE_KEY, user);

                Fragment newFragment = new EditEmployeesFragment();
                newFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                fragmentTransaction.replace(R.id.frame, newFragment, EditEmployeesFragment.TAG);
                fragmentTransaction.addToBackStack(newFragment.getTag());
                fragmentTransaction.commit();
            }
        });

        toPDFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TODO", Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        final String SESSION_ID = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(PREFERENCE_SESSIONID, "");

        new GetCountriesAsyncTask("SessionID=" + SESSION_ID, getActivity(), v).execute();


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            user = (Users) bundle.getSerializable(EMPLOYEE_KEY);
            title = user.getFirstname() + " " + user.getLastname();
        }

        MainActivity activity = ((MainActivity) getActivity());
        String positionName = activity.getUserLevelNameFromID(user.getUserLevelID());
        String countryName = activity.getCountryFromCountryID(user.getCountryID());
        String formattedDate = MainActivity.DATE_FORMAT.format(new Date(user.getDateHired()));

        getActivity().setTitle(title);
        usernameTextview.setText(user.getUsername());
        positionTextview.setText(positionName);
        cityTextview.setText(user.getCity());
        addressTextview.setText(user.getAddress());
        countryTextview.setText(countryName);
        dateHiredTextview.setText(formattedDate);
        nameTextview.setText(title);
        telephoneTextview.setText(user.getTelephone());

        //new GetEmployeeAsyncTask("UserID=" + user.getUserID() + "&SessionID=" + SESSION_ID, getActivity(), v).execute();

    }

}
