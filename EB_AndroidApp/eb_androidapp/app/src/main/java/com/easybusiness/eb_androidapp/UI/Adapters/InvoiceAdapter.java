package com.easybusiness.eb_androidapp.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.easybusiness.eb_androidapp.Entities.Sales;
import com.easybusiness.eb_androidapp.R;

import java.util.ArrayList;

public class InvoiceAdapter extends ArrayAdapter<Sales> {
    final ArrayList<Sales>  salesList;


    public InvoiceAdapter(final Context context, ArrayList<Sales> salesList) {
        super(context, R.layout.sales_adapter);
        this.salesList = salesList;
    }

    @Override
    public int getCount(){
        return salesList.size();
    }

    @Override
    public View getView (final int position, final View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View view = layoutInflater.inflate(R.layout.sales_adapter, parent, false);
        final TextView salesAdapterId = view.findViewById(R.id.sales_adapter_id);
        final TextView salesAdapterName = view.findViewById(R.id.sales_adapter_customer_name);
        final TextView salesAdapterTotal = view.findViewById(R.id.sales_adapter_total);


        //set TextViews
        salesAdapterId.setText(String.valueOf(position+1));
        salesAdapterName.setText(String.valueOf(salesList.get(position).getCustomerID())); //TODO Display the customers name instead of his ID.
        salesAdapterTotal.setText(String.valueOf(salesList.get(position).getSaleProductsID()));

        return view;
    }

}
