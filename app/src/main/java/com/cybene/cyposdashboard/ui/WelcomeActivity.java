package com.cybene.cyposdashboard.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.ui.auth.LoginActivity;
import com.cybene.cyposdashboard.utils.adapter.WelcomeViewPagerAdapter;
import com.cybene.cyposdashboard.utils.items.WelcomeItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WelcomeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    WelcomeViewPagerAdapter pagerAdapter;
    TabLayout layout;
    Button next, btnGetStarted, btnConfig;
    int position = 0 ;
    Animation btnAnim ;
    TextView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // when this activity is about to be launch we need to check if its opened before or not
        if (restorePrefData()) {
            Intent launcherActivity = new Intent(getApplicationContext(), SplashActivity.class );
            startActivity(launcherActivity);
            finish();
        }
         setContentView(R.layout.activity_welcome);
        // hide the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        // init views
        viewPager = findViewById(R.id.wPager);
        layout = findViewById(R.id.wTabs);
        btnGetStarted = findViewById(R.id.btn_get_started);
        btnConfig = findViewById(R.id.btn_configure);
        skip = findViewById(R.id.tv_skip);
        next = findViewById(R.id.btnWNext);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        // fill list screen
        final List<WelcomeItem> list = new ArrayList<>();
        list.add(new WelcomeItem("CyPOS","Cybene Technologies\n" +
                "Providing Software Solutions for Business", R.drawable.logo));
        list.add(new WelcomeItem("CyPOS","Cybene Technologies\n" +
                "Providing Software Solutions for Business", R.drawable.cypos_from));
        list.add(new WelcomeItem("CyPOS","Cybene Technologies\n" +
                "Providing Software Solutions for Business", R.drawable.app_image));
        // setup viewpager
        pagerAdapter = new WelcomeViewPagerAdapter(this,list);
        viewPager.setAdapter(pagerAdapter);
        // setup tab layout with viewpager
        layout.setupWithViewPager(viewPager);
        // next button click Listener
        next.setOnClickListener(view -> {
            position = viewPager.getCurrentItem();
            if (position < list.size()) {
                position++;
                viewPager.setCurrentItem(position);
            }
            if (position == list.size()-1) { // when we reach to the last screen
                loadLastScreen();
            }
        });
        // tab layout add change listener
        layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == list.size()-1) {
                    loadLastScreen();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        // Get Started button click listener

        btnGetStarted.setOnClickListener(v -> {
            //open main activity
            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginActivity);
            //SAVE a boolean value to show that user has already seen the welcome screen
            savePrefsData();
            finish();
        });
        // skip button click listener
        skip.setOnClickListener(v -> viewPager.setCurrentItem(list.size()));
        //configure button click listener
        btnConfig.setOnClickListener(view -> {
            Intent config = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(config);
            //SAVE a boolean value to show that user has already seen the welcome screen
            savePrefsData();
            finish();
        });
    }
    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        return pref.getBoolean("isIntroOpened",false);
    }
    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.apply();
    }
    // show the GET STARTED Button and hide the indicator and the next button
    private void loadLastScreen() {
        next.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        btnConfig.setVisibility(View.VISIBLE);
        skip.setVisibility(View.INVISIBLE);
        layout.setVisibility(View.INVISIBLE);
        // setup animation
        btnGetStarted.setAnimation(btnAnim);
        btnConfig.setAnimation(btnAnim);
    }
}