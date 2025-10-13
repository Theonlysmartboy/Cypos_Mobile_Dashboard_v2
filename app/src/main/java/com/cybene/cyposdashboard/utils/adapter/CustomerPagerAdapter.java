package com.cybene.cyposdashboard.utils.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cybene.cyposdashboard.ui.customer.CreditNoteListFragment;
import com.cybene.cyposdashboard.ui.customer.CustomerCreditDetailsFragment;
import com.cybene.cyposdashboard.ui.customer.DailyCustomerSalesFragment;
import com.cybene.cyposdashboard.ui.customer.InvoiceListFragment;
import com.cybene.cyposdashboard.ui.customer.LastInvoiceDetailsFragment;
import com.cybene.cyposdashboard.ui.customer.LastReceiptDetailsFragment;
import com.cybene.cyposdashboard.ui.customer.MonthlyCustomerPurchaseFragment;
import com.cybene.cyposdashboard.ui.customer.MonthlyCustomerSalesFragment;
import com.cybene.cyposdashboard.ui.customer.MonthlySalesCustomerWiseFragment;
import com.cybene.cyposdashboard.ui.customer.PostDatedChequeFragment;
import com.cybene.cyposdashboard.ui.customer.ReceiptFragment;
import com.cybene.cyposdashboard.ui.customer.TotalInvoicePerMonthFragment;


public class CustomerPagerAdapter extends FragmentStatePagerAdapter {
    String[] tabArray = new String[]{"I.L", "C.N.L", "M.C.L.G","D.C.L.G", "P.D.C.L", "R.L", "M.C.P.C", "T.I.P.M",
    "L.I.D", "L.R.D", "M.S.C.W", "C.C.D"};
    Integer tabno = 12;
    public CustomerPagerAdapter(FragmentManager manager) {
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
                return new InvoiceListFragment();
            case 1:
                return new CreditNoteListFragment();
            case 2:
                return new MonthlyCustomerSalesFragment();
            case 3:
                return new DailyCustomerSalesFragment();
            case 4:
                return new PostDatedChequeFragment();
            case 5:
                return new ReceiptFragment();
            case 6:
                return new MonthlyCustomerPurchaseFragment();
            case 7:
                return new TotalInvoicePerMonthFragment();
            case 8:
                return new LastInvoiceDetailsFragment();
            case 9:
                return new LastReceiptDetailsFragment();
            case 10:
                return new MonthlySalesCustomerWiseFragment();
            case 11:
                return new CustomerCreditDetailsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabno;
    }

}