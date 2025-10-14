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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private EditText fromDateEditText, toDateEditText;
    private String fromDate, toDate;
    private Button refreshButton;
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

        refreshButton = root.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> {
            fromDate = fromDateEditText.getText().toString().trim();
            toDate = toDateEditText.getText().toString().trim();

            if (fromDate.isEmpty() || toDate.isEmpty()) {
                Toast.makeText(requireContext(), "Please select both From and To dates", Toast.LENGTH_SHORT).show();
                return;
            }

            setupRecycler(fromDate, toDate);
        });

        Spinner spinner = root.findViewById(R.id.branchSelector);
        List<String> branches = new ArrayList<>();
        branches.add("Change Branch");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireActivity(), android.R.layout.simple_spinner_item, branches
        ) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
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

        // Default load: past 10 years to today
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -10);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String defaultFrom = sdf.format(cal.getTime());
        String defaultTo = sdf.format(new Date());

        setupRecycler(defaultFrom, defaultTo);

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
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    targetEditText.setText(sdf.format(calendar.getTime()));

                    if (targetEditText == fromDateEditText) {
                        toDateEditText.setText("");
                    }
                },
                year, month, day
        );

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

    private void setupRecycler(String fromDate, String toDate) {
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

        showSkeletonLoaders();

        fetchDashboardData(fromDate, toDate);
    }

    private void showSkeletonLoaders() {
        dashboardItems.clear();
        dashboardItems.add(new DashboardItem("Loading...", "", R.color.gray, true));
        for (int i = 0; i < 4; i++) {
            dashboardItems.add(new DashboardItem("Loading...", "", R.color.gray));
        }
        adapter.notifyDataSetChanged();
    }

    private void fetchDashboardData(String fromDate, String toDate) {
        String url = AppConfig.URL_DASHBOARD;
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    Log.d("Dashboard", "Response: " + response);
                    try {
                        JSONObject json = new JSONObject(response);
                        parseDashboardData(json);
                    } catch (Exception e) {
                        Log.e("Dashboard", "Error parsing response", e);
                        Toast.makeText(requireContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("Dashboard", "Network error fetching data", error);
                    Toast.makeText(requireContext(), "Network error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("from", fromDate);
                params.put("to", toDate);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void parseDashboardData(JSONObject response) {
        try {
            dashboardItems.clear();

            if (response.has("cashSale")) {
                JSONObject cash = response.getJSONObject("cashSale");
                DashboardItem largeCard = new DashboardItem(
                        "Total Cash Sales",
                        formatAmount(cash.optDouble("total", 0.0)),
                        R.color.green, true);
                largeCard.setExtra("total", formatAmount(cash.optDouble("total", 0.0)));
                largeCard.setExtra("cash", formatAmount(cash.optDouble("CS", 0.0)));
                largeCard.setExtra("mpesa", formatAmount(cash.optDouble("MP", 0.0)));
                largeCard.setExtra("cheque", formatAmount(cash.optDouble("CQ", 0.0)));
                largeCard.setExtra("card", formatAmount(cash.optDouble("CD", 0.0)));
                dashboardItems.add(largeCard);
            }

            dashboardItems.add(new DashboardItem("Invoice", formatAmount(response.optDouble("invoice")), R.color.pink));
            dashboardItems.add(new DashboardItem("Credit Note", formatAmount(response.optDouble("creditNote")), R.color.colorGoldenTainoi));
            dashboardItems.add(new DashboardItem("Purchase", formatAmount(response.optDouble("purchase")), R.color.purple1));
            dashboardItems.add(new DashboardItem("Goods Return", formatAmount(response.optDouble("goodsReturn")), R.color.purple));
            dashboardItems.add(new DashboardItem("Debit Note", formatAmount(response.optDouble("debitNote")), R.color.red));
            dashboardItems.add(new DashboardItem("Stock Valuation", formatAmount(response.optDouble("stockValuation")), R.color.skyBlue));
            dashboardItems.add(new DashboardItem("Daily Banking", formatAmount(response.optDouble("dailyBanking")), R.color.maroon));
            dashboardItems.add(new DashboardItem("VAT Payable", formatAmount(response.optDouble("vatPayable")), R.color.maroon1));

            if (response.has("computerWiseSales")) {
                JSONArray comps = response.getJSONArray("computerWiseSales");
                for (int i = 0; i < comps.length(); i++) {
                    JSONObject comp = comps.getJSONObject(i);
                    dashboardItems.add(new DashboardItem(
                            "Sales - " + comp.optString("ComputerName"),
                            formatAmount(Double.parseDouble(comp.optString("TotalSales", "0"))),
                            R.color.gray
                    ));
                }
            }

            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            Log.e("Dashboard", "Error parsing dashboard data", e);
        }
    }

    private String formatAmount(double value) {
        if (Double.isNaN(value)) value = 0.0;
        return String.format(Locale.getDefault(), "%,.2f", value);
    }
}
