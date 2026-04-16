package com.cybene.cyposdashboard.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.ui.activities.auth.LoginActivity;
import com.cybene.cyposdashboard.utils.db.Db;

public class MainActivity extends AppCompatActivity {
    AutoCompleteTextView url;
    Button save;
    Db db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        url = findViewById(R.id.txtUrl);
        save = findViewById(R.id.submit);
        save.setOnClickListener(view -> {
            String link = url.getText().toString().trim();
            db = new Db(getApplication());
            db.deleteConfig();
            if(db.storeConfig(link)){
                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
            }else{
                Toast.makeText(this,"Unable to save url",Toast.LENGTH_LONG).show();
            }
        });
    }
}