package com.cybene.cyposdashboard.ui.dashboard;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cybene.cyposdashboard.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private EditText fromDateEditText, toDateEditText;
    private Calendar calendar;

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

}