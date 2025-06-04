package com.cybene.cyposdashboard.ui.purchase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.adapter.PurchasePagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class PurchaseFragment extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_purchase, container, false);
        viewPager = root.findViewById(R.id.viewpager);
        tabLayout= root.findViewById(R.id.pager);
        return root;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PurchasePagerAdapter pagerAdapter = new PurchasePagerAdapter(requireActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}