package com.cybene.cyposdashboard.utils.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cybene.cyposdashboard.ui.fragment.accounts.CustomerOutstandingFragment;
import com.cybene.cyposdashboard.ui.fragment.accounts.PaymentListFragment;
import com.cybene.cyposdashboard.ui.fragment.accounts.PaymentReversalListFragment;
import com.cybene.cyposdashboard.ui.fragment.accounts.PostCreditNoteListSalesFragment;
import com.cybene.cyposdashboard.ui.fragment.accounts.PostCreditNotePurchaseListFragment;
import com.cybene.cyposdashboard.ui.fragment.accounts.PostDatedChequeSupplierListFragment;
import com.cybene.cyposdashboard.ui.fragment.accounts.PostDatedChequeCustomerListFragment;
import com.cybene.cyposdashboard.ui.fragment.accounts.ReceiptListFragment;
import com.cybene.cyposdashboard.ui.fragment.accounts.ReceiptReversalListFragment;
import com.cybene.cyposdashboard.ui.fragment.accounts.SupplierOutstandingFragment;

public class AccountsPagerAdapter extends FragmentStatePagerAdapter {
    String[] tabArray = new String[]{"C.O", "S.O", "RECEIPT","RECEIPT REV","PAYMENT","PAYMENT REV",
    "P.D.C.C.L","P.D.C.S.L","P.C.N.S.L","P,C.N.P.L"};
    Integer tabno = 10;
    public AccountsPagerAdapter(@NonNull FragmentManager fm) {
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
                return new CustomerOutstandingFragment();
            case 1:
                return new SupplierOutstandingFragment();
            case 2:
                return new ReceiptListFragment();
            case 3:
                return new ReceiptReversalListFragment();
            case 4:
                return new PaymentListFragment();
            case 5:
                return new PaymentReversalListFragment();
            case 6:
                return new PostDatedChequeCustomerListFragment();
            case 7:
                return new PostDatedChequeSupplierListFragment();
            case 8:
                return new PostCreditNoteListSalesFragment();
            case 9:
                return new PostCreditNotePurchaseListFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabno;
    }
}
