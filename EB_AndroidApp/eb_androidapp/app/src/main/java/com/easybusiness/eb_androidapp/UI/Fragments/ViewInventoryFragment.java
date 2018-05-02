package com.easybusiness.eb_androidapp.UI.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewInventoryFragment extends Fragment {

    public static final String TAG = "ViewInventoryFragment";
    public static final String TITLE = "Inventory";

    private Button viewProducts;
    private Button viewSupplies;
    private ViewPager pager;
    View v;
    TabLayout tabLayout;



    public ViewInventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_inventory, container, false);

        //add tabs
//        tabLayout = v.findViewById(R.id.tab_layout);
//        tabLayout.addTab(tabLayout.newTab().setText("Products"));
//        tabLayout.addTab(tabLayout.newTab().setText("Supplies"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(TITLE);

//        final ViewPager viewPager = v.findViewById(R.id.pager);
//        final TabPagerAdapter adapter = new TabPagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
//
//        viewPager.setAdapter(adapter);

        viewProducts = v.findViewById(R.id.viewProducts);
        viewProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new ProductsFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                getActivity().setTitle(ProductsFragment.TITLE);
                fragmentTransaction.replace(R.id.frame, newFragment, ProductsFragment.TAG);
                fragmentTransaction.addToBackStack(newFragment.getTag());
                fragmentTransaction.commit();
                ((MainActivity) getActivity()).setMenuItemChecked(newFragment);
            }
        });

        viewSupplies = v.findViewById(R.id.viewSupplies);
        viewSupplies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new SuppliesFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                getActivity().setTitle(SuppliesFragment.TITLE);
                fragmentTransaction.replace(R.id.frame, newFragment, SuppliesFragment.TAG);
                fragmentTransaction.addToBackStack(newFragment.getTag());
                fragmentTransaction.commit();
                ((MainActivity) getActivity()).setMenuItemChecked(newFragment);
            }
        });
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

    }

}
