package com.cybene.cyposdashboard.ui.fragment.supplier;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.adapter.SupplierPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class SupplierFragment extends Fragment implements DefaultLifecycleObserver {
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

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Register your class as observer
        if (getActivity() != null) {
            getActivity().getLifecycle().addObserver(this);
        }
    }

    public void onCreate(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onCreate(owner);
        // Remove the observer
        if (getActivity() != null) {
            getActivity().getLifecycle().removeObserver(this);
        }
        SupplierPagerAdapter pagerAdapter = new SupplierPagerAdapter(requireActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}