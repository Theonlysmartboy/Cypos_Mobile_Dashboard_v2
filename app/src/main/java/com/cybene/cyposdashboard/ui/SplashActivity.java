package com.cybene.cyposdashboard.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.ui.auth.LoginActivity;
import com.cybene.cyposdashboard.utils.db.SharedPrefs;

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
        getSupportActionBar().hide();
        splash();
    }
    private void splash() {
        new Handler().postDelayed(() -> {
            // if User has logged in
            //Show the Menu
            if(SharedPrefs.getInstance().getString("isLoggedIn").equalsIgnoreCase("")) {
                //User is not yet logged in
                // show the login activity
                Intent login = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
            else {
                Intent home = new Intent(SplashActivity.this, MenuActivity.class);
                startActivity(home);
                finish();
            }
        },5000);

    }
}