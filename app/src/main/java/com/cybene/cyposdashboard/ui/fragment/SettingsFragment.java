package com.cybene.cyposdashboard.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.ui.auth.LoginActivity;
import com.cybene.cyposdashboard.ui.auth.SetPinActivity;
import com.cybene.cyposdashboard.utils.HashUtils;
import com.cybene.cyposdashboard.utils.db.Db;
import com.cybene.cyposdashboard.utils.db.SharedPrefs;

import java.io.File;

public class SettingsFragment extends Fragment {

    private TextView tvName, tvEmail;
    private EditText etServerUrl;
    private Spinner spinnerIdleTime, spinnerTheme;
    private Db db;
    private static final String PREF_IDLE_TIMEOUT = "idle_timeout";
    private static final String PREF_THEME = "app_theme";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        db = new Db(getContext());
        tvName = view.findViewById(R.id.settings_name);
        tvEmail = view.findViewById(R.id.settings_email);
        etServerUrl = view.findViewById(R.id.et_server_url);
        spinnerIdleTime = view.findViewById(R.id.spinner_idle_time);
        spinnerTheme = view.findViewById(R.id.spinner_theme);
        
        Button btnChangePin = view.findViewById(R.id.btn_change_pin);
        Button btnUpdateUrl = view.findViewById(R.id.btn_update_url);
        Button btnClearCache = view.findViewById(R.id.btn_clear_cache);
        Button btnViewLogs = view.findViewById(R.id.btn_view_logs);
        Button btnLogout = view.findViewById(R.id.btn_logout);

        loadUserData();
        setupSpinners();
        loadConfig();

        btnChangePin.setOnClickListener(v -> startActivity(new Intent(getActivity(), SetPinActivity.class)));

        btnUpdateUrl.setOnClickListener(v -> {
            String newUrl = etServerUrl.getText().toString().trim();
            if (!newUrl.isEmpty()) {
                db.updateConfig("url", newUrl);
                Toast.makeText(getContext(), "Server URL updated", Toast.LENGTH_SHORT).show();
            }
        });

        btnClearCache.setOnClickListener(v -> showPinConfirmationDialog(this::clearCache));

        btnViewLogs.setOnClickListener(v -> Toast.makeText(getContext(), "Logs feature coming soon", Toast.LENGTH_SHORT).show());

        btnLogout.setOnClickListener(v -> logout());

        return view;
    }

    private void loadUserData() {
        Cursor cursor = db.getUser();
        if (cursor != null && cursor.moveToFirst()) {
            tvName.setText(cursor.getString(1));
            tvEmail.setText(cursor.getString(2));
        }
        if (cursor != null) cursor.close();
    }

    private void loadConfig() {
        String url = db.getConfig().get("url");
        etServerUrl.setText(url);
    }

    private void setupSpinners() {
        // Idle Time Spinner
        String[] idleOptions = {"Never", "1 Minute", "5 Minutes", "10 Minutes", "30 Minutes"};
        int[] idleValues = {0, 1, 5, 10, 30};
        ArrayAdapter<String> idleAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, idleOptions);
        idleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIdleTime.setAdapter(idleAdapter);

        int currentTimeout = SharedPrefs.getInstance().getInt(PREF_IDLE_TIMEOUT, 0);
        for (int i = 0; i < idleValues.length; i++) {
            if (idleValues[i] == currentTimeout) {
                spinnerIdleTime.setSelection(i);
                break;
            }
        }

        spinnerIdleTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPrefs.getInstance().saveInt(PREF_IDLE_TIMEOUT, idleValues[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Theme Spinner
        String[] themeOptions = {"Light", "Dark", "System Default"};
        ArrayAdapter<String> themeAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, themeOptions);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTheme.setAdapter(themeAdapter);

        int currentTheme = SharedPrefs.getInstance().getInt(PREF_THEME,
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        if (currentTheme == AppCompatDelegate.MODE_NIGHT_NO) spinnerTheme.setSelection(0);
        else if (currentTheme == AppCompatDelegate.MODE_NIGHT_YES) spinnerTheme.setSelection(1);
        else spinnerTheme.setSelection(2);

        spinnerTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int newTheme;
                if (position == 0) newTheme = AppCompatDelegate.MODE_NIGHT_NO;
                else if (position == 1) newTheme = AppCompatDelegate.MODE_NIGHT_YES;
                else newTheme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

                if (newTheme != SharedPrefs.getInstance().getInt(PREF_THEME, -1)) {
                    SharedPrefs.getInstance().saveInt(PREF_THEME, newTheme);
                    AppCompatDelegate.setDefaultNightMode(newTheme);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void showPinConfirmationDialog(Runnable onSuccess) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pin_confirm, null);
        EditText etPin = dialogView.findViewById(R.id.et_confirm_pin);

        new AlertDialog.Builder(getContext())
                .setTitle("Confirm PIN")
                .setView(dialogView)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    String enteredPin = etPin.getText().toString();
                    String userId = "";
                    Cursor c = db.getUser();
                    if(c != null && c.moveToFirst()) userId = c.getString(0);
                    if(c != null) c.close();

                    String storedHash = db.getUserPin(userId);
                    String enteredHash = HashUtils.hashPin(enteredPin);
                    if (enteredHash != null && enteredHash.equals(storedHash)) {
                        onSuccess.run();
                    } else {
                        Toast.makeText(getContext(), "Incorrect PIN", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void clearCache() {
        try {
            File cacheDir = requireContext().getCacheDir();
            if (cacheDir != null && cacheDir.isDirectory()) {
                deleteDir(cacheDir);
            }
            Toast.makeText(getContext(), "Cache cleared successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Failed to clear cache", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            assert children != null;
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) return false;
            }
        }
        assert dir != null;
        return dir.delete();
    }

    private void logout() {
        SharedPrefs.getInstance().saveBoolean("isLoggedIn", false);
        db.deleteUser();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
