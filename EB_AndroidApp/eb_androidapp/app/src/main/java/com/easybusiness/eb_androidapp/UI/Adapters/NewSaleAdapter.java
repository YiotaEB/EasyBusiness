package com.easybusiness.eb_androidapp.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.easybusiness.eb_androidapp.Entities.Products;
import com.easybusiness.eb_androidapp.Entities.SaleProducts;
import com.easybusiness.eb_androidapp.Entities.Sales;
import com.easybusiness.eb_androidapp.R;

import java.util.ArrayList;

public class NewSaleAdapter extends ArrayAdapter<Sales> {

    private ArrayList<SaleProducts> saleProductsList;
    private ArrayList<Products> productsList;


    public NewSaleAdapter(final Context context, ArrayList<SaleProducts> saleProductsList, ArrayList<Products> productsList) {
        super(context, R.layout.sales_adapter);
        this.saleProductsList = saleProductsList;
        this.productsList = productsList;
    }

    @Override
    public int getCount(){
        return saleProductsList.size();
    }

    @Override
    public View getView (final int position, final View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View view = layoutInflater.inflate(R.layout.sale_products_adapter, parent, false);

        final TextView productNameTextView = view.findViewById(R.id.sale_product_name);
        final TextView quantityTextView = view.findViewById(R.id.sale_product_quantity);
        final TextView totalTextView = view.findViewById(R.id.sale_product_total);

        //Find name:
        String productName = "";
        for (int i = 0; i < productsList.size(); i++) {
            if (productsList.get(i).getID() == saleProductsList.get(position).getProductID()) {
                productName = productsList.get(i).getName();
                break;
            }
        }

        //Find quantity:
        int quantity = saleProductsList.get(position).getQuantitySold();

        //Find Total:
        double price = 0;
        for (int i = 0; i < productsList.size(); i++) {
            if (productsList.get(i).getID() == saleProductsList.get(position).getProductID()) {
                price = productsList.get(i).getPrice();
                break;
            }
        }

        double total = price * quantity;
        String totalString = String.format("%.2f", total);

        productNameTextView.setText(productName);
        quantityTextView.setText(String.valueOf(quantity));
        totalTextView.setText(totalString);

        return view;
    }

}
