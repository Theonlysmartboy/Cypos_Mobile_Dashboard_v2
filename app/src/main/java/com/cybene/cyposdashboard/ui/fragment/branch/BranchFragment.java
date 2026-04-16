package com.cybene.cyposdashboard.ui.fragment.branch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.adapter.BranchPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class BranchFragment extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View root = inflater.inflate(R.layout.fragment_branch, container, false);
        viewPager = root.findViewById(R.id.viewpager);
        tabLayout= root.findViewById(R.id.pager);
        BranchPagerAdapter pagerAdapter = new BranchPagerAdapter(requireActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return root;
    }
}