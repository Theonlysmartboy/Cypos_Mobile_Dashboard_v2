package com.cybene.cyposdashboard.ui.sales;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.ui.fragment.SimpleFragment;
import com.cybene.cyposdashboard.utils.adapter.SalesPagerAdapter;
import com.google.android.material.tabs.TabLayout;
public class HomeFragment extends SimpleFragment{
    ViewPager viewPager;
    TabLayout tabLayout;
    @NonNull
    public static Fragment newInstance() {
        return new HomeFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = v.findViewById(R.id.viewpager);
        tabLayout=v.findViewById(R.id.pager);
        return v;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SalesPagerAdapter pagerAdapter = new SalesPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
