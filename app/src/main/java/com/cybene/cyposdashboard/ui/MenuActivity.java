package com.cybene.cyposdashboard.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.ui.auth.LoginActivity;
import com.cybene.cyposdashboard.ui.auth.PasswordResetActivity;
import com.cybene.cyposdashboard.utils.Converter;
import com.cybene.cyposdashboard.utils.db.Db;
import com.cybene.cyposdashboard.utils.db.SharedPrefs;
import com.cybene.cyposdashboard.utils.interfaces.AddOrRemoveCallbacks;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MenuActivity extends AppCompatActivity implements AddOrRemoveCallbacks {

    private AppBarConfiguration mAppBarConfiguration;
    private static final int count= Integer.parseInt(SharedPrefs.getInstance().getString("notification_content"));
    private Db myDb;
    TextView name, email;

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
                R.id.nav_customer, R.id.nav_supplier, R.id.nav_profile, R.id.nav_settings,
                R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_logout) {
                SharedPrefs.getInstance().saveBoolean("isLoggedIn", false);
                myDb.deleteUser();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_settings) {
                Toast.makeText(this, "Settings coming soon", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, PasswordResetActivity.class));
                return true;
            } else {
                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                if (handled) {
                    drawer.closeDrawers();
                }
                return handled;
            }
        });
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
            SharedPrefs.getInstance().saveBoolean("isLoggedIn", false);
            myDb.deleteUser();
            Intent Main = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(Main);
            finish();
            return true;
        }else if(item.getItemId() == R.id.action_settings){
            Toast.makeText(this,"Module not yet available",Toast.LENGTH_LONG).show();
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