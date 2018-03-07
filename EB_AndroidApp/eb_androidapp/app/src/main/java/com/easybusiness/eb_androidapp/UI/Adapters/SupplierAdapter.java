package com.easybusiness.eb_androidapp.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.easybusiness.eb_androidapp.Entities.Suppliers;
import com.easybusiness.eb_androidapp.R;

import java.util.ArrayList;

public class SupplierAdapter extends ArrayAdapter<Suppliers> {
    final ArrayList<Suppliers>  suppliersList;


    public SupplierAdapter(final Context context, ArrayList<Suppliers> suppliersList) {
        super(context, R.layout.supplier_adapter);
        this.suppliersList = suppliersList;
    }

    @Override
    public int getCount(){
        return suppliersList.size();
    }

    @Override
    public View getView (final int position, final View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View view = layoutInflater.inflate(R.layout.supplier_adapter, parent, false);
        final TextView suppliersAdapterId = view.findViewById(R.id.supplier_adapter_id);
        final TextView suppliersAdapterName = view.findViewById(R.id.supplier_adapter_name);
        final TextView suppliersAdapterTelephone= view.findViewById(R.id.supplier_adapter_telephone);


        //set TextViews
        suppliersAdapterId.setText(String.valueOf(position+1));
        suppliersAdapterName.setText(suppliersList.get(position).getName());
        suppliersAdapterTelephone.setText(String.valueOf(suppliersList.get(position).getTelephone()));

        return view;
    }

}
