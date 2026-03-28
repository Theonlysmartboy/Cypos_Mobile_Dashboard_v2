package com.cybene.cyposdashboard.ui.dashboard;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.AppConfig;
import com.cybene.cyposdashboard.utils.adapter.DashboardDetailAdapter;
import com.cybene.cyposdashboard.utils.items.DashboardDetailItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class DashboardDetailFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_FROM = "fromDate";
    private static final String ARG_TO = "toDate";
    private static final String ARG_BRANCH = "branch";

    private String title;
    private String fromDate;
    private String toDate;
    private String branch;
    private Calendar calendar;
    private EditText fromDateEditText, toDateEditText;
    private Button refreshButton;
    private RecyclerView recyclerView;
    private DashboardDetailAdapter adapter;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard_detail, container, false);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            fromDate = getArguments().getString(ARG_FROM);
            toDate = getArguments().getString(ARG_TO);
            branch = getArguments().getString(ARG_BRANCH);
        }
        fromDateEditText = root.findViewById(R.id.fromDateEditText);
        toDateEditText = root.findViewById(R.id.toDateEditText);
        calendar = Calendar.getInstance();
        recyclerView = root.findViewById(R.id.dashboardDetailsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new DashboardDetailAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);
        // Set the passed dates in the EditText fields
        fromDateEditText.setText(fromDate);
        toDateEditText.setText(toDate);
        setToolbarTitle(title);
        fromDateEditText.setOnClickListener(v -> showDatePicker(fromDateEditText));
        toDateEditText.setOnClickListener(v -> showDatePicker(toDateEditText));
        refreshButton = root.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> {
            fromDate = fromDateEditText.getText().toString().trim();
            toDate = toDateEditText.getText().toString().trim();
            if (fromDate.isEmpty() || toDate.isEmpty()) {
                Toast.makeText(requireContext(), "Please select both From and To dates", Toast.LENGTH_SHORT).show();
                return;
            }
            fetchDetailData(recyclerView, title, fromDate, toDate, branch);
        });
        fetchDetailData(recyclerView, title, fromDate, toDate, branch);
        return root;
    }
    private void setToolbarTitle(String title) {
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(title);
        }
    }

    private void showDatePicker(EditText targetEditText) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    targetEditText.setText(sdf.format(calendar.getTime()));
                    if (targetEditText == fromDateEditText) toDateEditText.setText("");
                }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        if (targetEditText == toDateEditText && !fromDateEditText.getText().toString().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                calendar.setTime(Objects.requireNonNull(sdf.parse(fromDateEditText.getText().toString())));
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            } catch (Exception ignored) {}
        }
        datePickerDialog.show();
    }
    private void fetchDetailData(RecyclerView recyclerView, String title, String from, String to, String branch) {
        String url = AppConfig.URL_DASHBOARD_DETAIL;
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        StringRequest request = new StringRequest(Request.Method.POST, url,
            response -> {
                Log.d("fetchDetailData: ", "Response: " + response);
                try {
                    JSONObject json = new JSONObject(response);
                    List<DashboardDetailItem> items = parseDashboardResponse(json);
                    adapter.updateItems(items);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.e("Dashboard", "Error parsing response", e);
                    Toast.makeText(requireActivity(), "Error parsing data", Toast.LENGTH_SHORT).show();
                }
            },
            error -> {
                Log.e("Dashboard", "Failed to load: " + error.getMessage());
                Toast.makeText(requireActivity(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        ){
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("from_date", from);
                params.put("to_date", to);
                params.put("branch", branch);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
    private List<DashboardDetailItem> parseDashboardResponse(JSONObject response) throws JSONException {
        List<DashboardDetailItem> list = new ArrayList<>();
        if (response.has("data")) {
            JSONArray dataArray = response.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject obj = dataArray.getJSONObject(i);
                String itemTitle = obj.optString("title", "N/A");
                double itemAmount = Double.parseDouble(obj.optString("amount", "0.00"));
                int itemColor;
                if(Double.isNaN(itemAmount)) {
                    itemColor = R.color.black;
                } else if (itemAmount == 0.00) {
                    itemColor = R.color.black;
                } else if (itemAmount<0) {
                    itemColor = R.color.red;
                }else{
                    itemColor = R.color.green;
                }
                list.add(new DashboardDetailItem(itemTitle, itemAmount, itemColor));
            }
        } else if (response.has("error")) {
            Toast.makeText(requireActivity(), response.getString("error_msg"), Toast.LENGTH_SHORT).show();
        }
        return list;
    }
}