package com.cybene.cyposdashboard.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SharedMemory;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.TrailingCircularDotsLoader;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.ui.MenuActivity;
import com.cybene.cyposdashboard.utils.AppConfig;
import com.cybene.cyposdashboard.utils.AppController;
import com.cybene.cyposdashboard.utils.ValidateInput;
import com.cybene.cyposdashboard.utils.db.Db;
import com.cybene.cyposdashboard.utils.db.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView register,reset;
    Button login;
    AutoCompleteTextView email;
    EditText password;
    CheckBox remember;
    ImageView view;
    androidx.constraintlayout.widget.ConstraintLayout container;
    TrailingCircularDotsLoader trailingCircularDotsLoader;
    private SharedPrefs session;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Db myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        // hide the action bar
        getSupportActionBar().hide();
        initializeComponents();
        setControlActionListeners();
    }
    private void initializeComponents() {
        register = findViewById(R.id.sign_up);
        reset = findViewById(R.id.pwdReset);
        login = findViewById(R.id.btnLogin);
        email = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPass);
        remember = findViewById(R.id.rememberMe);
        container = findViewById(R.id.container);
        view = findViewById(R.id.view);
        myDb = new Db(this);
        session = new SharedPrefs(getApplicationContext());
        trailingCircularDotsLoader = new TrailingCircularDotsLoader(
                this, 24, ContextCompat.getColor(this, android.R.color.holo_blue_dark), 100, 5);
        trailingCircularDotsLoader.setAnimDuration(1200);
        trailingCircularDotsLoader.setAnimDelay(200);
    }
    private void setControlActionListeners() {
        register.setOnClickListener(this);
        reset.setOnClickListener(this);
        login.setOnClickListener(this);
        remember.setOnClickListener(this);
        view.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.sign_up:
                loadSignUp();
                break;
            case R.id.pwdReset:
                loadPwdRecovery();
                break;
            case R.id.btnLogin:
                showDialog();
                String emailVal = email.getText().toString().trim();
                String passwordVal = password.getText().toString().trim();
                if(validateInput(emailVal, passwordVal)){
                    auth(emailVal,passwordVal);
                }
                break;
            case R.id.view:
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
                break;
        }
    }
    private void auth(final String emailVal, final String passwordVal) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        //showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        //check if remember is checked
                        if(remember.isChecked()){
                            // Create login session
                            session.saveString("isLoggedIn", "true");
                        }

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("cname");
                        String email = user.getString("email");
                        myDb.storeUser(name,email);
                        Toast.makeText(getApplicationContext(),"Welcome " + name+" email "+email,Toast.LENGTH_LONG).show();
                        // Launch main activity
                        loadMain();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),"Login Error "+errorMsg, Toast.LENGTH_LONG).show();
                        login.setEnabled(true);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    login.setEnabled(true);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " +error + ">>" + error.networkResponse.statusCode
                        + ">>" + error.networkResponse.data
                        + ">>" + error.getCause()
                        + ">>" + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error Loging In please try again "+error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
                login.setEnabled(true);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("email", emailVal);
                params.put("password", passwordVal);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private boolean validateInput(String emailVal, String passwordVal) {
        ValidateInput validateInput = new ValidateInput();
        boolean valid = true;
        if (!validateInput.validateText(emailVal)){
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
    private void showDialog() {
            container.addView(trailingCircularDotsLoader);
    }

    private void hideDialog() {
        container.removeView(trailingCircularDotsLoader);
    }
    private void loadMain() {
        Intent loadMain = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(loadMain);
        finish();
    }
    private void loadPwdRecovery() {
        Intent showPwdRecovery = new Intent(LoginActivity.this,PasswordRecoveryActivity.class);
        startActivity(showPwdRecovery);
    }

    private void loadSignUp() {
        Intent showSignup = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(showSignup);
    }
}