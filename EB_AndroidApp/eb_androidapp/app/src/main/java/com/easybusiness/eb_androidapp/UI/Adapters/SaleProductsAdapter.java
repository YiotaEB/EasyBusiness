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
import com.easybusiness.eb_androidapp.Entities.Products;
import com.easybusiness.eb_androidapp.Entities.SaleProducts;
import com.easybusiness.eb_androidapp.Entities.Sales;
import com.easybusiness.eb_androidapp.R;

import java.sql.Date;
import java.util.ArrayList;

public class SaleProductsAdapter extends ArrayAdapter<Sales> {

    private Sales sale;
    private ArrayList<SaleProducts> saleProductsList;
    private ArrayList<Products> productsList;
    private ArrayList<SaleProducts> currentSaleProductsList;


    public SaleProductsAdapter(final Context context, Sales sale, ArrayList<SaleProducts> saleProductsList, ArrayList<Products> productsList) {
        super(context, R.layout.sales_adapter);
        this.sale = sale;
        this.saleProductsList = saleProductsList;
        this.productsList = productsList;

        //Filter for current sale only
        currentSaleProductsList = new ArrayList<SaleProducts>();
        for (int i = 0; i < saleProductsList.size(); i++) {
            if (saleProductsList.get(i).getSaleID() == sale.getID()) {
                currentSaleProductsList.add(saleProductsList.get(i));
            }
        }

    }

    @Override
    public int getCount(){
        return currentSaleProductsList.size();
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
            if (productsList.get(i).getID() == currentSaleProductsList.get(position).getProductID()) {
                productName = productsList.get(i).getName();
                break;
            }
        }

        //Find quantity:
        int quantity = currentSaleProductsList.get(position).getQuantitySold();

        //Find Total:
        double price = 0;
        for (int i = 0; i < productsList.size(); i++) {
            if (productsList.get(i).getID() == currentSaleProductsList.get(position).getProductID()) {
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
