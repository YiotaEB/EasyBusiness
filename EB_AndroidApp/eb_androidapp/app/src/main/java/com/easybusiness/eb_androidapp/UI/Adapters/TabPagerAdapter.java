package com.easybusiness.eb_androidapp.UI.Adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.easybusiness.eb_androidapp.UI.Fragments.ViewProductsFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.SuppliesFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    public TabPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ViewProductsFragment productsTab = new ViewProductsFragment();
                return productsTab;
            case 1:
                SuppliesFragment suppliesTab = new SuppliesFragment();
                return suppliesTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

}
