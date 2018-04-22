package com.easybusiness.eb_androidapp.UI.Fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.easybusiness.eb_androidapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewEmployeeFragment extends Fragment {

    public static final String TAG = "ViewEmployeeFragment";

    public static final String EMPLOYEE_NAME_KEY = "employee-name";
    public static final String EMPLOYEE_POSITION = "employee-position";
    public static final String EMPLOYEE_CITY = "employee-city";
    public static final String EMPLOYEE_ADDRESS = "employee-address";
    public static final String EMPLOYEE_COUNTRY = "employee-country";
    public static final String EMPLOYEE_DATEHIRED = "employee-datehired";
    public static final String EMPLOYEE_USERNAME = "employee-username";
    public static final String EMPLOYEE_TELEPHONE = "employee-telephone";

    private View v;
    private String title = "Employee";
    private String username = "";
    private String dateHired = "";
    private String city = "";
    private String address = "";
    private String country = "";
    private String position = "";
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
                Toast.makeText(getActivity(), "TODO", Toast.LENGTH_LONG).show();
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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            title = bundle.getString(EMPLOYEE_NAME_KEY);
            username = bundle.getString(EMPLOYEE_USERNAME);
            position = bundle.getString(EMPLOYEE_POSITION);
            city = bundle.getString(EMPLOYEE_CITY);
            address = bundle.getString(EMPLOYEE_ADDRESS);
            country = bundle.getString(EMPLOYEE_COUNTRY);
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
