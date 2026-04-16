package com.cybene.cyposdashboard.ui.fragment.accounts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.adapter.AccountsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class AccountsFragment extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;

    public AccountsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_accounts, container, false);
        viewPager = v.findViewById(R.id.viewpager);
        tabLayout=v.findViewById(R.id.pager);
        AccountsPagerAdapter pagerAdapter = new AccountsPagerAdapter(requireActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }
}