package com.easybusiness.eb_androidapp.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.easybusiness.eb_androidapp.Entities.Supplies;
import com.easybusiness.eb_androidapp.R;

import java.util.ArrayList;

public class SuppliesAdapter extends ArrayAdapter<Supplies> {
    final ArrayList<Supplies>  suppliesList;


    public SuppliesAdapter(final Context context, ArrayList<Supplies> suppliesList) {
        super(context, R.layout.supplies_adapter);
        this.suppliesList = suppliesList;
    }

    @Override
    public int getCount(){
        return suppliesList.size();
    }

    @Override
    public View getView (final int position, final View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View view = layoutInflater.inflate(R.layout.supplies_adapter, parent, false);
        final TextView suppliesAdapterId = view.findViewById(R.id.supplies_adapter_id);
        final TextView suppliesAdapterName = view.findViewById(R.id.supplies_adapter_name);
        final TextView suppliesAdapterQuantity = view.findViewById(R.id.supplies_adapter_quantity);


        //set TextViews
        suppliesAdapterId.setText(String.valueOf(position+1));
        suppliesAdapterName.setText(suppliesList.get(position).getName());
        suppliesAdapterQuantity.setText(String.valueOf(suppliesList.get(position).getQuantity()));

        return view;
    }

}
