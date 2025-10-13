package com.cybene.cyposdashboard.utils.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cybene.cyposdashboard.ui.branch.PaymentListFragment;
import com.cybene.cyposdashboard.ui.branch.PaymentReversalListFragment;
import com.cybene.cyposdashboard.ui.branch.ReceiptListFragment;
import com.cybene.cyposdashboard.ui.branch.ReceiptReversalListFragment;
import com.cybene.cyposdashboard.ui.branch.ReceivedListFragment;
import com.cybene.cyposdashboard.ui.branch.TransferListFragment;

public class BranchPagerAdapter extends FragmentStatePagerAdapter {
    String[] tabArray = new String[]{"BRANCH TRANSFER", "BRANCH RECEIVED", "BRANCH PAYMENT","BRANCH PAYMENT REV","BRANCH RECEIPT","BRANCH RECEIPT REV"};
    Integer tabno = 6;
    public BranchPagerAdapter(@NonNull FragmentManager fm) {
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
                return new TransferListFragment();
            case 1:
                return new ReceivedListFragment();
            case 2:
                return new PaymentListFragment();
            case 3:
                return new PaymentReversalListFragment();
            case 4:
                return new ReceiptListFragment();
            case 5:
                return new ReceiptReversalListFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabno;
    }
}