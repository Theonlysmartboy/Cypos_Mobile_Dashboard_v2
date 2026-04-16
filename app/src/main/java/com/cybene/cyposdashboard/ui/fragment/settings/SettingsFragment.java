package com.cybene.cyposdashboard.ui.fragment.settings;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cybene.cyposdashboard.BuildConfig;
import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.ui.auth.LoginActivity;
import com.cybene.cyposdashboard.ui.auth.SetPinActivity;
import com.cybene.cyposdashboard.utils.AppController;
import com.cybene.cyposdashboard.utils.HashUtils;
import com.cybene.cyposdashboard.utils.db.Db;
import com.cybene.cyposdashboard.utils.db.SharedPrefs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SettingsFragment extends Fragment {

    private TextView tvName, tvEmail, tvServerStatus, tvAppVersion, tvStorageUsage, tvDeviceInfo;
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
        tvServerStatus = view.findViewById(R.id.tv_server_status);
        tvAppVersion = view.findViewById(R.id.tv_app_version);
        tvStorageUsage = view.findViewById(R.id.tv_storage_usage);
        tvDeviceInfo = view.findViewById(R.id.tv_device_info);
        spinnerIdleTime = view.findViewById(R.id.spinner_idle_time);
        spinnerTheme = view.findViewById(R.id.spinner_theme);
        
        Button btnChangePin = view.findViewById(R.id.btn_change_pin);
        Button btnUpdateUrl = view.findViewById(R.id.btn_update_url);
        Button btnClearCache = view.findViewById(R.id.btn_clear_cache);
        Button btnViewLogs = view.findViewById(R.id.btn_view_logs);
        Button btnCopyDeviceInfo = view.findViewById(R.id.btn_copy_device_info);
        Button btnLogout = view.findViewById(R.id.btn_logout);

        loadUserData();
        setupSpinners();
        loadConfig();
        loadAppInfo();
        checkServerStatus();

        btnChangePin.setOnClickListener(v -> startActivity(new Intent(getActivity(), SetPinActivity.class)));

        btnUpdateUrl.setOnClickListener(v -> {
            String newUrl = etServerUrl.getText().toString().trim();
            if (!newUrl.isEmpty()) {
                db.updateConfig("url", newUrl);
                Toast.makeText(getContext(), "Server URL updated", Toast.LENGTH_SHORT).show();
                checkServerStatus();
            }
        });

        btnClearCache.setOnClickListener(v -> showPinConfirmationDialog(this::clearCache));

        btnViewLogs.setOnClickListener(v -> showLogsDialog());

        btnCopyDeviceInfo.setOnClickListener(v -> copyDeviceInfo());

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

    private void loadAppInfo() {
        tvAppVersion.setText(getString(R.string.version_format, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
        
        long cacheSize = getDirSize(requireContext().getCacheDir());
        long dbSize = getDirSize(requireContext().getDatabasePath("cypos.db"));
        tvStorageUsage.setText(getString(R.string.storage_usage_format, Formatter.formatFileSize(getContext(), cacheSize + dbSize)));

        String deviceInfo = getString(R.string.device_info_format, Build.MODEL, Build.VERSION.RELEASE, Build.VERSION.SDK_INT);
        tvDeviceInfo.setText(deviceInfo);
    }

    private long getDirSize(File dir) {
        long size = 0;
        if (dir != null && dir.exists()) {
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        size += getDirSize(file);
                    }
                }
            } else {
                size += dir.length();
            }
        }
        return size;
    }

    private void checkServerStatus() {
        String url = etServerUrl.getText().toString().trim();
        if (url.isEmpty()) {
            tvServerStatus.setText(R.string.status_url_not_set);
            tvServerStatus.setTextColor(Color.GRAY);
            return;
        }

        StringRequest request = new StringRequest(Request.Method.GET, url, 
            response -> {
                if (isAdded()) {
                    tvServerStatus.setText(R.string.status_online);
                    tvServerStatus.setTextColor(Color.parseColor("#4CAF50")); // Green
                }
            }, 
            error -> {
                if (isAdded()) {
                    tvServerStatus.setText(R.string.status_offline);
                    tvServerStatus.setTextColor(Color.RED);
                }
            });
        AppController.getInstance().addToRequestQueue(request);
    }

    private void copyDeviceInfo() {
        String info = getString(R.string.app_version_label, BuildConfig.VERSION_NAME) + "\n" + tvDeviceInfo.getText().toString();
        ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Device Info", info);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), "Device information copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    private void setupSpinners() {
        // Idle Time Spinner
        String[] idleOptions = {"Never", "1 Minute", "3 Minutes", "5 Minutes", "10 Minutes", "15 Minutes", "30 Minutes"};
        int[] idleValues = {0, 1, 3, 5, 10, 15, 30};
        ArrayAdapter<String> idleAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, idleOptions);
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
        ArrayAdapter<String> themeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, themeOptions);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTheme.setAdapter(themeAdapter);

        int currentTheme = SharedPrefs.getInstance().getInt(PREF_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
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
                .setTitle("Confirm Action")
                .setMessage("Please enter your PIN to proceed.")
                .setView(dialogView)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    String enteredPin = etPin.getText().toString();
                    String userId = "";
                    Cursor c = db.getUser();
                    if(c != null && c.moveToFirst()) userId = c.getString(0);
                    if(c != null) c.close();

                    String storedHash = db.getUserPin(userId);
                    String enteredHash = HashUtils.hashPin(enteredPin);
                    if (storedHash != null && enteredHash != null && enteredHash.equals(storedHash)) {
                        onSuccess.run();
                    } else {
                        Toast.makeText(getContext(), "Incorrect PIN", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showLogsDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_view_logs, null);
        TextView tvLogs = dialogView.findViewById(R.id.tv_logs_content);
        Button btnExport = dialogView.findViewById(R.id.btn_export_logs);
        Button btnClose = dialogView.findViewById(R.id.btn_close_logs);

        StringBuilder log = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int count = 0;
            while ((line = bufferedReader.readLine()) != null && count < 500) {
                log.append(line).append("\n");
                count++;
            }
        } catch (IOException e) {
            log.append("Error reading logs: ").append(e.getMessage());
        }

        tvLogs.setText(log.toString());

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setCancelable(true)
                .create();

        btnExport.setOnClickListener(v -> {
            exportLogs(log.toString());
            dialog.dismiss();
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void exportLogs(String logs) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "export_" + timeStamp + ".txt";
        
        File file = new File(requireContext().getExternalFilesDir(null), fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(logs.getBytes());
            Toast.makeText(getContext(), "Logs exported to " + fileName, Toast.LENGTH_LONG).show();
            openFile(file);
        } catch (IOException e) {
            Toast.makeText(getContext(), "Failed to export logs: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openFile(File file) {
        Uri uri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".fileprovider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "text/plain");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Open Log File"));
    }

    private void clearCache() {
        try {
            File cacheDir = requireContext().getCacheDir();
            if (cacheDir != null && cacheDir.isDirectory()) {
                deleteDir(cacheDir);
            }
            loadAppInfo();
            Toast.makeText(getContext(), "Cache cleared successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Failed to clear cache", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) return false;
                }
            }
        }
        return dir != null && dir.delete();
    }

    private void logout() {
        SharedPrefs.getInstance().saveBoolean("isLoggedIn", false);
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}