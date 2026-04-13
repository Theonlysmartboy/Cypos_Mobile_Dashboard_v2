package com.cybene.cyposdashboard.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.ui.auth.LoginActivity;
import com.cybene.cyposdashboard.utils.db.SharedPrefs;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {
    private SharedPrefs session;

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
        session = new SharedPrefs(getApplicationContext());
        splash();
    }
    private void splash() {
        new Handler().postDelayed(() -> {
            // if User has logged in
            //Show Menu screen
            if(session.isLoggedIn()){
                Intent home = new Intent(SplashActivity.this, MenuActivity.class);
                startActivity(home);
                finish();
            }
            //Otherwise show login screen
            else{
                Intent login = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        },5000);

    }
}