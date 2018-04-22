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
public class ViewSupplyTabFragment extends Fragment {

    public static final String TAG = "ViewSupplyTabFragment";

    public static final String SUPPLY_NAME_KEY = "supply-name";
    public static final String SUPPLY_PRICE= "supply-price";
    public static final String SUPPLY_QUANTITY= "supply-quantity";
    public static final String SUPPLY_SUPPLIER = "supply-supplier";

    private View v;
    private String title = "Supply";
    private String price = "";
    private String quantityInStock = "";
    private String supplier = "";

    private TextView priceTextview;
    private TextView quantityTextview;
    private TextView supplierTextview;
    private TextView nameTextview;

    private Button editButton;
    private Button toPDFButton;

    public ViewSupplyTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_tab_view_supply, container, false);

        priceTextview = v.findViewById(R.id.viewSupply_price_textview);
        quantityTextview = v.findViewById(R.id.viewSupply_quantity_textview);
        supplierTextview = v.findViewById(R.id.viewSupply_supplier_textview);
        nameTextview = v.findViewById(R.id.viewSupply_name);

        editButton = v.findViewById(R.id.viewSupply_editButton);
        toPDFButton = v.findViewById(R.id.viewSupply_ToPDFButton);

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
            title = bundle.getString(SUPPLY_NAME_KEY);
            price = bundle.getString(SUPPLY_PRICE);
            quantityInStock = bundle.getString(SUPPLY_QUANTITY);
            supplier = bundle.getString(SUPPLY_SUPPLIER);
        }

        getActivity().setTitle(title);
        priceTextview.setText(price);
        quantityTextview.setText(quantityInStock);
        supplierTextview.setText(supplier);
        nameTextview.setText(title);

    }

}
