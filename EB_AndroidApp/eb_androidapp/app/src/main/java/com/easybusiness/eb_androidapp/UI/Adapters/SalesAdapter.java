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
import com.easybusiness.eb_androidapp.Entities.ProductSupplies;
import com.easybusiness.eb_androidapp.Entities.Products;
import com.easybusiness.eb_androidapp.Entities.SaleProducts;
import com.easybusiness.eb_androidapp.Entities.Sales;
import com.easybusiness.eb_androidapp.Entities.Supplies;
import com.easybusiness.eb_androidapp.R;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

public class SalesAdapter extends ArrayAdapter<Sales> {

    private ArrayList<Sales>  salesList;
    private ArrayList<Customers> customersList;
    private ArrayList<SaleProducts> saleProductsList;
    private ArrayList<Products> productsList;
    private ArrayList<Customers> orig;


    public SalesAdapter(final Context context, ArrayList<Sales> salesList, ArrayList<Customers> customersList, ArrayList<SaleProducts> saleProductsList, ArrayList<Products> productsList) {
        super(context, R.layout.sales_adapter);
        this.salesList = salesList;
        this.customersList = customersList;
        this.saleProductsList = saleProductsList;
        this.productsList = productsList;
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
        final TextView dateTextView = view.findViewById(R.id.sales_adapter_date);

        final int id = position + 1;
        String customerName = "";
        for (int i = 0; i < customersList.size(); i++) {
            if (customersList.get(i).getID() == salesList.get(position).getCustomerID()) {
                customerName = customersList.get(i).getName();
                break;
            }
        }

        Date date = new Date(salesList.get(position).getSaleTimeDate());
        String dateString = Days.DATE_FORMAT.format(date);
        dateTextView.setText("(" + dateString + ")");

        float total = 0;
        for (int i = 0; i < saleProductsList.size(); i++) {
            double currentItemTotal = 0;
            if (saleProductsList.get(i).getSaleID() == salesList.get(position).getID()) {
                int quantity = saleProductsList.get(i).getQuantitySold();
                double price = 0;
                //Find product:
                int productID = saleProductsList.get(i).getProductID();
                for (int j = 0; j < productsList.size(); j++) {
                    if (productsList.get(j).getID() == productID) {
                        price = productsList.get(j).getPrice();
                        break;
                    }
                }
                currentItemTotal = quantity * price;
            }
            total += currentItemTotal;
        }

        String totalString = String.format("%.2f", total);

        //set TextViews
        salesAdapterId.setText(String.valueOf(id));
        salesAdapterName.setText(customerName);
        salesAdapterTotal.setText(String.valueOf(totalString));

        return view;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Customers> results = new ArrayList<>();
                if (orig == null)
                    orig = customersList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Customers g : orig) {
                            if (g.getName().toLowerCase().contains(constraint.toString()))
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
                customersList = (ArrayList<Customers>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
