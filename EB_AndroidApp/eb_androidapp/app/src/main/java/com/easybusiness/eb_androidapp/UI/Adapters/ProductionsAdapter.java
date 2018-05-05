package com.easybusiness.eb_androidapp.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.easybusiness.eb_androidapp.Entities.Customers;
import com.easybusiness.eb_androidapp.Entities.Days;
import com.easybusiness.eb_androidapp.Entities.ProductionBatches;
import com.easybusiness.eb_androidapp.Entities.Products;
import com.easybusiness.eb_androidapp.Entities.SaleProducts;
import com.easybusiness.eb_androidapp.Entities.Sales;
import com.easybusiness.eb_androidapp.R;

import java.sql.Date;
import java.util.ArrayList;

public class ProductionsAdapter extends ArrayAdapter<Sales> {

    private ArrayList<ProductionBatches> productionBatchesList;
    private ArrayList<Products> productsList;


    public ProductionsAdapter(final Context context, ArrayList<ProductionBatches> productionBatchesList, ArrayList<Products> productsList) {
        super(context, R.layout.sales_adapter);
        this.productionBatchesList = productionBatchesList;
        this.productsList = productsList;
    }

    @Override
    public int getCount(){
        return productionBatchesList.size();
    }

    @Override
    public View getView (final int position, final View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View view = layoutInflater.inflate(R.layout.production_adapter, parent, false);
        final TextView productNameTextView = view.findViewById(R.id.production_adapter_name);
        final TextView quantityTextView = view.findViewById(R.id.production_adapter_quantity);
        final TextView dateTextView = view.findViewById(R.id.production_adapter_date);

        String productName = "";
        for (int i = 0; i < productsList.size(); i++) {
            if (productsList.get(i).getID() == productionBatchesList.get(position).getProductID()) {
                productName = productsList.get(i).getName();
                break;
            }
        }

        Date date = new Date(productionBatchesList.get(position).getProductionDate());
        String dateString = Days.DATE_FORMAT.format(date);

        int quantity = productionBatchesList.get(position).getQuantityProduced();

        //set TextViews
        productNameTextView.setText(productName);
        quantityTextView.setText(String.valueOf(quantity));
        dateTextView.setText("(" + dateString + ")");

        return view;
    }

}
