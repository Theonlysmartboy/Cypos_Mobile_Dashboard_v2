package com.cybene.cyposdashboard.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.ui.activities.auth.AuthGateActivity;
import com.cybene.cyposdashboard.ui.activities.auth.LoginActivity;
import com.cybene.cyposdashboard.ui.activities.auth.SetPinActivity;
import com.cybene.cyposdashboard.utils.db.Db;

import java.util.HashMap;
import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        // hide the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        splash();
    }
    private void splash() {
        new Handler().postDelayed(() -> {
            Db db = new Db(getApplicationContext());
            
            // 1. Check Configuration (Server URL)
            HashMap<String, String> config = db.getConfig();
            if (config.isEmpty()) {
                Intent welcome = new Intent(SplashActivity.this, WelcomeActivity.class);
                startActivity(welcome);
                finish();
                return;
            }

            // 2. Check User
            android.database.Cursor cursor = db.getUser();
            String userId = null;
            if (cursor.moveToFirst()) {
                userId = cursor.getString(0);
            }
            cursor.close();

            if (userId == null) {
                // No user -> Go to Login
                Intent login = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(login);
            } else {
                // User exists -> Check for PIN
                if (db.hasUserPin(userId)) {
                    // Subsequent launch or after logout -> Biometric/PIN gate
                    Intent authGate = new Intent(SplashActivity.this, AuthGateActivity.class);
                    startActivity(authGate);
                } else {
                    // Logged in but no PIN set yet
                    Intent setPin = new Intent(SplashActivity.this, SetPinActivity.class);
                    startActivity(setPin);
                }
            }
            finish();
        },3000);
    }
}