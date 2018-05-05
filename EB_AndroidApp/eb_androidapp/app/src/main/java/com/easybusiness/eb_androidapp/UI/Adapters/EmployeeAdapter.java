package com.easybusiness.eb_androidapp.UI.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.easybusiness.eb_androidapp.Entities.Customers;
import com.easybusiness.eb_androidapp.Entities.Users;
import com.easybusiness.eb_androidapp.R;

import java.util.ArrayList;

public class EmployeeAdapter extends ArrayAdapter<Users> {
    private ArrayList<Users> employeeList;
    private ArrayList<Users> orig;

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

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Users> results = new ArrayList<Users>();
                if (orig == null)
                    orig = employeeList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Users g : orig) {
                            if (g.getFirstname().toLowerCase().contains(constraint.toString()))
                                results.add(g);
                            else if (g.getLastname().toLowerCase().contains(constraint.toString()))
                                results.add(g);
                            else if (g.getUsername().toLowerCase().contains(constraint.toString()))
                                results.add(g);

                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                employeeList = (ArrayList<Users>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
