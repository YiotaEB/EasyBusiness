package com.easybusiness.eb_androidapp.UI.Adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.easybusiness.eb_androidapp.UI.Fragments.TabFragments.ProductsTabFragment;
import com.easybusiness.eb_androidapp.UI.Fragments.TabFragments.SuppliesTabFragment;

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
                ProductsTabFragment productsTab = new ProductsTabFragment();
                return productsTab;
            case 1:
                SuppliesTabFragment suppliesTab = new SuppliesTabFragment();
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
