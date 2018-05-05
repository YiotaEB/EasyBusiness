package com.easybusiness.eb_androidapp.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.easybusiness.eb_androidapp.Entities.ProductionBatches;
import com.easybusiness.eb_androidapp.Entities.Products;
import com.easybusiness.eb_androidapp.Entities.SaleProducts;
import com.easybusiness.eb_androidapp.Entities.Sales;
import com.easybusiness.eb_androidapp.R;

import java.util.ArrayList;

public class NewProductionAdapter extends ArrayAdapter<Sales> {

    private ArrayList<ProductionBatches> productionBatchesList;
    private ArrayList<Products> productsList;


    public NewProductionAdapter(final Context context, ArrayList<ProductionBatches> productionBatchesList, ArrayList<Products> productsList) {
        super(context, R.layout.sales_adapter);
        this.productionBatchesList = productionBatchesList;
        this.productsList = productsList;
    }

    @Override
    public int getCount(){
        return productionBatchesList.size();
    }

    @Override
    public View getView (final int position, final View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View view = layoutInflater.inflate(R.layout.productions_adapter, parent, false);

        final TextView productNameTextView = view.findViewById(R.id.productions_adapter_product_name);
        final TextView totalTextView = view.findViewById(R.id.productions_adapter_total);

        //Find name:
        String productName = "";
        for (int i = 0; i < productsList.size(); i++) {
            if (productsList.get(i).getID() == productionBatchesList.get(position).getProductID()) {
                productName = productsList.get(i).getName();
                break;
            }
        }

        //Find quantity:
        int quantity = productionBatchesList.get(position).getQuantityProduced();

        productNameTextView.setText(productName);
        totalTextView.setText(String.valueOf(quantity));

        return view;
    }

}
