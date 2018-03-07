package com.easybusiness.eb_androidapp.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.easybusiness.eb_androidapp.Entities.Customers;
import com.easybusiness.eb_androidapp.R;

import java.util.ArrayList;

public class CustomerAdapter extends ArrayAdapter<Customers> {
    final ArrayList<Customers>  customersList;


    public CustomerAdapter(final Context context, ArrayList<Customers> customersList) {
        super(context, R.layout.customer_adapter);
        this.customersList = customersList;
    }

    @Override
    public int getCount(){
        return customersList.size();
    }

    @Override
    public View getView (final int position, final View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View view = layoutInflater.inflate(R.layout.customer_adapter, parent, false);
        final TextView customerAdapterId = view.findViewById(R.id.customer_adapter_id);
        final TextView customerAdapterName = view.findViewById(R.id.customer_adapter_name);
        final TextView customerAdapterTelephone= view.findViewById(R.id.customer_adapter_telephone);


        //set TextViews
        customerAdapterId.setText(String.valueOf(position+1));
        customerAdapterName.setText(customersList.get(position).getName());
        customerAdapterTelephone.setText(String.valueOf(customersList.get(position).getTelephone()));

        return view;
    }

}
