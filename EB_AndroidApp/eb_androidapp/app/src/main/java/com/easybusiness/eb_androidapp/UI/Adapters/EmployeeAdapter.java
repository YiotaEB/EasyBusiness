package com.easybusiness.eb_androidapp.UI.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.easybusiness.eb_androidapp.Entities.Users;
import com.easybusiness.eb_androidapp.R;

import java.util.ArrayList;

public class EmployeeAdapter extends ArrayAdapter<Users> {
    final ArrayList<Users> employeeList;


    public EmployeeAdapter(final Context context, ArrayList<Users> employeeList) {
        super(context, R.layout.employee_adapter);
        this.employeeList = employeeList;
    }

    @Override
    public int getCount(){
        return employeeList.size();
    }

    @Nullable
    @Override
    public Users getItem(int position) {
        return employeeList.get(position);
    }

    @Override
    public View getView (final int position, final View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View view = layoutInflater.inflate(R.layout.employee_adapter, parent, false);
        final TextView employeeAdapterId = view.findViewById(R.id.employee_adapter_id);
        final TextView employeeAdapterFirstName = view.findViewById(R.id.employee_adapter_first_name);
        final TextView employeeAdapterLastName = view.findViewById(R.id.employee_adapter_last_name);
        final TextView employeeAdapterTelephone= view.findViewById(R.id.employee_adapter_telephone);


        //set TextViews
        employeeAdapterId.setText(String.valueOf(position+1));
        employeeAdapterFirstName.setText(employeeList.get(position).getFirstname());
        employeeAdapterLastName.setText(employeeList.get(position).getLastname());
        employeeAdapterTelephone.setText(String.valueOf(employeeList.get(position).getTelephone()));

        return view;
    }

}
