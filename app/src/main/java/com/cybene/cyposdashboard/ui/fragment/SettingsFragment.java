package com.cybene.cyposdashboard.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.ui.auth.LoginActivity;
import com.cybene.cyposdashboard.ui.auth.SetPinActivity;
import com.cybene.cyposdashboard.utils.db.Db;
import com.cybene.cyposdashboard.utils.db.SharedPrefs;

public class SettingsFragment extends Fragment {

    private TextView tvName, tvEmail;
    private Db db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        db = new Db(getContext());
        tvName = view.findViewById(R.id.settings_name);
        tvEmail = view.findViewById(R.id.settings_email);
        Button btnChangePin = view.findViewById(R.id.btn_change_pin);
        Button btnLogout = view.findViewById(R.id.btn_logout);

        loadUserData();

        btnChangePin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SetPinActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            SharedPrefs.getInstance().saveBoolean("isLoggedIn", false);
            db.deleteUser();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

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
}
