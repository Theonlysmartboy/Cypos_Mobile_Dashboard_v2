package com.cybene.cyposdashboard.utils.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cybene.cyposdashboard.ui.fragment.supplier.DailySupplierPurchaseGridFragment;
import com.cybene.cyposdashboard.ui.fragment.supplier.GRNListFragment;
import com.cybene.cyposdashboard.ui.fragment.supplier.GoodsReturnNoteListFragment;
import com.cybene.cyposdashboard.ui.fragment.supplier.LastGRNDetailsFragment;
import com.cybene.cyposdashboard.ui.fragment.supplier.LastPaymentDetailsFragment;
import com.cybene.cyposdashboard.ui.fragment.supplier.MonthlyPurchaseSupplierWiseFragment;
import com.cybene.cyposdashboard.ui.fragment.supplier.MonthlySupplierPurchaseChartFragment;
import com.cybene.cyposdashboard.ui.fragment.supplier.MonthlySupplierPurchaseGridFragment;
import com.cybene.cyposdashboard.ui.fragment.supplier.PaymentListFragment;
import com.cybene.cyposdashboard.ui.fragment.supplier.PostDatedChequeSupplierListFragment;
import com.cybene.cyposdashboard.ui.fragment.supplier.SupplierCreditDetailsFragment;
import com.cybene.cyposdashboard.ui.fragment.supplier.TotalGRNPerMonthFragment;


public class SupplierPagerAdapter extends FragmentStatePagerAdapter {
    String[] tabArray = new String[]{"GRN L", "G.R.N", "M.S.P.G","D.S.P.G", "P.D.C.S.L", "P.L", "M.S.P.C", "T.GRN.P.M",
     "L.GRN.D", "L.P.D", "M.P.S.W", "S.C.D"};
    Integer tabno = 12;
    public SupplierPagerAdapter(FragmentManager manager) {
        super(manager);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabArray[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new GRNListFragment();
            case 1:
                return new GoodsReturnNoteListFragment();
            case 2:
                return new MonthlySupplierPurchaseGridFragment();
            case 3:
                return new DailySupplierPurchaseGridFragment();
            case 4:
                return new PostDatedChequeSupplierListFragment();
            case 5:
                return new PaymentListFragment();
            case 6:
                return new MonthlySupplierPurchaseChartFragment();
            case 7:
                return new TotalGRNPerMonthFragment();
            case 8:
                return new LastGRNDetailsFragment();
            case 9:
                return new LastPaymentDetailsFragment();
            case 10:
                return new MonthlyPurchaseSupplierWiseFragment();
            case 11:
                return new SupplierCreditDetailsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabno;
    }

}