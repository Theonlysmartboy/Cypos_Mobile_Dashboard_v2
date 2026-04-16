package com.cybene.cyposdashboard.ui.fragment.sales;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.ui.fragment.SimpleFragment;
import com.cybene.cyposdashboard.utils.adapter.SalesPagerAdapter;
import com.google.android.material.tabs.TabLayout;
public class SalesFragment extends SimpleFragment{
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sales, container, false);
        viewPager = v.findViewById(R.id.viewpager);
        tabLayout=v.findViewById(R.id.pager);
        SalesPagerAdapter pagerAdapter = new SalesPagerAdapter(requireActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }
}
