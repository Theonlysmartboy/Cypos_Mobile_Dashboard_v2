package com.cybene.cyposdashboard.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cybene.cyposdashboard.R;

public class DashboardDetailFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_FROM = "fromDate";
    private static final String ARG_TO = "toDate";
    private static final String ARG_BRANCH = "branch";

    private String title;
    private String fromDate;
    private String toDate;
    private String branch;

    public DashboardDetailFragment() {}

    public static DashboardDetailFragment newInstance(String title, String fromDate, String toDate, String branch) {
        DashboardDetailFragment fragment = new DashboardDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_FROM, fromDate);
        args.putString(ARG_TO, toDate);
        args.putString(ARG_BRANCH, branch);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard_detail, container, false);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            fromDate = getArguments().getString("fromDate");
            toDate = getArguments().getString("toDate");
            branch = getArguments().getString("branch");

            // Now you can use these to fetch details from your API
        }
        // Example: display the title at the top
        TextView tvTitle = root.findViewById(R.id.tvDashboardDetailTitle);
        tvTitle.setText(title);

        // You can now also use fromDate, toDate, and branch to fetch data
        // e.g., fetchDetailData(title, fromDate, toDate, branch);

        return root;
    }

    // Optional: method to fetch data
    private void fetchDetailData(String title, String from, String to, String branch) {
        // Use Volley or Retrofit to fetch detail data using the filters
    }
}
