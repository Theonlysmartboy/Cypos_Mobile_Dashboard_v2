package com.cybene.cyposdashboard.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.ui.activities.auth.AuthGateActivity;
import com.cybene.cyposdashboard.ui.activities.auth.LockActivity;
import com.cybene.cyposdashboard.ui.activities.auth.PasswordResetActivity;
import com.cybene.cyposdashboard.utils.Converter;
import com.cybene.cyposdashboard.utils.db.Db;
import com.cybene.cyposdashboard.utils.db.SharedPrefs;
import com.cybene.cyposdashboard.utils.interfaces.AddOrRemoveCallbacks;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MenuActivity extends AppCompatActivity implements AddOrRemoveCallbacks {

    private AppBarConfiguration mAppBarConfiguration;
    private Db myDb;
    TextView name, email;
    private long lastInteractionTime;
    private static final String PREF_IDLE_TIMEOUT = "idle_timeout"; // in minutes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        myDb = new Db(this);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        name = headerView.findViewById(R.id.name);
        email = headerView.findViewById(R.id.textView);
        //get user details from sync db
        Cursor userData = myDb.getUser();
        if (userData.getCount() > 0) {
            userData.moveToFirst();
            do {
                name.setText(userData.getString(1));
                email.setText(userData.getString(2));
            } while (userData.moveToNext());
            userData.close();
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_sales,
                R.id.nav_purchase, R.id.nav_inventory, R.id.nav_accounts, R.id.nav_branch,
                R.id.nav_customer, R.id.nav_supplier, R.id.nav_notifications, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_logout) {
                logout();
                return true;
            } else {
                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                if (handled) {
                    drawer.closeDrawers();
                }
                return handled;
            }
        });

        lastInteractionTime = System.currentTimeMillis();
    }

    private void logout() {
        SharedPrefs.getInstance().saveBoolean("isLoggedIn", false);
        // Do NOT delete user or PIN to allow Biometric/PIN login after logout
        Intent intent = new Intent(this, AuthGateActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        lastInteractionTime = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIdleTime();
    }

    private void checkIdleTime() {
        int timeoutMinutes = SharedPrefs.getInstance().getInt(PREF_IDLE_TIMEOUT, 0);
        if (timeoutMinutes > 0) {
            long idleMillis = System.currentTimeMillis() - lastInteractionTime;
            if (idleMillis > (long) timeoutMinutes * 60 * 1000) {
                // Lock the app
                Intent intent = new Intent(this, LockActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("IS_IDLE_LOCK", true);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_notifications);
        int unreadCount = myDb.getUnreadCount();
        menuItem.setIcon(Converter.convertLayoutToImage(MenuActivity.this, unreadCount, R.drawable.ic_baseline_notifications));

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_notifications) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            navController.navigate(R.id.nav_notifications);
            return true;
        } else if (item.getItemId() == R.id.action_profile) {
            Intent profile = new Intent(MenuActivity.this, PasswordResetActivity.class);
            startActivity(profile);
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }else if(item.getItemId() == R.id.action_settings){
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            navController.navigate(R.id.nav_settings);
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onNewNotificationReceived() {
        invalidateOptionsMenu();
        Snackbar.make(findViewById(R.id.parentContainer), "You have a new Notification !!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onReadNotification() {
        invalidateOptionsMenu();
    }
}