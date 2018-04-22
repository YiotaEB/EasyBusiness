package com.easybusiness.eb_androidapp.UI.Fragments;


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
public class ViewSupplierFragment extends Fragment {

    public static final String TAG = "ViewsSupplierFragment";

    public static final String SUPPLIER_NAME_KEY = "supplier-name";
    public static final String SUPPLIER_CITY = "supplier-city";
    public static final String SUPPLIER_ADDRESS = "supplier-address";
    public static final String SUPPLIER_COUNTRY = "supplier-country";
    public static final String SUPPLIER_TELEPHONE = "supplier-telephone";

    private View v;
    private String title = "Supplier";
    private String city = "";
    private String address = "";
    private String country = "";
    private String telephone = "";

    private TextView cityTextview;
    private TextView addressTextview;
    private TextView countryTextview;
    private TextView nameTextview;
    private TextView telephoneTextview;

    private Button editButton;
    private Button toPDFButton;

    public ViewSupplierFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_supplier, container, false);

        cityTextview = v.findViewById(R.id.viewSupplier_city_textview);
        addressTextview = v.findViewById(R.id.viewSupplier_address_textview);
        countryTextview = v.findViewById(R.id.viewSupplier_country_textview);
        nameTextview = v.findViewById(R.id.viewSupplier_name);
        telephoneTextview = v.findViewById(R.id.viewSupplier_telephone_textview);

        editButton = v.findViewById(R.id.viewSupplier_editButton);
        toPDFButton = v.findViewById(R.id.viewSupplier_ToPDFButton);

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
            title = bundle.getString(SUPPLIER_NAME_KEY);
            city = bundle.getString(SUPPLIER_CITY);
            address = bundle.getString(SUPPLIER_ADDRESS);
            country = bundle.getString(SUPPLIER_COUNTRY);
            telephone = bundle.getString(SUPPLIER_TELEPHONE);
        }

        getActivity().setTitle(title);
        cityTextview.setText(city);
        addressTextview.setText(address);
        countryTextview.setText(country);
        nameTextview.setText(title);
        telephoneTextview.setText(telephone);

    }

}
