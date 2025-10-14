package com.cybene.cyposdashboard.ui.dashboard;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.AppConfig;
import com.cybene.cyposdashboard.utils.adapter.DashboardAdapter;
import com.cybene.cyposdashboard.utils.items.DashboardItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private EditText fromDateEditText, toDateEditText;
    private Calendar calendar;
    private RecyclerView recyclerView;
    private DashboardAdapter adapter;
    private List<DashboardItem> dashboardItems;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        fromDateEditText = root.findViewById(R.id.fromDateEditText);
        toDateEditText = root.findViewById(R.id.toDateEditText);
        calendar = Calendar.getInstance();
        fromDateEditText.setOnClickListener(v -> showDatePicker(fromDateEditText));
        toDateEditText.setOnClickListener(v -> showDatePicker(toDateEditText));
        Spinner spinner = root.findViewById(R.id.branchSelector);
        List<String> branches = new ArrayList<>();
        branches.add("Change Branch");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireActivity(), android.R.layout.simple_spinner_item, branches
        ) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0; // Disable the placeholder
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        recyclerView = root.findViewById(R.id.dashboardRecyclerView);
        setupRecycler();
        return root;
    }

    private void showDatePicker(EditText targetEditText) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                    targetEditText.setText(sdf.format(calendar.getTime()));

                    // Optional: if updating fromDate, clear toDate to force reselection
                    if (targetEditText == fromDateEditText) {
                        toDateEditText.setText("");
                    }
                },
                year, month, day
        );

        // Always limit selection to today or earlier
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        // If selecting 'toDate', ensure it's not before 'fromDate'
        if (targetEditText == toDateEditText && !fromDateEditText.getText().toString().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                calendar.setTime(Objects.requireNonNull(sdf.parse(fromDateEditText.getText().toString())));
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            } catch (Exception ignored) {}
        }

        datePickerDialog.show();
    }

    private void setupRecycler() {
        dashboardItems = new ArrayList<>();
        adapter = new DashboardAdapter(requireContext(), dashboardItems);

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return dashboardItems.get(position).isLargeCard() ? 2 : 1;
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Show placeholders before fetching data
        showSkeletonLoaders();

        //Fetch real data
        fetchDashboardData();
    }

    private void showSkeletonLoaders() {
        dashboardItems.clear();

        // Adding one large skeleton card
        dashboardItems.add(new DashboardItem("Loading...", "", R.color.gray, true));

        // Adding 4 smaller skeleton cards
        for (int i = 0; i < 4; i++) {
            dashboardItems.add(new DashboardItem("Loading...", "", R.color.gray));
        }

        adapter.notifyDataSetChanged();
    }

    private void fetchDashboardData() {
        String url = AppConfig.URL_DASHBOARD;
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        dashboardItems.clear(); // remove hardcoded placeholders and skeleton data

                        // Handle large card
                        if (response.has("large")) {
                            JSONObject large = response.getJSONObject("large");
                            DashboardItem largeCard = new DashboardItem(
                                    large.optString("title", "Total Cash Sales"),
                                    large.optString("total_sales", "0.00"),
                                    R.color.black,
                                    true
                            );

                            // Optionally store extra breakdowns
                            largeCard.setExtra("cash", large.optString("cash", "0.00"));
                            largeCard.setExtra("mpesa", large.optString("mpesa", "0.00"));
                            largeCard.setExtra("credit", large.optString("credit", "0.00"));
                            largeCard.setExtra("expenses", large.optString("expenses", "0.00"));
                            largeCard.setExtra("total", large.optString("total_sales", "0.00"));

                            dashboardItems.add(largeCard);
                        }

                        // Handle smaller cards
                        if (response.has("cards")) {
                            JSONArray cards = response.getJSONArray("cards");
                            for (int i = 0; i < cards.length(); i++) {
                                JSONObject obj = cards.getJSONObject(i);
                                DashboardItem item = new DashboardItem(
                                        obj.optString("title", "Unknown"),
                                        obj.optString("amount", "0.00"),
                                        R.color.black
                                );
                                dashboardItems.add(item);
                            }
                        }

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        Log.e("Dashboard", "Error parsing dashboard data", e);
                    }
                },
                error -> Log.e("Dashboard", "Network error fetching dashboard data", error)
        );

        queue.add(request);
    }
}