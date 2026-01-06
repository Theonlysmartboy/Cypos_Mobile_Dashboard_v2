package com.cybene.cyposdashboard.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.items.DashboardItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
        }
        TextView tvTitle = root.findViewById(R.id.tvDashboardDetailTitle);
        tvTitle.setText(title);
        fetchDetailData(title, fromDate, toDate, branch);
        return root;
    }

    // Optional: method to fetch data
    private void fetchDetailData(String title, String from, String to, String branch) {
        String url = "https://yourserver.com/api/dashboard/details?from=2016-01-06&to=2026-01-06";
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<DashboardItem> items = parseDashboardResponse(response);
                            adapter.updateItems(items);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Dashboard", "Failed to load: " + error.getMessage());
                    }
                }
        );
        queue.add(request);
    }
}
