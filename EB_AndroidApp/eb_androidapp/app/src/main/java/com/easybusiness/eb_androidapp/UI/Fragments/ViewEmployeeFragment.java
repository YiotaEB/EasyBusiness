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
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.MainActivity;

import static com.easybusiness.eb_androidapp.UI.MainActivity.PREFERENCE_SESSIONID;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewEmployeeFragment extends Fragment {

    public static final String TAG = "ViewEmployeeFragment";

    public static final String EMPLOYEE_ID_KEY = "employee-id";
    public static final String EMPLOYEE_FIRSTNAME_KEY = "employee-firstname";
    public static final String EMPLOYEE_SURNAME_KEY = "employee-surname";
    public static final String EMPLOYEE_POSITION = "employee-position";
    public static final String EMPLOYEE_POSITION_ID = "employee-position-id";
    public static final String EMPLOYEE_CITY = "employee-city";
    public static final String EMPLOYEE_ADDRESS = "employee-address";
    public static final String EMPLOYEE_COUNTRY = "employee-country";
    public static final String EMPLOYEE_COUNTRY_ID = "employee-country-id";
    public static final String EMPLOYEE_DATEHIRED = "employee-datehired";
    public static final String EMPLOYEE_USERNAME = "employee-username";
    public static final String EMPLOYEE_TELEPHONE = "employee-telephone";

    private View v;
    private String title = "Employee";
    private int id = 0;
    private String username = "";
    private String firstname = "";
    private String surname = "";
    private String dateHired = "";
    private String city = "";
    private String address = "";
    private String country = "";
    private int countryID = -1;
    private String position = "";
    private int positionID = -1;
    private String telephone = "";

    private TextView positionTextview;
    private TextView usernameTextview;
    private TextView cityTextview;
    private TextView addressTextview;
    private TextView countryTextview;
    private TextView dateHiredTextview;
    private TextView nameTextview;
    private TextView telephoneTextview;

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
                bundle.putInt(EMPLOYEE_ID_KEY, id);
                bundle.putString(EMPLOYEE_FIRSTNAME_KEY, firstname);
                bundle.putString(EMPLOYEE_SURNAME_KEY, surname);
                bundle.putString(EMPLOYEE_USERNAME, username);
                bundle.putString(EMPLOYEE_POSITION, position);
                bundle.putString(EMPLOYEE_CITY, city);
                bundle.putString(EMPLOYEE_ADDRESS, address);
                bundle.putString(EMPLOYEE_COUNTRY, country);
                bundle.putString(EMPLOYEE_DATEHIRED, dateHired);
                bundle.putString(EMPLOYEE_TELEPHONE, telephone);
                bundle.putInt(EMPLOYEE_COUNTRY_ID, countryID);
                bundle.putInt(EMPLOYEE_POSITION_ID, positionID);


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

        new GetCountriesAsyncTask("SessionID=" + PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(PREFERENCE_SESSIONID, ""), getActivity(), v).execute();


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getInt(EMPLOYEE_ID_KEY);
            title = bundle.getString(EMPLOYEE_FIRSTNAME_KEY) + " " + bundle.getString(EMPLOYEE_SURNAME_KEY);
            username = bundle.getString(EMPLOYEE_USERNAME);
            firstname = bundle.getString(EMPLOYEE_FIRSTNAME_KEY);
            surname = bundle.getString(EMPLOYEE_SURNAME_KEY);
            position = bundle.getString(EMPLOYEE_POSITION);
            city = bundle.getString(EMPLOYEE_CITY);
            address = bundle.getString(EMPLOYEE_ADDRESS);
            country = bundle.getString(EMPLOYEE_COUNTRY);
            countryID = bundle.getInt(EMPLOYEE_COUNTRY_ID);
            positionID = bundle.getInt(EMPLOYEE_POSITION_ID);
            dateHired = bundle.getString(EMPLOYEE_DATEHIRED);
            telephone = bundle.getString(EMPLOYEE_TELEPHONE);
        }

        getActivity().setTitle(title);
        usernameTextview.setText(username);
        positionTextview.setText(position);
        cityTextview.setText(city);
        addressTextview.setText(address);
        countryTextview.setText(country);
        dateHiredTextview.setText(dateHired);
        nameTextview.setText(title);
        telephoneTextview.setText(telephone);

    }

}
