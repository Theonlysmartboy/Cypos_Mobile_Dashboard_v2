package com.cybene.cyposdashboard.utils.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cybene.cyposdashboard.ui.purchase.DailyPurchaseChartFragment;
import com.cybene.cyposdashboard.ui.purchase.DailyPurchaseGridFragment;
import com.cybene.cyposdashboard.ui.purchase.DepartmentWisePurchaseFragment;
import com.cybene.cyposdashboard.ui.purchase.GrnListFragment;
import com.cybene.cyposdashboard.ui.purchase.LpoListFragment;
import com.cybene.cyposdashboard.ui.purchase.MonthlyPurchaseChartFragment;
import com.cybene.cyposdashboard.ui.purchase.MonthlyPurchaseGridFragment;
import com.cybene.cyposdashboard.ui.purchase.ReturnedGoodsListFragment;
import com.cybene.cyposdashboard.ui.purchase.SupplierWisePurchaseListFragment;

public class PurchasePagerAdapter extends FragmentStatePagerAdapter {
    String[] tabArray = new String[]{"D.P.C", "D.P.G", "M.P.C","M.P.G", "GRN List",
            "G.R.L", "LPO L", "S.W.P.L", "D.W.P"};
    Integer tabno = 9;
    public PurchasePagerAdapter(@NonNull FragmentManager fm) {
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
                return new DailyPurchaseChartFragment();
            case 1:
                return new DailyPurchaseGridFragment();
            case 2:
                return new MonthlyPurchaseChartFragment();
            case 3:
                return new MonthlyPurchaseGridFragment();
            case 4:
                return  new GrnListFragment();
            case 5:
                return new ReturnedGoodsListFragment();
            case 6:
                return new LpoListFragment();
            case 7:
                return new SupplierWisePurchaseListFragment();
            case 8:
                return new DepartmentWisePurchaseFragment();
        }
        return null;
    }
    @Override
    public int getCount() {
        return tabno;
    }
}
