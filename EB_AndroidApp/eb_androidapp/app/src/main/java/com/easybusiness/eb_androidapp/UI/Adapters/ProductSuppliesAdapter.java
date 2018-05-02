package com.easybusiness.eb_androidapp.UI.Adapters;

import com.easybusiness.eb_androidapp.Entities.ProductSupplies;
import com.easybusiness.eb_androidapp.Entities.Products;
import com.easybusiness.eb_androidapp.Entities.Supplies;
import com.easybusiness.eb_androidapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductSuppliesAdapter extends ArrayAdapter<ProductSupplies> {

    private ArrayList<ProductSupplies> productSupplies;
    private ArrayList<Supplies> suppliesList;
    private ArrayList<ProductSupplies> orig;

    public ProductSuppliesAdapter(final Context context, ArrayList<ProductSupplies> productSupplies, ArrayList<Supplies> suppliesList) {
        super(context, R.layout.product_adapter);
        this.productSupplies = productSupplies;
        this.suppliesList = suppliesList;
    }

    @Override
    public int getCount(){
        return productSupplies.size();
    }

    @Override
    public View getView (final int position, final View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View view = layoutInflater.inflate(R.layout.product_supplies_adapter, parent, false);
        final TextView supplyNameTextView = view.findViewById(R.id.product_supply_name);
        final EditText supplyQuantityEditText = view.findViewById(R.id.product_supply_quantity);

        //Set TextViews
        String supplyName = "";
        for (int i = 0; i < suppliesList.size(); i++) {
            if (suppliesList.get(i).getID() == productSupplies.get(position).getSupplyID()) {
                supplyName = suppliesList.get(i).getName();
                break;
            }
        }

        supplyNameTextView.setText(supplyName);
        supplyQuantityEditText.setText(String.valueOf(productSupplies.get(position).getQuantityRequired()));

        return view;
    }

}
