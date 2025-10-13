package com.cybene.cyposdashboard.utils.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cybene.cyposdashboard.ui.inventory.DeliveryNoteListFragment;
import com.cybene.cyposdashboard.ui.inventory.DepartmentWiseStockValuationFragment;
import com.cybene.cyposdashboard.ui.inventory.TotalStockValuationFragment;

public class InventoryPagerAdapter extends FragmentStatePagerAdapter {
    String[] tabArray = new String[]{"DELIVERY NOTE LIST", "DEPARTMENT WISE STOCK VALUATION", "TOTAL STOCK VALUATION"};
    Integer tabno = 3;
    public InventoryPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }
    public CharSequence getPageTitle(int position) {
        return tabArray[position];
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new DeliveryNoteListFragment();
            case 1:
                return new DepartmentWiseStockValuationFragment();
            case 2:
                return new TotalStockValuationFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabno;
    }
}
