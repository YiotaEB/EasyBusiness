package com.easybusiness.eb_androidapp.UI.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.easybusiness.eb_androidapp.Entities.Countries;
import com.easybusiness.eb_androidapp.Entities.UserLevels;
import com.easybusiness.eb_androidapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEmployeesFragment extends Fragment {

    public static final String TAG = "AddEmployeesFragment";
    public static final String TITLE = "Add Employee";

    //TODO: Fill these array lists with values given from the API calls.
    private ArrayList<Countries> countries;
    private ArrayList<UserLevels> positions;

    private View v;
    private EditText firstnameEditText;
    private EditText lastnameEditText;
    private EditText addressEditText;
    private EditText telephoneEditText;
    private EditText cityEditText;
    private Spinner countrySpinner;
    private Spinner positionSpinner;
    private RadioButton partTimeRadioButton;
    private RadioButton fullTimeRadioButton;
    private Button add_employees_Btn;

    public AddEmployeesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_add_employees, container, false);

        add_employees_Btn = v.findViewById(R.id.add_employees_Btn);
        firstnameEditText = v.findViewById(R.id.employees_name_edittext);
        lastnameEditText = v.findViewById(R.id.employees_lastname_edittext);
        addressEditText = v.findViewById(R.id.employees_address_edittext);
        telephoneEditText = v.findViewById(R.id.employees_city_edittext);
        cityEditText = v.findViewById(R.id.employees_city_edittext);
        countrySpinner = v.findViewById(R.id.country_spinner);
        positionSpinner = v.findViewById(R.id.position_spinner);
        partTimeRadioButton = v.findViewById(R.id.part_time_radio_button);
        fullTimeRadioButton = v.findViewById(R.id.full_time_radio_button);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        add_employees_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: if (checkInput()) then call the API with the set values.
            }
        });

    }

    private boolean checkInput() {

        //Check first name:
        if (firstnameEditText.getText().toString().trim().isEmpty()) {
            firstnameEditText.setError("Please provide a valid answer.");
            return false;
        }
        else {
            firstnameEditText.setError(null);
        }

        //Check last name:
        if (lastnameEditText.getText().toString().trim().isEmpty()) {
            lastnameEditText.setError("Please provide a valid answer.");
            return false;
        }
        else {
            lastnameEditText.setError(null);
        }

        //Check address
        if (addressEditText.getText().toString().trim().isEmpty()) {
            addressEditText.setError("Please provide a valid answer.");
            return false;
        }
        else {
            addressEditText.setError(null);
        }

        //Check city
        if (cityEditText.getText().toString().trim().isEmpty()) {
            cityEditText.setError("Please provide a valid answer.");
            return false;
        }
        else {
            cityEditText.setError(null);
        }

        //Check Telephone
        if (telephoneEditText.getText().toString().trim().isEmpty()) {
            telephoneEditText.setError("Please provide a valid answer.");
            return false;
        }
        else {
            telephoneEditText.setError(null);
        }

        return true;
    }

    private void getRequiredFields() {

        //TODO: Make API calls to get the required fields. For example countries & positions.
    }



}
