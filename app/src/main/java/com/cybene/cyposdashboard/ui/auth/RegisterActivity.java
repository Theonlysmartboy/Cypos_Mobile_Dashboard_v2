package com.cybene.cyposdashboard.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.AppConfig;
import com.cybene.cyposdashboard.utils.AppController;
import com.cybene.cyposdashboard.utils.KeyGenerator;
import com.cybene.cyposdashboard.utils.TrailingDotsLoader;
import com.cybene.cyposdashboard.utils.ValidateInput;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    private AutoCompleteTextView cName,email;
    private EditText password, cPassword, key;
    private CheckBox accept;
    private Button reg;
    private TextView signIn;
    private String cnameVal, emailVal, passwordVal, keyVal;
    ImageView show, view;
    TrailingDotsLoader trailingCircularDotsLoader;
    LinearLayout container;
    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        // hide the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        initializeComponents();
        setControlActionListeners();
    }
    private void initializeComponents() {
        cName = findViewById(R.id.txtRcName);
        email = findViewById(R.id.txtREmail);
        password = findViewById(R.id.txtRPass);
        cPassword = findViewById(R.id.txtCPass);
        key = findViewById(R.id.txtKey);
        accept = findViewById(R.id.btnConfirm);
        reg = findViewById(R.id.btnRegister);
        signIn = findViewById(R.id.btnLoginLink);
        container = findViewById(R.id.container);
        show = findViewById(R.id.show);
        view = findViewById(R.id.view);
        KeyGenerator keyGenerator = new KeyGenerator();
        String activationKey = KeyGenerator.generateKey(4);
        key.setText(activationKey);
         trailingCircularDotsLoader = new TrailingDotsLoader(this);
        trailingCircularDotsLoader.setPrimaryColor(Color.parseColor(AppConfig.loaderPrimaryColor));
        trailingCircularDotsLoader.setSecondaryColor(Color.parseColor(AppConfig.loaderSecondaryColor));
        trailingCircularDotsLoader.setDotCount(AppConfig.loaderDotsCount);
        trailingCircularDotsLoader.setDotRadius(AppConfig.loaderDotsRadius);
        trailingCircularDotsLoader.setAnimationDuration(AppConfig.loaderAnimationDuration);
    }
    private void setControlActionListeners() {
        cPassword.addTextChangedListener(this);
        reg.setOnClickListener(this);
        signIn.setOnClickListener(this);
        show.setOnClickListener(this);
        view.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(cPassword.getText().length()<8 && !cPassword.getText().toString().trim().equals(password.getText().toString().trim())){
            cPassword.setError("Password mismatch");
            reg.setEnabled(false);
        }else{
            reg.setEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnRegister){
            showDialog();
            cnameVal = cName.getText().toString().trim();
            emailVal = email.getText().toString().trim();
            passwordVal = password.getText().toString().trim();
            keyVal = key.getText().toString().trim();
            if(valid(cnameVal,emailVal,passwordVal)){
                createUser(cnameVal,emailVal,passwordVal,keyVal);
            }
        } else if (view.getId() == R.id.btnLoginLink) {
            showLogin();
        } else if (view.getId() == R.id.show) {
            if(cPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                show.setImageResource(R.drawable.ic_visibility_off);
                //Show Password
                cPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                show.setImageResource(R.drawable.ic_visibility);
                //Hide Password
                cPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        } else {
            if(password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_visibility_off);
                //Show Password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_visibility);
                //Hide Password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    private boolean valid(String cnameVal, String emailVal, String passwordVal) {
        ValidateInput validateInput = new ValidateInput();
        boolean valid = true;
        if(!validateInput.validateText(cnameVal)){
           container.removeView(trailingCircularDotsLoader);
            cName.setError("Company name is required");
            valid = false;
        }else if (!validateInput.validateText(emailVal)){
            container.removeView(trailingCircularDotsLoader);
            email.setError("Email address is required");
            valid = false;
        }else if(!validateInput.validateEmail(emailVal)){
            container.removeView(trailingCircularDotsLoader);
            email.setError("Enter a valid email address");
            valid = false;
        }else if(!validateInput.validateText(passwordVal)){
            container.removeView(trailingCircularDotsLoader);
            password.setError("Password is required");
            valid = false;
        }else if(!validateInput.validatePassword(passwordVal)){
            container.removeView(trailingCircularDotsLoader);
            password.setError("Password must contain at least one lowercase letter, one uppercase " +
                    "letter, one special character one numeric character and must be at least " +
                    "8 characters long");
            valid = false;
        }
        return valid;
    }

    private void createUser(final String cnameVal, final String emailVal, final String passwordVal, final String keyVal) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_REGISTER, response -> {
            Log.d(TAG, "Register Response: " + response);
            hideDialog();
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {
                    // User successfully stored in MySQL
                    Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
                    // Launch login activity
                    showLogin();
                } else {

                    // Error occurred in registration. Get the error
                    // message
                    String errorMsg = jObj.getString("error_msg");
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Log.e(TAG, "createUser: ", e );
            }

        }, error -> {
            Log.e(TAG, "Registration Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(), " An error has occurred during registration "+error.getMessage(), Toast.LENGTH_LONG).show();
            hideDialog();
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("cname", cnameVal);
                params.put("email", emailVal);
                params.put("password", passwordVal);
                params.put("key", keyVal);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showLogin(){
        Intent login = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(login);
        finish();
    }

    private void showDialog() {
        container.addView(trailingCircularDotsLoader);
    }

    private void hideDialog() {
        container.removeView(trailingCircularDotsLoader);
    }
}