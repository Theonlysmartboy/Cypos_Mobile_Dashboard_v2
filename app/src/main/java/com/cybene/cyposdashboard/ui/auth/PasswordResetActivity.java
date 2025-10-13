package com.cybene.cyposdashboard.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.ui.MenuActivity;
import com.cybene.cyposdashboard.utils.AppConfig;
import com.cybene.cyposdashboard.utils.NetworkUtils;
import com.cybene.cyposdashboard.utils.ValidateInput;
import com.cybene.cyposdashboard.utils.db.Db;
import com.cybene.cyposdashboard.utils.db.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PasswordResetActivity extends AppCompatActivity implements TextWatcher {
    private TextView back;
    private EditText nameText,emailText, passwordText, cPasswordText;
    private Button submit;
    private static final String TAG = PasswordResetActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    Db mydb;
    private String clientName, clientEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        submit = findViewById(R.id.btn_save);
        back = findViewById(R.id.link_back);
        nameText = findViewById(R.id.input_RFname);
        emailText = findViewById(R.id.input_Remail);
        passwordText = findViewById(R.id.txtRPass);
        cPasswordText = findViewById(R.id.txtCPass);
        // Progress dialog
        pDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        pDialog.setCancelable(false);
        mydb = new Db(this);
        Cursor clientData = mydb.getUser();
        if(clientData.getCount()>0){
            clientData.moveToFirst();
            do{
                clientName = clientData.getString(1);
                clientEmail = clientData.getString(2);

            }while (clientData.moveToNext());
            clientData.close();
            nameText.setText(clientName);
            emailText.setText(clientEmail);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Home = new Intent(PasswordResetActivity.this, MenuActivity.class);
                startActivity(Home);
                finish();
            }
        });
        cPasswordText.addTextChangedListener(this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fullname = nameText.getText().toString().trim();
                final String password = passwordText.getText().toString().trim();
                final String cpass = cPasswordText.getText().toString().trim();
                if(valid(fullname, password)){
                    update(clientEmail, fullname, password);

                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(passwordText.getText().length()<8 && !cPasswordText.getText().toString().trim().equals(passwordText.getText().toString().trim())){
            cPasswordText.setError("Password mismatch");
            submit.setEnabled(false);
        }else{
            submit.setEnabled(true);
        }
    }
    private boolean valid(String fullname, String password) {
        ValidateInput validateInput = new ValidateInput();
        boolean valid = true;
        if(!validateInput.validateText(fullname)){
            nameText.setError("Please provide a valid company name");
            valid = false;
        }
        else if(!validateInput.validatePassword(password)){
            passwordText.setError("Password must contain at least one lowercase letter, one uppercase " +
                    "letter, one special character one numeric character and must be at least " +
                    "8 characters long");
            valid = false;
        }
        return valid;
    }
    private void update( final String clientEmail, final String fullname, final String password) {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            NetworkUtils.showNoInternetDialog(this, true); // true = allow exit
            return; // Stop further execution
        }
        // Tag used to cancel the request
        pDialog.setMessage("Resetting ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_PASSWORD_RESET, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Reset Response: " + response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_LONG).show();
                        //clear shared frefs
                        SharedPrefs.getInstance().saveString("is_logged_in", "");
                        //delete user
                        mydb.deleteUser();
                        // Launch login activity
                        Intent intent = new Intent(PasswordResetActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: ", e );
                }

            }
        }, error -> {
            Log.e(TAG, "Reset Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(), " An error has occurred during data update "+error.getMessage(), Toast.LENGTH_LONG).show();
            hideDialog();
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("Id", clientEmail);
                params.put("name", fullname);
                params.put("password", password);
                return params;
            }
        };
        // Adding request to request queue
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                1000*10,
                /*DefaultRetryPolicy.DEFAULT_MAX_RETRIES*/ 3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue pwdResetRequestQue = Volley.newRequestQueue(this);
        pwdResetRequestQue.add(strReq);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}