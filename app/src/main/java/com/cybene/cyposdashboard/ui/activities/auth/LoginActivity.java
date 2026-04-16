package com.cybene.cyposdashboard.ui.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.ui.activities.MenuActivity;
import com.cybene.cyposdashboard.utils.AppConfig;
import com.cybene.cyposdashboard.utils.NetworkUtils;
import com.cybene.cyposdashboard.utils.TrailingDotsLoader;
import com.cybene.cyposdashboard.utils.ValidateInput;
import com.cybene.cyposdashboard.utils.db.Db;
import com.cybene.cyposdashboard.utils.db.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView register,reset;
    Button login;
    AutoCompleteTextView email;
    EditText password;
    CheckBox remember;
    androidx.constraintlayout.widget.ConstraintLayout container;
    private SharedPrefs session;
    private static final String TAG = LoginActivity.class.getSimpleName();
    TrailingDotsLoader trailingCircularDotsLoader;
    private Db myDb;

    /**
     * Called when the activity is first created.
     * This is where you should do all of your normal static set up: create views,
     * bind data to lists, etc. This method also provides you with a Bundle containing
     * the activity's previously frozen state, if there was one.
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        // hide the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        initializeComponents();
        setControlActionListeners();
    }

    /**
     * Initializes the UI components and other necessary objects.
     */
    private void initializeComponents() {
        register = findViewById(R.id.sign_up);
        reset = findViewById(R.id.pwdReset);
        login = findViewById(R.id.btnLogin);
        email = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPass);
        remember = findViewById(R.id.rememberMe);
        container = findViewById(R.id.container);
        myDb = new Db(this);
        session = new SharedPrefs(getApplicationContext());
        trailingCircularDotsLoader = new TrailingDotsLoader(this);
        trailingCircularDotsLoader.setPrimaryColor(Color.parseColor(AppConfig.loaderPrimaryColor));
        trailingCircularDotsLoader.setSecondaryColor(Color.parseColor(AppConfig.loaderSecondaryColor));
        trailingCircularDotsLoader.setDotCount(AppConfig.loaderDotsCount);
        trailingCircularDotsLoader.setDotRadius(AppConfig.loaderDotsRadius);
        trailingCircularDotsLoader.setAnimationDuration(AppConfig.loaderAnimationDuration);
    }

    /**
     * Sets onClick listeners for the interactive UI elements.
     */

    private void setControlActionListeners() {
        register.setOnClickListener(this);
        reset.setOnClickListener(this);
        login.setOnClickListener(this);
        remember.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.sign_up){
            loadSignUp();
        } else if (view.getId() == R.id.pwdReset) {
            loadPwdRecovery();
        } else if (view.getId() == R.id.btnLogin) {
            showDialog();
            String emailVal = email.getText().toString().trim();
            String passwordVal = password.getText().toString().trim();
            if(validateInput(emailVal, passwordVal)){
                auth(emailVal,passwordVal);
            }
        }
    }

    /**
     * Authenticates the user with the provided credentials.
     *
     * @param emailVal    The user's email.
     * @param passwordVal The user's password.
     */

    private void auth(final String emailVal, final String passwordVal) {
        //showDialog();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetDialog(this, true); // true = allow exit
            return; // Stop further execution
        }

        StringRequest strLoginReq = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, response -> {
            Log.d(TAG, "Login Response: " + response);
            hideDialog();
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                // Check for error node in JSON
                if (!error) {
                    // user successfully logged in
                    //check if remember is checked
                    if(remember.isChecked()){
                        // Create login session
                        session.saveBoolean("isLoggedIn", true);
                    }

                    // Now store the user in SQLite
                    String uid = jObj.getString("uid");
                    JSONObject user = jObj.getJSONObject("user");
                    String name = user.getString("cname");
                    String email = user.getString("email");
                    if(myDb.storeUser(uid, name,email)){
                    Toast.makeText(getApplicationContext(),"Welcome " + name+" email "+email,Toast.LENGTH_LONG).show();
                    // Launch main activity
                    loadMain();}
                    else{
                        Toast.makeText(getApplicationContext(),"Unable To write to local database ", Toast.LENGTH_LONG).show();
                        login.setEnabled(true);
                    }
                } else {
                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("error_msg");
                    Toast.makeText(getApplicationContext(),"Login Error "+errorMsg, Toast.LENGTH_LONG).show();
                    login.setEnabled(true);
                }
            } catch (JSONException e) {
                // JSON error
                Log.e(TAG, "auth: ", e );
                Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                login.setEnabled(true);
            }

        }, error -> {
            if (error.networkResponse == null) {
                Log.e(TAG, "Network error: " + error);
                Toast.makeText(this, "Network error: Check your connection", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "Login Error: "
                        + error.networkResponse.statusCode + " >> "
                        + new String(error.networkResponse.data));
                Toast.makeText(getApplicationContext(),
                        "Server error: " + error.networkResponse.statusCode,
                        Toast.LENGTH_LONG).show();
            }

            hideDialog();
            login.setEnabled(true);
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

        strLoginReq.setRetryPolicy(new DefaultRetryPolicy(
                1000*10,
                /*DefaultRetryPolicy.DEFAULT_MAX_RETRIES*/ 3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue loginRequestQue = Volley.newRequestQueue(this);
        loginRequestQue.add(strLoginReq);
    }

    /**
     * Validates the login input fields.
     *
     * @param emailVal    The email to validate.
     * @param passwordVal The password to validate.
     * @return True if the input is valid, false otherwise.
     */
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

    /**
     * Shows the loading dialog.
     */
    private void showDialog() {
            container.addView(trailingCircularDotsLoader);
    }

    /**
     * Hides the loading dialog.
     */
    private void hideDialog() {
        container.removeView(trailingCircularDotsLoader);
    }
    /**
     * Loads the main menu activity.
     */
    private void loadMain() {
        Intent intent;
        android.database.Cursor cursor = myDb.getUser();
        String userId = null;
        if (cursor.moveToFirst()) {
            userId = cursor.getString(0);
        }
        cursor.close();

        if (userId != null && myDb.hasUserPin(userId)) {
            intent = new Intent(LoginActivity.this, LockActivity.class);
        } else if (userId != null) {
            intent = new Intent(LoginActivity.this, SetPinActivity.class);
        } else {
            intent = new Intent(LoginActivity.this, MenuActivity.class);
        }
        startActivity(intent);
        finish();
    }

    /**
     * Loads the password recovery activity.
     */
    private void loadPwdRecovery() {
        Intent showPwdRecovery = new Intent(LoginActivity.this,PasswordRecoveryActivity.class);
        startActivity(showPwdRecovery);
    }

    /**
     * Loads the sign-up activity.
     */
    private void loadSignUp() {
        Intent showSignup = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(showSignup);
    }
}
