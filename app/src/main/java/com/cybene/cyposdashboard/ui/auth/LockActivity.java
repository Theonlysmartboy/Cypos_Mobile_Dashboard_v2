package com.cybene.cyposdashboard.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.ui.MenuActivity;
import com.cybene.cyposdashboard.utils.HashUtils;
import com.cybene.cyposdashboard.utils.db.Db;

public class LockActivity extends AppCompatActivity {

    private EditText pin1, pin2, pin3, pin4;
    private String userId;
    private String storedHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        Db db = new Db(this);
        android.database.Cursor cursor = db.getUser();
        if (cursor.moveToFirst()) {
            userId = cursor.getString(0);
        }
        cursor.close();
        
        storedHash = db.getUserPin(userId);

        pin1 = findViewById(R.id.pin_1);
        pin2 = findViewById(R.id.pin_2);
        pin3 = findViewById(R.id.pin_3);
        pin4 = findViewById(R.id.pin_4);

        setupPinInputs();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Prevent back to app while locked
                moveTaskToBack(true);
            }
        });
    }

    private void setupPinInputs() {
        EditText[] pins = {pin1, pin2, pin3, pin4};
        for (int i = 0; i < pins.length; i++) {
            final int index = i;
            pins[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1) {
                        if (index < pins.length - 1) {
                            pins[index + 1].requestFocus();
                        } else {
                            validatePin();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            pins[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (pins[index].getText().length() == 0 && index > 0) {
                        pins[index - 1].requestFocus();
                        pins[index - 1].setText("");
                        return true;
                    }
                }
                return false;
            });
        }
    }

    private void validatePin() {
        String enteredPin = pin1.getText().toString() + pin2.getText().toString() + 
                            pin3.getText().toString() + pin4.getText().toString();
        
        String hashedInput = HashUtils.hashPin(enteredPin);
        
        if (storedHash != null && storedHash.equals(hashedInput)) {
            startActivity(new Intent(this, MenuActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
            clearPins();
        }
    }

    private void clearPins() {
        pin1.setText("");
        pin2.setText("");
        pin3.setText("");
        pin4.setText("");
        pin1.requestFocus();
    }
}
