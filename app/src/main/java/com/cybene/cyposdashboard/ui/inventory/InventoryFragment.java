package com.cybene.cyposdashboard.ui.inventory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.adapter.InventoryPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class InventoryFragment extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inventory, container, false);
        viewPager = root.findViewById(R.id.viewpager);
        tabLayout= root.findViewById(R.id.pager);
        return root;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InventoryPagerAdapter pagerAdapter = new InventoryPagerAdapter(requireActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}