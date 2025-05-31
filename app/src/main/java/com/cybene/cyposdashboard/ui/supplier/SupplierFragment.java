package com.cybene.cyposdashboard.ui.supplier;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.adapter.CustomerPagerAdapter;
import com.cybene.cyposdashboard.utils.adapter.SupplierPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class SupplierFragment extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;

    public SupplierFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_supplier, container, false);
        viewPager = root.findViewById(R.id.viewpager);
        tabLayout= root.findViewById(R.id.pager);
        return root;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupplierPagerAdapter pagerAdapter = new SupplierPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}