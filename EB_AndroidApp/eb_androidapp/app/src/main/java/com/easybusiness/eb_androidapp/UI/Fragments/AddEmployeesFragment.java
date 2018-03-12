package com.easybusiness.eb_androidapp.UI.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
        //TODO: Check if all the required input has been filled and for any errors.
        //Return true if correct, false if not.
        return false;
    }

    private void getRequiredFields() {
        //TODO: Make API calls to get the required fields. For example countries & positions.
    }



}
