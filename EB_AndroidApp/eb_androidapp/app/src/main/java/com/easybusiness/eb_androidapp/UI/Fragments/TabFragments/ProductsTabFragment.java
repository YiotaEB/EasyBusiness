package com.easybusiness.eb_androidapp.UI.Fragments.TabFragments;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;


import com.easybusiness.eb_androidapp.AsyncTask.GetProductsAsyncTask;
import com.easybusiness.eb_androidapp.Entities.Products;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.Adapters.ProductAdapter;
import com.easybusiness.eb_androidapp.UI.Dialogs;
import com.easybusiness.eb_androidapp.UI.Fragments.AddProductFragment;
import com.easybusiness.eb_androidapp.UI.MainActivity;

import java.util.ArrayList;

public class ProductsTabFragment extends Fragment {

    public static final String TAG = "ViewProductsFragment";
    public static final String TITLE = "View Products";

    private SearchView searchView;
    private ListView productListView;
    private ImageButton addProductBtn;
    private Button refreshButton;
    public static ProductAdapter allProductsAdapter;
    private View v;

    public ProductsTabFragment() {
        // Required empty public constructor
        System.out.println("CONSTRUCTOR CALLLED");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_tab_products, container, false);


        productListView = v.findViewById(R.id.productsList);
        searchView = v.findViewById(R.id.products_search_view);
        addProductBtn = v.findViewById(R.id.addProductButton);
        refreshButton = v.findViewById(R.id.refresh_products);

        //VIEW (Short click)
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                MainActivity mainActivity = (MainActivity) getActivity();
                bundle.putString(ViewProductTabFragment.PRODUCT_NAME_KEY, mainActivity.PRODUCT_DATA.get(i).getName());
                bundle.putString(ViewProductTabFragment.PRODUCT_PRICE, String.valueOf(mainActivity.PRODUCT_DATA.get(i).getPrice()));
                bundle.putString(ViewProductTabFragment.PRODUCT_QUANTITY_IN_STOCK, String.valueOf(mainActivity.PRODUCT_DATA.get(i).getQuantityInStock()));
                bundle.putString(ViewProductTabFragment.PRODUCT_SIZE,String.valueOf(mainActivity.PRODUCT_DATA.get(i).getProductSizeID()));
                bundle.putString(ViewProductTabFragment.PRODUCT_TYPE, String.valueOf(mainActivity.PRODUCT_DATA.get(i).getProductTypeID()));

                Fragment newFragment = new ViewProductTabFragment();
                newFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                fragmentTransaction.replace(R.id.frame, newFragment, ViewProductTabFragment.TAG);
                fragmentTransaction.addToBackStack(newFragment.getTag());
                fragmentTransaction.commit();
            }
        });

        //DELETE (Long click)
        productListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity mainActivity = (MainActivity) getActivity();
                AlertDialog dialog = Dialogs.createDeleteDialog(getActivity(), view, "Products", mainActivity.PRODUCT_DATA.get(i).getID(), mainActivity.PRODUCT_DATA.get(i).getName());
                dialog.show();
                return true;
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "None");
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("SessionID", sessionID);
                String query = builder.build().getEncodedQuery();
                new GetProductsAsyncTask(query, getActivity(), v).execute();
            }
        });


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productListView.setAdapter(allProductsAdapter);

                final ProductAdapter adapter = (ProductAdapter) productListView.getAdapter();
                ArrayList<Products> searchedProducts= new ArrayList<>();
                System.out.println("ADAPTER SIZE: " + adapter.getCount());
                for (int i = 0; i < adapter.getCount(); i++) {
                    Products products= adapter.getItem(i);
                    if (products.getName() != null) {
                        if (products.getName().toLowerCase().contains(newText.toLowerCase())) {
                            searchedProducts.add(products);
                        }
                    } else {
                        return false;
                    }
                }
                final ProductAdapter newAdapter = new ProductAdapter(getActivity(), searchedProducts);
                productListView.setAdapter(newAdapter);

                return true;
            }
        });
        addProductBtn =v.findViewById(R.id.addProductButton);
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new AddProductFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                getActivity().setTitle(AddProductFragment.TITLE);
                fragmentTransaction.replace(R.id.frame, newFragment, AddProductFragment.TAG);
                fragmentTransaction.addToBackStack(newFragment.getTag());
                fragmentTransaction.commit();
                ((MainActivity) getActivity()).setMenuItemChecked(newFragment);
            }
        });

        System.out.println("CREATED TAB FRAG");

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "None");

        Uri.Builder builder = new Uri.Builder().appendQueryParameter("SessionID", sessionID);
        String query = builder.build().getEncodedQuery();

        new GetProductsAsyncTask(query, getActivity(), v).execute();

        System.out.println("RESUMED TAG FRAG");

    }

}
