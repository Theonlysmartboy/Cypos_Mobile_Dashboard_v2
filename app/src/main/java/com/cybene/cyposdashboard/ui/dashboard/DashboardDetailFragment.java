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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cybene.cyposdashboard.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
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
        fromDateEditText = root.findViewById(R.id.fromDateEditText);
        toDateEditText = root.findViewById(R.id.toDateEditText);
        calendar = Calendar.getInstance();
        // Set the passed dates in the EditText fields
        fromDateEditText.setText(fromDate);
        toDateEditText.setText(toDate);
        // Set the title in the toolbar
            setToolbarTitle(title);
        fromDateEditText.setOnClickListener(v -> showDatePicker(fromDateEditText));
        toDateEditText.setOnClickListener(v -> showDatePicker(toDateEditText));

        Button refreshButton = root.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> {
            fromDate = fromDateEditText.getText().toString().trim();
            toDate = toDateEditText.getText().toString().trim();

            if (fromDate.isEmpty() || toDate.isEmpty()) {
                Toast.makeText(requireContext(), "Please select both From and To dates", Toast.LENGTH_SHORT).show();
                return;
            }

            fetchDetailData(title,fromDate, toDate,branch);
        });
        fetchDetailData(title, fromDate, toDate, branch);
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

    // Optional: method to fetch data
    private void fetchDetailData(String title, String from, String to, String branch) {
        String url = "https://yourserver.com/api/dashboard/details?from=2016-01-06&to=2026-01-06";
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    /*try {
                        List<DashboardItem> items = parseDashboardResponse(response);
                        adapter.updateItems(items);
                    } catch (JSONException e) {
                        Log.e("Dashboard", "Error parsing response", e);
                        Toast.makeText(requireActivity(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }*/
                },
                error -> {
                    Log.e("Dashboard", "Failed to load: " + error.getMessage());
                    Toast.makeText(requireActivity(), "Failed to load data", Toast.LENGTH_SHORT).show();
                }
        );
        request.setRetryPolicy(new DefaultRetryPolicy(25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}
