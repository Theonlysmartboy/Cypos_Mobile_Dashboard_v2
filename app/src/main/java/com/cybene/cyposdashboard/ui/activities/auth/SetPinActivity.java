package com.cybene.cyposdashboard.ui.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.ui.activities.MenuActivity;
import com.cybene.cyposdashboard.utils.HashUtils;
import com.cybene.cyposdashboard.utils.db.Db;

public class SetPinActivity extends AppCompatActivity {

    private EditText pin1, pin2, pin3, pin4;
    private TextView tvTitle, tvInstruction;
    private Button btnAction;
    private String firstPin = "";
    private boolean isConfirming = false;
    private Db db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pin);
        db = new Db(this);
        android.database.Cursor cursor = db.getUser();
        if (cursor.moveToFirst()) {
            userId = cursor.getString(0);
        }
        cursor.close();
        pin1 = findViewById(R.id.pin_1);
        pin2 = findViewById(R.id.pin_2);
        pin3 = findViewById(R.id.pin_3);
        pin4 = findViewById(R.id.pin_4);
        tvTitle = findViewById(R.id.tv_title);
        tvInstruction = findViewById(R.id.tv_instruction);
        btnAction = findViewById(R.id.btn_action);
        clearPins();
        setupPinInputs();
        btnAction.setOnClickListener(v -> handleAction());
    }

    private void setupPinInputs() {
        EditText[] pins = {pin1, pin2, pin3, pin4};
        for (int i = 0; i < pins.length; i++) {
            final int index = i;
            pins[i].setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            pins[i].setTransformationMethod(new PasswordTransformationMethod());
            pins[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < pins.length - 1) {
                        new Handler(Looper.getMainLooper()).postDelayed(() -> pins[index + 1].requestFocus(), 100);
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void handleAction() {
        String pin = pin1.getText().toString() + pin2.getText().toString() + 
                     pin3.getText().toString() + pin4.getText().toString();
        if (pin.length() < 4) {
            Toast.makeText(this, "Enter 4 digits", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isConfirming) {
            firstPin = pin;
            isConfirming = true;
            clearPins();
            tvTitle.setText(R.string.confirm_pin);
            tvInstruction.setText(R.string.re_enter_your_pin);
            btnAction.setText(R.string.finish);
        } else {
            if (pin.equals(firstPin)) {
                String hashedPin = HashUtils.hashPin(pin);
                if (db.updateUserPin(userId, hashedPin)) {
                    startActivity(new Intent(this, MenuActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Error saving PIN", Toast.LENGTH_SHORT).show();
                    clearPins();
                }
            } else {
                Toast.makeText(this, "PINs do not match", Toast.LENGTH_SHORT).show();
                clearPins();
            }
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
