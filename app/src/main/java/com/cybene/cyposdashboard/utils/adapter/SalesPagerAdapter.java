package com.cybene.cyposdashboard.utils.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cybene.cyposdashboard.ui.fragment.sales.CashSalePaymentModeFragment;
import com.cybene.cyposdashboard.ui.fragment.sales.ComputerWiseSalesFragment;
import com.cybene.cyposdashboard.ui.fragment.sales.CreditNoteListFragment;
import com.cybene.cyposdashboard.ui.fragment.sales.CustomerWiseSalesDataGridFragment;
import com.cybene.cyposdashboard.ui.fragment.sales.DailySalesDataGridFragment;
import com.cybene.cyposdashboard.ui.fragment.sales.DailySalesFragment;
import com.cybene.cyposdashboard.ui.fragment.sales.InvoiceListFragment;
import com.cybene.cyposdashboard.ui.fragment.sales.MonthlySalesDataGridFragment;
import com.cybene.cyposdashboard.ui.fragment.sales.MonthlySalesFragment;
import com.cybene.cyposdashboard.ui.fragment.sales.SalesManWiseSalesDataGridFragment;
import com.cybene.cyposdashboard.ui.fragment.sales.TillCashPickupFragment;
import com.cybene.cyposdashboard.ui.fragment.sales.TransactionWiseSalesDataGridFragment;
import com.cybene.cyposdashboard.ui.fragment.sales.VatCodeWiseSalesDataGridFragment;

public class SalesPagerAdapter extends FragmentStatePagerAdapter {
    String[] tabArray = new String[]{"M.S.C", "M.S.G", "D.S.C","D.S.G", "I.L", "T.C.P", "C.S.P", "C.W.S",
    "V.A.T", "C.N.L", "T.W.S", "S.W.S", "C.W.S"};
    Integer tabno = 13;
    public SalesPagerAdapter(FragmentManager manager) {
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
                return new MonthlySalesFragment();
            case 1:
                return new MonthlySalesDataGridFragment();
            case 2:
                return new DailySalesFragment();
            case 3:
                return new DailySalesDataGridFragment();
            case 4:
                return new InvoiceListFragment();
            case 5:
                return new TillCashPickupFragment();
            case 6:
                return new CashSalePaymentModeFragment();
            case 7:
                return new ComputerWiseSalesFragment();
            case 8:
                return new VatCodeWiseSalesDataGridFragment();
            case 9:
                return new CreditNoteListFragment();
            case 10:
                return new TransactionWiseSalesDataGridFragment();
            case 11:
                return new SalesManWiseSalesDataGridFragment();
            case 12:
                return new CustomerWiseSalesDataGridFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabno;
    }

}