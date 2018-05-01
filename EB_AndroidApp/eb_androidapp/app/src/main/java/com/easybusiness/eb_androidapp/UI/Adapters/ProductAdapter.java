package com.easybusiness.eb_androidapp.UI.Adapters;

import com.easybusiness.eb_androidapp.Entities.Products;
import com.easybusiness.eb_androidapp.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ProductAdapter extends ArrayAdapter<Products> {

    final ArrayList<Products>  productList;

    public ProductAdapter(final Context context, ArrayList<Products> productList) {
        super(context, R.layout.product_adapter);
        this.productList = productList;
    }

    @Override
    public int getCount(){
        return productList.size();
    }

    @Override
    public View getView (final int position, final View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View view = layoutInflater.inflate(R.layout.product_adapter, parent, false);
        final TextView productAdapterId = view.findViewById(R.id.product_adapter_id);
        final TextView productAdapterName = view.findViewById(R.id.product_adapter_name);
        final TextView productAdapterQuantity = view.findViewById(R.id.product_adapter_quantity);


        //set TextViews
        productAdapterId.setText(String.valueOf(position+1));
        productAdapterName.setText(productList.get(position).getName());
        productAdapterQuantity.setText(String.valueOf(productList.get(position).getQuantityInStock()) + " items");

        return view;
    }

}
