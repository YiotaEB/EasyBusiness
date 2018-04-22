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
public class ViewCustomerFragment extends Fragment {

    public static final String TAG = "ViewCustomerFragment";

    public static final String CUSTOMER_NAME_KEY = "customer-name";
    public static final String CUSTOMER_CITY = "customer-city";
    public static final String CUSTOMER_ADDRESS = "customer-address";
    public static final String CUSTOMER_COUNTRY = "customer-country";
    public static final String CUSTOMER_TELEPHONE = "customer-telephone";

    private View v;
    private String title = "Customer";
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

    public ViewCustomerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_customer, container, false);

        cityTextview = v.findViewById(R.id.viewCustomer_city_textview);
        addressTextview = v.findViewById(R.id.viewCustomer_address_textview);
        countryTextview = v.findViewById(R.id.viewCustomer_country_textview);
        nameTextview = v.findViewById(R.id.viewCustomer_name);
        telephoneTextview = v.findViewById(R.id.viewCustomer_telephone_textview);

        editButton = v.findViewById(R.id.viewCustomer_editButton);
        toPDFButton = v.findViewById(R.id.viewCustomer_ToPDFButton);

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
            title = bundle.getString(CUSTOMER_NAME_KEY);
            city = bundle.getString(CUSTOMER_CITY);
            address = bundle.getString(CUSTOMER_ADDRESS);
            country = bundle.getString(CUSTOMER_COUNTRY);
            telephone = bundle.getString(CUSTOMER_TELEPHONE);
        }

        getActivity().setTitle(title);
        cityTextview.setText(city);
        addressTextview.setText(address);
        countryTextview.setText(country);
        nameTextview.setText(title);
        telephoneTextview.setText(telephone);

    }

}
