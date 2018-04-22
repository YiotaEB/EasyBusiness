package com.easybusiness.eb_androidapp.UI.Fragments.TabFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.easybusiness.eb_androidapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProductTabFragment extends Fragment {

    public static final String TAG = "ViewProductTabFragment";

    public static final String PRODUCT_NAME_KEY = "product-name";
    public static final String PRODUCT_PRICE= "product-price";
    public static final String PRODUCT_QUANTITY_IN_STOCK= "product-quantity-in-stock";
    public static final String PRODUCT_SIZE = "product-size";
    public static final String PRODUCT_TYPE = "product-type";

    private View v;
    private String title = "Product";
    private String price = "";
    private String quantityInStock = "";
    private String size = "";
    private String type  = "";

    private TextView priceTextview;
    private TextView quantityTextview;
    private TextView sizeTextview;
    private TextView nameTextview;
    private TextView typeTextview;

    private Button editButton;
    private Button toPDFButton;

    public ViewProductTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_tab_view_product, container, false);

        priceTextview = v.findViewById(R.id.viewProduct_price_textview);
        quantityTextview = v.findViewById(R.id.viewProduct_quantity_textview);
        sizeTextview = v.findViewById(R.id.viewProduct_size_textview);
        nameTextview = v.findViewById(R.id.viewProduct_name);
        typeTextview = v.findViewById(R.id.viewProduct_type_textview);

        editButton = v.findViewById(R.id.viewProduct_editButton);
        toPDFButton = v.findViewById(R.id.viewProduct_ToPDFButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TODO", Toast.LENGTH_LONG).show();
            }
        });

        toPDFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TODO", Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            title = bundle.getString(PRODUCT_NAME_KEY);
            price = bundle.getString(PRODUCT_PRICE);
            quantityInStock = bundle.getString(PRODUCT_QUANTITY_IN_STOCK);
            size = bundle.getString(PRODUCT_SIZE);
            type = bundle.getString(PRODUCT_TYPE);
        }

        getActivity().setTitle(title);
        priceTextview.setText(price);
        quantityTextview.setText(quantityInStock);
        sizeTextview.setText(size);
        nameTextview.setText(title);
        typeTextview.setText(type);

    }

}
